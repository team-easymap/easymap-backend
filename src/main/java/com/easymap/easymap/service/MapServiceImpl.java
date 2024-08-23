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
import com.easymap.easymap.repository.DetailedCategoryRepository;
import com.easymap.easymap.repository.PoiRepository;
import com.easymap.easymap.repository.UserRepository;
import com.easymap.easymap.repository.UserRouteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.AStarAdmissibleHeuristic;
import org.jgrapht.alg.shortestpath.AStarShortestPath;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MapServiceImpl implements MapService{

    private final PoiRepository poiRepository;

    private final UserRepository userRepository;

    private final DetailedCategoryRepository detailedCategoryRepository;

    private final UserRouteRepository userRouteRepository;

    private final AmazonS3 amazonS3Client;

    @Value("${aws.s3.user-rawdata-bucket-name}")
    private String bucketName;



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

    @Override
    public List<RouteDTO> getRouteBetweenPois(RouteGetRequestDTO requestDTO) {
        // 두 POI 점을 기준으로 일정 범위에서 존재하는 노드, 링크 데이터를 가져오는 로직

        Poi startPoi = poiRepository.findById(requestDTO.getStartPoiId()).orElseThrow(() -> new ResourceNotFoundException("no poi : " + requestDTO.getStartPoiId()));
        Poi endPoi = poiRepository.findById(requestDTO.getEndPoiId()).orElseThrow(() -> new ResourceNotFoundException("no poi : " + requestDTO.getStartPoiId()));

        // 시작점, 도착점에 가장 가까운 노드 산출
        PedestrianNodeProcessDTO startNode = new PedestrianNodeProcessDTO();
        PedestrianNodeProcessDTO endNode = new PedestrianNodeProcessDTO();

        List<PedestrianNodeProcessDTO> nodeProcessDTOList = new ArrayList<>();
        List<PedestrianLinkProcessDTO> linkProcessDTOList = new ArrayList<>();

        // 시작점, 도착점에 가장 가까운 노드 가져오는

        // 두 POI 사이의 장애물 데이터를 가져오는 로직
        Double smLat = Math.min(startPoi.getPoiLatitude(), endPoi.getPoiLatitude());
        Double bLat = Math.max(startPoi.getPoiLatitude(), endPoi.getPoiLatitude());
        Double smLng = Math.min(startPoi.getPoiLongitude(), endPoi.getPoiLongitude());
        Double bLng = Math.max(startPoi.getPoiLongitude(), endPoi.getPoiLongitude());
        // 일단 패딩 생략

        List<Poi> poiInBbox = poiRepository.findPoiInBbox(4L, smLat, bLat, smLng, bLng);

        // 그래프 선언
        Graph<PedestrianNodeProcessDTO, PedestrianLinkProcessDTO> graph = new SimpleWeightedGraph<>(PedestrianLinkProcessDTO.class);

        // 가저온 노드와 링크를 JGraphT에 그래프에 등록하는 로직
        nodeProcessDTOList.forEach(node-> graph.addVertex(node));

        linkProcessDTOList.forEach(link-> graph.addEdge(link.getFromNodeDTO(), link.getToNodeDTO(), link));

        // 장애물 poi로 가중치 설정
        poiInBbox.stream()
                //.filter(poi-> poi.getDetailedCategory().getCategory().getCategoryName().equals("보행 장애물"))
                .forEach(poi -> {

                    // 가져온 장애물 데이터의 장애 유형을 판별하고, 해당 장애물에서 일정 범위 내의 노드, 링크를 제외하는 로직
                    // (이때 출발점, 도착점이 제외되어서는 안됨)
                    // 이 로직을 최완만 경로에만 적용해야 하나 고민 중.
                    Integer lenByType = poi.getDetailedCategory().getDetailedCategoryId().equals(17L)?2:1;

                    Integer weightByType = 50;

                    Polygon buffer = createBuffer(poi.getPoiLatitude(), poi.getPoiLongitude(), lenByType);

                    linkProcessDTOList.stream().filter(link-> buffer.intersects(link.getGeom()))
                            .forEach(link-> graph.setEdgeWeight(link, weightByType));


                });

        AStarAdmissibleHeuristic<PedestrianNodeProcessDTO> shortestHeruistic = (n1, n2)-> {
            PedestrianLinkProcessDTO link = graph.getEdge(n1, n2);
            if(link !=null){
                // 경사도 기반 휴리스틱 함수

                return link.getLinkLen();

            }
            return Double.MAX_VALUE;

        };

        AStarAdmissibleHeuristic<PedestrianNodeProcessDTO> lowestSlopeHeuristic = (n1, n2)-> {
            PedestrianLinkProcessDTO link = graph.getEdge(n1, n2);
            if(link !=null){
                // 경사도 기반 휴리스틱 함수

                return 0.0;

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
        RouteDTO shortest = RouteDTO.builder()
                .distance(shortestGraphPath.getEdgeList().stream().mapToDouble(edge -> edge.getLinkLen()).sum())
                .slope(shortestGraphPath.getEdgeList().stream().mapToDouble(edge -> Double.valueOf(edge.getSlopeMax())).max().getAsDouble())
                .type("SHORTEST")
                .list(shortestGraphPath.getVertexList().stream().map(vertex -> RouteNodeDTO.builder().lng(vertex.getGeom().getY()).lat(vertex.getGeom().getX()).build()).collect(Collectors.toList()))
                .build();

        RouteDTO lowestSlope = RouteDTO.builder()
                .distance(lowestSlopeGraphPath.getEdgeList().stream().mapToDouble(edge -> edge.getLinkLen()).sum())
                .slope(lowestSlopeGraphPath.getEdgeList().stream().mapToDouble(edge -> Double.valueOf(edge.getSlopeMax())).max().getAsDouble())
                .type("LOWEST_SLOPE")
                .list(lowestSlopeGraphPath.getVertexList().stream().map(vertex -> RouteNodeDTO.builder().lng(vertex.getGeom().getY()).lat(vertex.getGeom().getX()).build()).collect(Collectors.toList()))
                .build();

        // 결과물 DTO 리턴

        List<RouteDTO> routeDTOS = List.of(shortest, lowestSlope);

        return routeDTOS;
    }

    private static Polygon createBuffer(double latitude, double longitude, double distanceInMeters) {
        double EARTH_RADIUS = 6378137;

        GeometryFactory geometryFactory = new GeometryFactory();

        // 경위도 좌표를 미터 단위로 변환하여 반경 계산
        double latDistance = distanceInMeters / EARTH_RADIUS * (180 / Math.PI);
        double lonDistance = distanceInMeters / (EARTH_RADIUS * Math.cos(Math.toRadians(latitude))) * (180 / Math.PI);

        // 중심 좌표로부터 계산된 거리를 이용해 버퍼 생성
        Coordinate[] coords = new Coordinate[5];
        coords[0] = new Coordinate(longitude - lonDistance, latitude - latDistance);
        coords[1] = new Coordinate(longitude + lonDistance, latitude - latDistance);
        coords[2] = new Coordinate(longitude + lonDistance, latitude + latDistance);
        coords[3] = new Coordinate(longitude - lonDistance, latitude + latDistance);
        coords[4] = coords[0]; // 폴리곤을 닫기 위해 처음 좌표로 돌아가기

        return geometryFactory.createPolygon(coords);
    }
}
