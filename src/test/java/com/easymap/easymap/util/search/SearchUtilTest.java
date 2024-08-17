package com.easymap.easymap.util.search;

import com.easymap.easymap.util.search.dto.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest
@ActiveProfiles("dev")
class SearchUtilTest {

    @Autowired
    private SearchUtil searchUtil;

    @Test
    public void 검색객체매핑테스트(){

        SearchResult result = searchUtil.searchKeyword("도선동");

        log.info(result.toString());

    }

}