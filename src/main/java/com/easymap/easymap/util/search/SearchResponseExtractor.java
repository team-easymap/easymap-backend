package com.easymap.easymap.util.search;

import com.easymap.easymap.util.search.dto.AddressData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseExtractor;

import java.io.IOException;
import java.util.List;

public class SearchResponseExtractor implements ResponseExtractor<List<AddressData>> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public List<AddressData> extractData(ClientHttpResponse response) throws IOException {
        JsonNode rootNode = objectMapper.readTree(response.getBody());
        JsonNode metaNode = rootNode.path("results").path("common");
        if(!metaNode.get("errorCode").asText().equals("0")){
            throw new IOException(metaNode.get("errorMessage").asText());
        }
        JsonNode addressNode = rootNode.path("results").path("juso");

        return objectMapper.convertValue(addressNode, new TypeReference<List<AddressData>>() {});

    }
}
