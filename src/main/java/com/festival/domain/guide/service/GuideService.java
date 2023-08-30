package com.festival.domain.guide.service;

import com.festival.domain.guide.dto.GuideReq;
import com.festival.domain.guide.dto.GuideRes;
import com.festival.domain.guide.model.Guide;
import com.festival.domain.guide.repository.GuideRepository;
import com.festival.domain.image.model.Image;
import com.festival.domain.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.festival.domain.guide.model.GuideStatus.TERMINATE;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GuideService {

    private final GuideRepository guideRepository;
    private final ImageService imageService;

    @Transactional
    public Long createGuide(GuideReq guideReq) {
        Guide guide = Guide.of(guideReq);
        guide.setImage(imageService.uploadImage(guideReq.getMainFile(), guideReq.getSubFiles(), guideReq.getType()));
        return guideRepository.save(guide).getId();
    }

    @Transactional
    public Long updateGuide(Long id, GuideReq guideReq) {
        Guide guide = guideRepository.findById(id).orElseThrow();
        guide.update(guideReq);
        guide.setImage(imageService.uploadImage(guideReq.getMainFile(), guideReq.getSubFiles(), guideReq.getType()));
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

    public List<GuideRes> getGuideList(String status, Pageable pageable) {
        List<Guide> guideList = guideRepository.getList(status, pageable);
        return guideList.stream().map(GuideRes::of).collect(Collectors.toList());
    }
}
