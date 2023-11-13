package com.festival.domain.viewcount.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HomeTest {

    @DisplayName("조회수 객체는 초기값이 0이다.")
    @Test
    void createdViewCount() throws Exception {
        //given //when
        Home home = new Home(0);

        //then
        assertThat(home.getCount()).isEqualTo(0);
    }

    @DisplayName("조회수 객체의 조회수는 더해진만큼 조회수가 증가한다.")
    @Test
    void plusViewCount() throws Exception {
        //given
        Home home = new Home(0);

        //when
        home.plus(10);

        //then
        assertThat(home.getCount()).isEqualTo(10);
    }
}