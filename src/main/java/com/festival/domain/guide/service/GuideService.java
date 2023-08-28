package com.festival.domain.guide.service;

import com.festival.domain.guide.dto.GuideReq;
import com.festival.domain.guide.dto.GuideRes;
import com.festival.domain.guide.model.Guide;
import com.festival.domain.guide.repository.GuideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.festival.domain.guide.model.GuideStatus.TERMINATE;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GuideService {

    private final GuideRepository guideRepository;

    @Transactional
    public Long createGuide(GuideReq guideReq) {
        Guide guide = Guide.of(guideReq);
        Guide savedGuide = guideRepository.save(guide);
        return savedGuide.getId();
    }

    @Transactional
    public Long updateGuide(Long id, GuideReq guideReq) {
        Guide guide = guideRepository.findById(id).orElseThrow();
        guide.update(guideReq);
        return guide.getId();
    }

    @Transactional
    public void deleteGuide(Long id) {
        Guide guide = guideRepository.findById(id).orElseThrow();
        guide.changeStatus(TERMINATE);
    }

    public GuideRes getGuide(Long id){
        Guide guide = guideRepository.findById(id).orElseThrow();
        return GuideRes.of(guide);
    }

    public Page<GuideRes> getGuideList(String status, Pageable pageable) {
        return guideRepository.getList(status, pageable);
    }
}
