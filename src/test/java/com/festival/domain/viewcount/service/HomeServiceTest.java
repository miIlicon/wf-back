package com.festival.domain.viewcount.service;

import com.festival.domain.viewcount.model.Home;
import com.festival.domain.viewcount.repository.HomeRepository;
import com.festival.domain.viewcount.util.ViewCountUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class HomeServiceTest {

    @InjectMocks
    private HomeService homeService;

    @Mock
    private ViewCountUtil viewCountUtil;

    @Mock
    private HomeRepository homeRepository;

    @DisplayName("조회수를 Redis에서 검증하고 1회 증가시킨다.")
    @Test
    void increaseHomeViewCount() throws Exception {
        //given
        String mockIp = "123.123.123.123";
        given(viewCountUtil.isDuplicatedAccess(mockIp, "Home")).willReturn(false);

        //when
        homeService.increaseHomeViewCount(mockIp);

        //then
        then(viewCountUtil).should(times(1)).increaseData("viewCount_Home_1");
        then(viewCountUtil).should(times(1)).setDuplicateAccess(mockIp, "Home");
    }

    @DisplayName("조회수를 갯수만큼 업데이트 한다.")
    @Test
    void updateHomeViewCount() throws Exception {
        //given
        given(homeRepository.findById(anyLong())).willReturn(java.util.Optional.of(new Home(0)));
        given(viewCountUtil.getViewCount("viewCount_Home_1")).willReturn(10L);

        //when
        homeService.updateHomeViewCount(1L, 10);
        Long viewCountHome = viewCountUtil.getViewCount("viewCount_Home_1");
        //then
        assertThat(viewCountHome).isEqualTo(10L);
    }
}