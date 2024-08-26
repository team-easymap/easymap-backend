package com.easymap.easymap.util.coordinate;

import com.easymap.easymap.dto.request.search.SearchAddressPostRequestDTO;
import com.easymap.easymap.dto.response.search.AddressResultDTO;
import com.easymap.easymap.util.coordinate.dto.CoordinatesAndAddress;
import com.easymap.easymap.util.coordinate.dto.juso.CoordinateData;
import com.easymap.easymap.util.coordinate.dto.vworld.VworldAddressDTO;
import com.easymap.easymap.util.coordinate.dto.vworld.VworldCoordinateResponseDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class CoordinateConverterUtil {

    private String jusoConformKey;

    @Value("${apikey.vworld.conformKey}")
    private String vWorldConformKey;

    private final ObjectMapper objectMapper;

    public CoordinateData convertByDTOUsingJuso(SearchAddressPostRequestDTO requestDTO){
        RestTemplate restTemplate = new RestTemplate();
        String url ="https://business.juso.go.kr/addrlink/addrCoordApi.do";

        URI uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("confmKey", jusoConformKey)
                .queryParam("admCd", requestDTO.getAdmCd())
                .queryParam("reMgtSn", requestDTO.getRnMgtSn())
                .queryParam("udrtYn", requestDTO.getUdtrYn())
                .queryParam("buldMnnm", requestDTO.getBuldMnnm())
                .queryParam("buldSlno", requestDTO.getBuldSlno())
                .queryParam("resultType", "json")
                .build()
                .encode()
                .toUri();

        CoordinateData coordinateData = restTemplate.execute(uri, HttpMethod.GET, null, response -> {
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            JsonNode metaNode = rootNode.path("results").path("common");
            if (!metaNode.get("errorCode").asText().equals("0")) {
                throw new IOException(metaNode.get("errorMessage").asText());
            }
            JsonNode addressNode = rootNode.path("results").path("juso");

            return objectMapper.convertValue(addressNode, new TypeReference<CoordinateData>() {
            });
        });


        return coordinateData;
    }

    public CoordinatesAndAddress convertByAddressFromVworld(SearchAddressPostRequestDTO requestDTO) {
        RestTemplate restTemplate = new RestTemplate();


        URI uri = getVworldUri(requestDTO.getAddress(), "road");

        VworldCoordinateResponseDTO forObject = restTemplate.execute(uri, HttpMethod.GET, null, response -> {
            JsonNode rootNode = objectMapper.readTree(response.getBody());

            JsonNode responseNode = rootNode.path("response");

            return objectMapper.convertValue(responseNode, new TypeReference<VworldCoordinateResponseDTO>() {
            });
        });


        if(forObject.getStatus().equals("NOT_FOUND")){
            log.info("parcel convert---");
            forObject = restTemplate.execute(getVworldUri(requestDTO.getAddress(), "parcel"), HttpMethod.GET, null, response -> {
                JsonNode rootNode = objectMapper.readTree(response.getBody());

                JsonNode responseNode = rootNode.path("response");

                return objectMapper.convertValue(responseNode, new TypeReference<VworldCoordinateResponseDTO>() {
                });
            });
        }

        return CoordinatesAndAddress.mapFromVworldRst(forObject);
    }

    private URI getVworldUri(String address, String type) {
        String url ="https://api.vworld.kr/req/address";
        URI uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("service", "address")
                .queryParam("request", "getCoord")
                .queryParam("key", vWorldConformKey)
                .queryParam("address", address)
                .queryParam("type", type)
                .queryParam("refine", "true")
                .build()
                .encode()
                .toUri();
        return uri;
    }

    public AddressResultDTO convertCoordinateIntoAddress(Double latitude, Double longitude) {
        String url ="https://api.vworld.kr/req/address";
        URI uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("service", "address")
                .queryParam("request", "getAddress")
                .queryParam("key", vWorldConformKey)
                .queryParam("point", latitude+","+longitude)
                .queryParam("type", "BOTH")
                .build()
                .encode()
                .toUri();
        RestTemplate restTemplate = new RestTemplate();


        List<VworldAddressDTO> execute = restTemplate.execute(uri, HttpMethod.GET, null, response -> {
            JsonNode rootNode = objectMapper.readTree(response.getBody());

            JsonNode responseNode = rootNode.path("response").path("result");

            return objectMapper.convertValue(responseNode, new TypeReference<List<VworldAddressDTO>>() {
            });
        });



        return AddressResultDTO.builder().zipcode(execute.get(0).getZipcode())
                .road(execute.get(0).getType().equals("road")?execute.get(0).getText():execute.get(1).getText())
                .parcel(execute.get(0).getType().equals("parcel")?execute.get(0).getText():execute.get(1).getText())
                .build();

    }
}
