package com.festival.domain.viewcount.service;

import com.festival.domain.viewcount.Home;
import com.festival.domain.viewcount.repository.HomeRepository;
import com.festival.domain.viewcount.util.ViewCountUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class HomeService {

    private final ViewCountUtil viewCountUtil;
    private final HomeRepository homeRepository;

    public void increase(String ipAddress) {
        if(!viewCountUtil.isDuplicatedAccess(ipAddress, "Home")) {
            viewCountUtil.increaseData("viewCount_Home_1");
            viewCountUtil.setDuplicateAccess(ipAddress, "Home");
        }
    }

    @Transactional
    public void update(Long id, long count){
        Home home = homeRepository.findById(id).orElse(null);
        home.plus(count);
    }
}
