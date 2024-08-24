package com.easymap.easymap.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.easymap.easymap.dto.process.map.pedestrian.PedestrianLinkProcessDTO;
import com.easymap.easymap.dto.process.map.pedestrian.PedestrianNodeProcessDTO;
import com.easymap.easymap.dto.request.map.RouteGetRequestDTO;
import com.easymap.easymap.dto.request.map.UserRoutePostRequestDTO;
import com.easymap.easymap.dto.response.map.MapPoisDTO;
import com.easymap.easymap.dto.response.map.RouteDTO;
import com.easymap.easymap.dto.response.map.RouteNodeDTO;
import com.easymap.easymap.entity.*;
import com.easymap.easymap.entity.category.Category;
import com.easymap.easymap.handler.exception.ResourceNotFoundException;
import com.easymap.easymap.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.AStarAdmissibleHeuristic;
import org.jgrapht.alg.shortestpath.AStarShortestPath;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.locationtech.jts.geom.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MapServiceImpl implements MapService{

    private final PoiRepository poiRepository;

    private final UserRepository userRepository;

    private final DetailedCategoryRepository detailedCategoryRepository;

    private final UserRouteRepository userRouteRepository;

    private final PedestrianNodeRepository pedestrianNodeRepository;

    private final PedestrianLinkRepository pedestrianLinkRepository;

    private final AmazonS3 amazonS3Client;

    @Value("${aws.s3.user-rawdata-bucket-name}")
    private String bucketName;

    private static final double EARTH_RADIUS = 6378137; // 지구의 반경 (미터)

    private static final double maxPossibleAngle = 5.5;

    final static Double wheelChairSpeed = 1.5;



    @Transactional(readOnly = true)
    @Override
    public List<MapPoisDTO> getPoisOnMap(List<Double> bbox) {
        Double smLat = Math.min(bbox.get(0), bbox.get(2));
        Double bLat = Math.max(bbox.get(0), bbox.get(2));
        Double smLng = Math.min(bbox.get(1), bbox.get(3));
        Double bLng = Math.max(bbox.get(1), bbox.get(3));

        List<Poi> poisInBbox = poiRepository.findPoiInBbox(null, smLat, bLat, smLng, bLng);

        Category build = Category.builder().categoryId(4L).build();

        List<Long> obstacleDcIdList = detailedCategoryRepository.findDetailedCategoryByCategory(build).stream().map(dc -> dc.getDetailedCategoryId()).collect(Collectors.toList());

        List<MapPoisDTO> collect = poisInBbox.stream().map(poi -> MapPoisDTO.builder()
                        .poiId(poi.getPoiId())
                        .poiName(poi.getPoiName())
                        .type(obstacleDcIdList.contains(poi.getPoiId()) ? "obstacle" : "place")
                        .lat(poi.getPoiLatitude())
                        .lng(poi.getPoiLongitude())
                        .build())
                .collect(Collectors.toList());


        return collect;
    }

    @Override
    public Long postUserRoute(UserRoutePostRequestDTO userRoutePostRequestDTO, UserDetails userDetails) {

        User user = userRepository.findUserByEmailAndDeactivationDateIsNull(userDetails.getUsername()).orElseThrow(() -> new ResourceNotFoundException("no user such as : " + userDetails.getUsername()));

        StringBuilder sb = new StringBuilder();
        sb.append(user.getUserId()).append(",");
        sb.append(userRoutePostRequestDTO.getStartTime().toString()).append(",");
        sb.append(userRoutePostRequestDTO.getHop()).append(",");

        String collect = userRoutePostRequestDTO.getData().stream().map(userRouteCoordiDTO -> "(" + userRouteCoordiDTO.getLat() + "," + userRouteCoordiDTO.getLng() + ")")
                .collect(Collectors.joining(","));
        sb.append(collect);

        String csvData = sb.toString();


        String fileName = user.getUserId()+"-"+userRoutePostRequestDTO.getStartTime().toString()+ UUID.randomUUID().toString()+".csv";

        try(InputStream inputStream = new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8))){
            ObjectMetadata metadata = new ObjectMetadata();

            metadata.setContentType("text/csv");
            metadata.setContentLength(csvData.length());

            amazonS3Client.putObject(bucketName, fileName, inputStream, metadata);

            UserRoute userRoute = UserRoute.builder()
                    .user(user)
                    .fileName(fileName)
                    .createdAt(userRoutePostRequestDTO.getStartTime())
                    .build();

            UserRoute save = userRouteRepository.save(userRoute);
            return save.getId();

        }catch (Exception e){
            e.printStackTrace();
            return -1L;
        }


    }

    @Transactional(readOnly = true)
    @Override
    public List<RouteDTO> getRouteBetweenPois(RouteGetRequestDTO requestDTO) {
        GeometryFactory geometryFactory = new GeometryFactory();

        final double paddingMeter = 50.0;

        // 두 POI 점을 기준으로 일정 범위에서 존재하는 노드, 링크 데이터를 가져오는 로직

        // 시작점, 끝점 확인
        Poi startPoi = poiRepository.findById(requestDTO.getStartPoiId()).orElseThrow(() -> new ResourceNotFoundException("no poi : " + requestDTO.getStartPoiId()));
        Poi endPoi = poiRepository.findById(requestDTO.getEndPoiId()).orElseThrow(() -> new ResourceNotFoundException("no poi : " + requestDTO.getStartPoiId()));

        // 시작점 끝점을 바탕으로 한 bbox 생성

        Point startPoint = geometryFactory.createPoint(new Coordinate(startPoi.getPoiLongitude(), startPoi.getPoiLatitude()));
        startPoint.setSRID(4326);
        Point endPoint = geometryFactory.createPoint(new Coordinate(endPoi.getPoiLongitude(), endPoi.getPoiLatitude()));
        endPoint.setSRID(4326);

        Map<String, Double> map = makeBbox(startPoint, endPoint, paddingMeter);


        // 노드 리스트 가져오기
        List<PedestrianNode> nodeList = pedestrianNodeRepository.findPedestrianNodesInBbox(map.get("minLng"), map.get("minLat"), map.get("maxLng"), map.get("maxLat"));
        List<PedestrianNodeProcessDTO> nodeProcessDTOList  = nodeList.stream().map(node-> PedestrianNode.mapToDTO(node)).collect(Collectors.toList());


        // 링크 리스트 가져오기
        List<PedestrianLink> linkList = pedestrianLinkRepository.findPedestrianLinksInBbox(map.get("minLng"), map.get("minLat"), map.get("maxLng"), map.get("maxLat"));
        List<PedestrianLinkProcessDTO> linkProcessDTOList = linkList.stream()
                .peek(link-> Optional.of(link).orElseThrow(()-> new ResourceNotFoundException("해당 범위 내에 정의된 링크가 없습니다.")))
                .map(PedestrianLinkProcessDTO::new).collect(Collectors.toList());

        // 시작점, 도착점에 가장 가까운 노드 산출


        PedestrianNode startRawNode = pedestrianNodeRepository.findClosestNodeWithinDistance(startPoint, paddingMeter)
                .orElseThrow(() -> new ResourceNotFoundException("poi에 인접 노드가 존재하지 않습니다. poiId : " + startPoi.getPoiId()));
        PedestrianNode endRawNode = pedestrianNodeRepository.findClosestNodeWithinDistance(endPoint, paddingMeter)
                .orElseThrow(() -> new ResourceNotFoundException("poi에 인접 노드가 존재하지 않습니다. poiId : " + endPoi.getPoiId()));

        PedestrianNodeProcessDTO startNode = PedestrianNode.mapToDTO(startRawNode);
        PedestrianNodeProcessDTO endNode = PedestrianNode.mapToDTO(endRawNode);

        // 두 POI 사이의 장애물 데이터를 가져오는 로직
        List<Poi> poiInBbox = poiRepository.findPoiInBbox(4L, map.get("minLat"), map.get("maxLat"), map.get("minLng"), map.get("maxLng"));

        // 그래프 선언
        Graph<PedestrianNodeProcessDTO, PedestrianLinkProcessDTO> graph = new SimpleWeightedGraph<>(PedestrianLinkProcessDTO.class);

        // 가저온 노드와 링크를 JGraphT에 그래프에 등록하는 로직
        nodeProcessDTOList.forEach(node-> graph.addVertex(node));

        linkProcessDTOList.forEach(link-> graph.addEdge(link.getFromNodeDTO(), link.getToNodeDTO(), link));

        // 장애물 poi로 가중치 설정
        poiInBbox.stream()
                //.filter(poi-> poi.getDetailedCategory().getCategory().getCategoryName().equals("보행 장애물"))
                .forEach(poi -> {

                    // 가져온 장애물 데이터의 장애 유형을 판별하고, 해당 장애물에서 일정 범위 내의 노드, 링크를 가중치를 부여하는 로직
                    // 카테고리 디테일 타입(보행 불가, 보행 불편)에 따른 장애물 범위 설정
                    Integer lenByType = poi.getDetailedCategory().getDetailedCategoryId().equals(17L)?2:1;

                    // 장애물 타입에 따른 가중치
                    Integer weightByType = 50;

                    Polygon buffer = createBuffer(poi.getPoiLatitude(), poi.getPoiLongitude(), lenByType);

                    linkProcessDTOList.stream().filter(link-> buffer.intersects(link.getGeom()))
                            .forEach(link-> graph.setEdgeWeight(link, weightByType));

                });

        // 휴리스틱 함수 설정
        // 최단 거리 휴리스틱
        AStarAdmissibleHeuristic<PedestrianNodeProcessDTO> shortestHeruistic = (n1, n2)-> {
            PedestrianLinkProcessDTO link = graph.getEdge(n1, n2);
            if(link !=null){
                return link.getLinkLen();
            }
            return Double.MAX_VALUE;

        };

        AStarAdmissibleHeuristic<PedestrianNodeProcessDTO> lowestSlopeHeuristic = (n1, n2)-> {
            PedestrianLinkProcessDTO link = graph.getEdge(n1, n2);
            if(link !=null){

                // 경사도 기반 휴리스틱 함수
                Integer slopeMax = link.getSlopeMax();
                Integer slopeAvg = link.getSlopeAvg();
                Integer slopeMedian = link.getSlopeMedian();

                Integer rst = slopeMax * 3 + slopeAvg + slopeMedian;
                return Double.valueOf(rst);
            }
            return Double.MAX_VALUE;

        };


        // 최단 거리 경로를 산출할 휴리스틱 함수 주입하여 최단거리 뽑아내는 로직

        AStarShortestPath<PedestrianNodeProcessDTO, PedestrianLinkProcessDTO> shortestPath = new AStarShortestPath<>(graph, shortestHeruistic);
        GraphPath<PedestrianNodeProcessDTO, PedestrianLinkProcessDTO> shortestGraphPath = shortestPath.getPath(startNode, endNode);

        // 최완만 경로를 산출할 휴리스틱 함수를 주입하고, 최대한 완만한 경로를 뽑아내는 로직
        AStarShortestPath<PedestrianNodeProcessDTO, PedestrianLinkProcessDTO> lowestSlopePath = new AStarShortestPath<>(graph, lowestSlopeHeuristic);
        GraphPath<PedestrianNodeProcessDTO, PedestrianLinkProcessDTO> lowestSlopeGraphPath = lowestSlopePath.getPath(startNode, endNode);


        // 각각의 경로 데이터를 DTO에 적합한 형식으로 묶어 가공하는 로직

        RouteDTO shortest = buildRouteDTO(shortestGraphPath, "SHORTEST", startPoi, endPoi);
        RouteDTO lowestSlope = buildRouteDTO(lowestSlopeGraphPath, "LOWEST_SLOPE", startPoi, endPoi);

        // 결과물 DTO 리턴

        List<RouteDTO> routeDTOS = List.of(shortest, lowestSlope);

        return routeDTOS;
    }

    private RouteDTO buildRouteDTO(GraphPath<PedestrianNodeProcessDTO, PedestrianLinkProcessDTO> shortestGraphPath, String type, Poi startPoi, Poi endPoi ) {

        RouteDTO build = RouteDTO.builder()
                .distance(shortestGraphPath.getEdgeList().stream().mapToDouble(edge -> edge.getLinkLen()).sum())
                .slope(shortestGraphPath.getEdgeList().stream().mapToDouble(edge -> Double.valueOf(edge.getSlopeMax())).max().getAsDouble())
                .type(type)
                .timeRequired(shortestGraphPath.getEdgeList().stream().mapToDouble(edge->edge.getLinkLen()/wheelChairSpeed).sum())
                .list(shortestGraphPath.getVertexList()
                        .stream()
                        .map(vertex -> RouteNodeDTO.builder()
                                .lng(vertex.getGeom().getY())
                                .lat(vertex.getGeom().getX())
                                .build())
                        .collect(Collectors.toList()))
                .build();

        // 시작점, 끝점 끼워넣기
        build.getList().add(0, RouteNodeDTO.builder()
                        .lat(startPoi.getPoiLatitude())
                        .lng(startPoi.getPoiLongitude())
                .build());
        build.getList().add(RouteNodeDTO.builder()
                        .lat(endPoi.getPoiLatitude())
                        .lng(endPoi.getPoiLongitude())
                .build());

        return build;
    }

    public static Map<String, Double> makeBbox(Point startPoint, Point endPoint, double bufferInMeters) {

        // 위도와 경도의 거리 변환
        double latBuffer = bufferInMeters / EARTH_RADIUS * (180 / Math.PI);
        double lonBuffer = bufferInMeters / (EARTH_RADIUS * Math.cos(Math.toRadians((startPoint.getY() + endPoint.getY()) / 2))) * (180 / Math.PI);

        Map<String, Double> map = new HashMap<>();
        map.put("minLat", Math.min(startPoint.getY(), endPoint.getY()) - latBuffer);
        map.put("maxLat", Math.max(startPoint.getY(), endPoint.getY()) + latBuffer);
        map.put("minLng", Math.min(startPoint.getX(), endPoint.getX()) - lonBuffer);
        map.put("maxLng", Math.max(startPoint.getX(), endPoint.getX()) + lonBuffer);

        return map;
    }


    private static Polygon createBuffer(double latitude, double longitude, double distanceInMeters) {

        GeometryFactory geometryFactory = new GeometryFactory();

        // 중심 좌표로부터 계산된 거리를 이용해 버퍼 생성
        Point centerPoint = geometryFactory.createPoint(new Coordinate(longitude, latitude));


        return (Polygon) centerPoint.buffer(distanceInMeters);

    }
}
