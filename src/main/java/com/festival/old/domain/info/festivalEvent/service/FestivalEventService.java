package com.festival.old.domain.info.festivalEvent.service;

import com.festival.old.common.base.CommonIdResponse;
import com.festival.old.common.utils.ImageServiceUtils;
import com.festival.old.domain.admin.data.entity.Admin;
import com.festival.old.domain.admin.exception.AdminNotFoundException;
import com.festival.old.domain.admin.repository.AdminRepository;
import com.festival.old.domain.info.festivalEvent.data.dto.FestivalEventListRes;
import com.festival.old.domain.info.festivalEvent.data.dto.FestivalEventReq;
import com.festival.old.domain.info.festivalEvent.data.dto.FestivalEventRes;
import com.festival.old.domain.info.festivalEvent.data.entity.FestivalEvent;
import com.festival.old.domain.info.festivalEvent.data.entity.FestivalEventImage;
import com.festival.old.domain.info.festivalEvent.exception.FestivalEventNotFoundException;
import com.festival.old.domain.info.festivalEvent.repository.FestivalEventImageRepository;
import com.festival.old.domain.info.festivalEvent.repository.FestivalEventRepository;
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

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FestivalEventService {

    private final FestivalEventRepository festivalEventRepository;
    private final FestivalEventImageRepository festivalEventImageRepository;

    private final AdminRepository adminRepository;

    @Value("${file.path}")
    private String filePath;

    @Transactional
    public CommonIdResponse create(FestivalEventReq festivalEventReq, MultipartFile mainFile, List<MultipartFile> subFiles) throws IOException {

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Admin admin = adminRepository.findByUsername(name).orElseThrow(() -> new AdminNotFoundException("관리자를 찾을 수 없습니다."));

        String mainFileName = saveMainFile(mainFile); // 메인 파일 저장
        List<String> subFileNames = saveSubFiles(subFiles); // 서브 파일 저장


        // 파일명으로 Entity생성 후 저장
        FestivalEventImage festivalEventImage = FestivalEventImage.of(mainFileName, subFileNames);
        festivalEventImageRepository.save(festivalEventImage);

        FestivalEvent festivalEvent = FestivalEvent.of(festivalEventReq, festivalEventImage, admin);
        festivalEventRepository.save(festivalEvent);


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

        festivalEvent.modify(festivalEventReq);
        festivalEventImage.deleteOriginalFile(filePath);

        String mainFileName = saveMainFile(mainFile); // 메인 파일 저장
        List<String> subFileNames = saveSubFiles(subFiles); // 서브 파일 저장
        festivalEventImage.modify(mainFileName, subFileNames);

        return new CommonIdResponse(festivalEventId);
    }

    @Transactional
    public CommonIdResponse delete(Long festivalEventId) {

        FestivalEvent festivalEvent = festivalEventRepository.findById(festivalEventId).orElseThrow(() -> new FestivalEventNotFoundException("이미 삭제된 게시물입니다."));
        FestivalEventImage festivalEventImage = festivalEvent.getFestivalEventImage();

        festivalEventImage.deleteOriginalFile(filePath);
        festivalEventRepository.delete(festivalEvent);

        return new CommonIdResponse(festivalEventId);
    }

    private String saveMainFile(MultipartFile mainFile) throws IOException {
        String fileName = ImageServiceUtils.createStoreFileName(mainFile.getOriginalFilename());
        mainFile.transferTo(new File(filePath + fileName));
        return fileName;
    }

    private List<String> saveSubFiles(List<MultipartFile> subFiles) throws IOException {
        List<String> subFileNames = new ArrayList<>();
        for (MultipartFile subFile : subFiles) {
            String fileName = ImageServiceUtils.createStoreFileName(subFile.getOriginalFilename());
            subFileNames.add(fileName);
            subFile.transferTo(new File(filePath + fileName));
        }
        return subFileNames;
    }
}
