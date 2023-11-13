package com.festival.domain.viewcount.repository;

import com.festival.domain.viewcount.model.Home;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class HomeRepositoryTest {

    @Autowired
    private HomeRepository homeRepository;

    @DisplayName("메인 조회수 객체를 생성한다.")
    @Test
    void createdHome() throws Exception {
        //given
        Home home = new Home(0);

        //when
        Home savedHome = homeRepository.save(home);

        //then
        assertThat(savedHome.getCount()).isEqualTo(home.getCount());
    }
}