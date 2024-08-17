package com.easymap.easymap.util.search;


import com.easymap.easymap.util.search.dto.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class SearchUtil {

    @Value("${juso.conformKey}")
    private String conformKey;

    private int currentPage = 1;
    private int countPerPage = 10;

    public SearchResult searchKeyword(String keyword){
        RestTemplate restTemplate = new RestTemplate();
        String url ="https://business.juso.go.kr/addrlink/addrLinkApi.do?confmKey="+ conformKey +"&keyword="+keyword+"&currentPage="+currentPage+"&countPerPage="+countPerPage+"&resultType=json";
        SearchResult results = restTemplate.getForObject(url, SearchResult.class);

        if(!results.getResults().getCommon().getErrorCode().equals("0")){
            // TODO 에러 핸들링
        }


        log.info(results.toString());

        return results;
    }

}
