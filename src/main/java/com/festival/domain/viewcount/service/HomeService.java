package com.festival.domain.viewcount.service;

import com.festival.common.exception.ErrorCode;
import com.festival.common.exception.custom_exception.NotFoundException;
import com.festival.domain.viewcount.model.Home;
import com.festival.domain.viewcount.repository.HomeRepository;
import com.festival.domain.viewcount.util.ViewCountUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class HomeService {

    private final ViewCountUtil viewCountUtil;
    private final HomeRepository homeRepository;

    public void increaseHomeViewCount(String ipAddress) {
        if(!viewCountUtil.isDuplicatedAccess(ipAddress, "Home")) {
            viewCountUtil.increaseData("viewCount_Home_1");
            viewCountUtil.setDuplicateAccess(ipAddress, "Home");
        }
    }

    @Transactional
    public void updateHomeViewCount(Long id, long count){
        Home home = homeRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_OBJECT));
        home.plus(count);
    }
}
