package com.easymap.easymap.util.search;


import com.easymap.easymap.util.search.dto.AddressData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class SearchUtil {

    private final SearchResponseExtractor extractor = new SearchResponseExtractor();

    @Value("${juso.search.conformKey}")
    private String conformKey;

    private final int currentPage = 1;
    private final int countPerPage = 10;

    public List<AddressData>searchKeyword(String keyword){
        RestTemplate restTemplate = new RestTemplate();
        String url ="https://business.juso.go.kr/addrlink/addrLinkApi.do";
        URI uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("confmKey", conformKey)
                .queryParam("keyword", keyword)
                .queryParam("currentPage", currentPage)
                .queryParam("countPerPage", countPerPage)
                .queryParam("resultType", "json")
                .build()
                .encode()
                .toUri();

        return restTemplate.execute(uri, HttpMethod.GET, null, extractor);


    }

}
