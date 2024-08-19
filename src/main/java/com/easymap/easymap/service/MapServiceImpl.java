package com.easymap.easymap.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.easymap.easymap.dto.request.map.UserRoutePostRequestDTO;
import com.easymap.easymap.dto.response.map.MapPoisDTO;
import com.easymap.easymap.entity.Poi;
import com.easymap.easymap.entity.User;
import com.easymap.easymap.entity.UserRoute;
import com.easymap.easymap.entity.category.Category;
import com.easymap.easymap.handler.exception.ResourceNotFoundException;
import com.easymap.easymap.repository.DetailedCategoryRepository;
import com.easymap.easymap.repository.PoiRepository;
import com.easymap.easymap.repository.UserRepository;
import com.easymap.easymap.repository.UserRouteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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
}
