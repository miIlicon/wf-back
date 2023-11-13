package com.festival.domain.viewcount.util;

import com.festival.domain.util.ControllerTestSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ViewCountUtilTest extends ControllerTestSupport {

    @AfterEach
    void tearDown() {
        redisService.initialize();
    }

    @DisplayName("이미 조회한 사용자인지 확인한다.")
    @Test
    void isDuplicatedAccess() throws Exception {
        //given
        String testIpAddress = "123.123.123.123";
        String testDomainName = "Home";

        //when
        boolean isDuplicatedAccess = viewCountUtil.isDuplicatedAccess(testIpAddress, testDomainName);

        //then
        assertThat(isDuplicatedAccess).isFalse();
    }

    @DisplayName("조회한 사용자를 Redis에 저장한다.")
    @Test
    void setDuplicateAccess() throws Exception {
        //given
        String testIpAddress = "123.123.123.123";
        String testDomainName = "Home";

        //when
        viewCountUtil.setDuplicateAccess(testIpAddress, testDomainName);

        //then
        Long viewCount = viewCountUtil.getViewCount(testIpAddress + "_" + testDomainName);
        assertThat(viewCount).isEqualTo(1L);
    }

    @DisplayName("조회수를 Redis에서 가져온다.")
    @Test
    void getViewCount() throws Exception {
        //given
        String testIpAddress = "456.456.456.456";
        String testDomainName = "Home";
        viewCountUtil.setDuplicateAccess("viewCount_" + testIpAddress, testDomainName);

        //when
        String testKey = "viewCount_456.456.456.456_Home";
        Long viewCount = viewCountUtil.getViewCount(testKey);

        //then
        assertThat(viewCount).isEqualTo(1L);
    }

    @DisplayName("조회수 키를 가져온다.")
    @Test
    void getKeySet() throws Exception {
        //given
        String testIpAddress = "123.123.123.123";
        String testDomainName = "Home";
        viewCountUtil.setDuplicateAccess("viewCount_" + testIpAddress, testDomainName);

        //when
        Set<String> keySet = viewCountUtil.getKeySet("viewCount*");

        //then
        assertThat(keySet).contains("viewCount_123.123.123.123_Home");
    }

    @DisplayName("")
    @Test
    void deleteData() throws Exception {
        //given
        String testIpAddress = "123.123.123.123";
        String testDomainName = "Home";
        viewCountUtil.setDuplicateAccess(testIpAddress, testDomainName);

        //when
        String testKey = "viewCount_Home_1";
        viewCountUtil.deleteData(testKey);

        //then
        redisService.getData(testKey);
        assertThat(redisService.getData(testKey)).isNull();
    }

    @DisplayName("")
    @Test
    void increaseData() throws Exception {
        //given
        String testIpAddress = "123.123.123.123";
        String testDomainName = "Home";
        viewCountUtil.setDuplicateAccess(testIpAddress, testDomainName);

        //when
        String testKey = "viewCount_Home_1";
        viewCountUtil.increaseData(testKey);

        //then
        Long viewCount = viewCountUtil.getViewCount(testKey);
        assertThat(viewCount).isEqualTo(1L);
    }
}