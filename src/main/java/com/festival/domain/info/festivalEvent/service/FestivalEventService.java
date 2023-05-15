package com.festival.domain.info.festivalEvent.service;

import com.amazonaws.services.s3.AmazonS3;
import com.festival.common.base.CommonIdResponse;
import com.festival.common.utils.ImageServiceUtils;
import com.festival.domain.admin.data.entity.Admin;
import com.festival.domain.admin.exception.AdminNotFoundException;
import com.festival.domain.admin.repository.AdminRepository;
import com.festival.domain.info.festivalEvent.data.dto.FestivalEventListRes;
import com.festival.domain.info.festivalEvent.data.dto.FestivalEventReq;
import com.festival.domain.info.festivalEvent.data.dto.FestivalEventRes;
import com.festival.domain.info.festivalEvent.data.entity.FestivalEvent;
import com.festival.domain.info.festivalEvent.data.entity.FestivalEventImage;
import com.festival.domain.info.festivalEvent.exception.FestivalEventNotFoundException;
import com.festival.domain.info.festivalEvent.repository.FestivalEventImageRepository;
import com.festival.domain.info.festivalEvent.repository.FestivalEventRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FestivalEventService {

    private final FestivalEventRepository festivalEventRepository;
    private final FestivalEventImageRepository festivalEventImageRepository;

    private final AdminRepository adminRepository;
    private final ImageServiceUtils utils;

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("https://${cloud.aws.s3.bucket}.s3.ap-northeast-2.amazonaws.com/")
    private String filePath;

    @Transactional
    public CommonIdResponse create(FestivalEventReq festivalEventReq, MultipartFile mainFile, List<MultipartFile> subFiles) throws IOException {

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Admin admin = adminRepository.findByUsername(name).orElseThrow(() -> new AdminNotFoundException("관리자를 찾을 수 없습니다."));

        String mainFileName = utils.saveMainFile(mainFile);
        List<String> subFileNames = utils.saveSubImages(subFiles);

        FestivalEventImage festivalEventImage = FestivalEventImage.of(mainFileName, subFileNames);
        festivalEventImageRepository.save(festivalEventImage);

        FestivalEvent festivalEvent = FestivalEvent.of(festivalEventReq, festivalEventImage, admin);
        festivalEventRepository.save(festivalEvent);

        festivalEventImage.connectFestivalEvent(festivalEvent);

        return new CommonIdResponse(festivalEvent.getId());
    }

    public FestivalEventRes find(Long festivalEventId) {
        FestivalEvent festivalEvent = festivalEventRepository.findById(festivalEventId).orElseThrow(() -> new FestivalEventNotFoundException("존재하지 않는 게시물입니다."));
        return FestivalEventRes.of(festivalEvent, filePath);
    }

    public Page<FestivalEventListRes> list(int offset, boolean state) {

        Pageable pageable = PageRequest.of(offset, 20);
        Page<FestivalEvent> festivalEvents = festivalEventRepository.findByFestivalEventState(pageable, state);
        return festivalEvents.map(festivalEvent -> FestivalEventListRes.of(festivalEvent, filePath));
    }

    @Transactional
    public CommonIdResponse modify(Long festivalEventId, FestivalEventReq festivalEventReq, MultipartFile mainFile, List<MultipartFile> subFiles) throws IOException {

        FestivalEvent festivalEvent = festivalEventRepository.findById(festivalEventId).orElseThrow(() -> new FestivalEventNotFoundException("존재하지 않는 게시물입니다."));
        FestivalEventImage festivalEventImage = festivalEvent.getFestivalEventImage();

        festivalEventImage.deleteOriginalFile(amazonS3, bucket);

        String mainFileName = utils.saveMainFile(mainFile);
        List<String> subFileNames = utils.saveSubImages(subFiles);

        festivalEventImage.modify(mainFileName, subFileNames);
        festivalEvent.modify(festivalEventReq);

        return new CommonIdResponse(festivalEventId);
    }

    @Transactional
    public CommonIdResponse delete(Long festivalEventId) {

        FestivalEvent festivalEvent = festivalEventRepository.findById(festivalEventId).orElseThrow(() -> new FestivalEventNotFoundException("이미 삭제된 게시물입니다."));
        FestivalEventImage festivalEventImage = festivalEvent.getFestivalEventImage();

        festivalEventImage.deleteOriginalFile(amazonS3, bucket);
        festivalEventRepository.delete(festivalEvent);

        return new CommonIdResponse(festivalEventId);
    }
}
