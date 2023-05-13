package com.festival.domain.info.festivalEvent.service;

import com.festival.domain.admin.data.entity.Admin;
import com.festival.domain.admin.exception.AdminNotFoundException;
import com.festival.domain.admin.repository.AdminRepository;
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
    private final AdminRepository adminRepository;
    private final FestivalEventRepository festivalEventRepository;
    private final FestivalEventImageRepository festivalEventImageRepository;
    private final EntityManager em;

    @Value("${cloud.aws.s3.bucket}")
    private String filePath;

    private static String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private static String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    @Transactional
    public FestivalEventRes create(FestivalEventReq festivalEventReq, MultipartFile mainFile, List<MultipartFile> subFiles) throws IOException {
        Admin admin = adminRepository.findById(1L).orElseThrow(() -> new AdminNotFoundException("관리자를 찾을 수 없습니다."));


        String mainFileName = saveMainFile(mainFile); // 메인 파일 저장
        List<String> subFileNames = saveSubFiles(subFiles); // 서브 파일 저장


        // 파일명으로 Entity생성 후 저장
        FestivalEventImage festivalEventImage = FestivalEventImage.of(mainFileName, subFileNames);
        festivalEventImageRepository.save(festivalEventImage);

        FestivalEvent festivalEvent = FestivalEvent.of(festivalEventReq, festivalEventImage, admin);
        festivalEventRepository.save(festivalEvent);


        return FestivalEventRes.of(festivalEvent, filePath);
    }

    public FestivalEventRes find(Long festivalEventId) {
        FestivalEvent festivalEvent = festivalEventRepository.findById(festivalEventId).orElseThrow(() -> new FestivalEventNotFoundException("존재하지 않는 게시물입니다."));
        return FestivalEventRes.of(festivalEvent, filePath);

    }

    public Page<FestivalEventRes> list(long l, int offset, Boolean state) {

        Pageable pageable = PageRequest.of(offset, 6);
        Page<FestivalEvent> festivalEvents = festivalEventRepository.findByAdminIdAndFestivalEventState(1L, true, pageable);
        return festivalEvents.map(festivalEvent -> FestivalEventRes.of(festivalEvent, filePath));

    }

    @Transactional
    public FestivalEventRes modify(Long festivalEventId, FestivalEventReq festivalEventReq, MultipartFile mainFile, List<MultipartFile> subFiles) throws IOException {
        FestivalEvent festivalEvent = festivalEventRepository.findById(festivalEventId).orElseThrow(() -> new FestivalEventNotFoundException("존재하지 않는 게시물입니다."));
        FestivalEventImage festivalEventImage = festivalEvent.getFestivalEventImage();

        festivalEvent.modify(festivalEventReq);
        festivalEventImage.deleteOriginalFile(filePath);

        String mainFileName = saveMainFile(mainFile); // 메인 파일 저장
        List<String> subFileNames = saveSubFiles(subFiles); // 서브 파일 저장
        festivalEventImage.modify(mainFileName, subFileNames);

        return FestivalEventRes.of(festivalEvent, filePath);
    }

    @Transactional
    public FestivalEventRes delete(Long festivalEventId) {
        FestivalEvent festivalEvent = festivalEventRepository.findById(festivalEventId).orElseThrow(() -> new FestivalEventNotFoundException("이미 삭제된 게시물입니다."));
        FestivalEventImage festivalEventImage = festivalEvent.getFestivalEventImage();
        festivalEventImage.deleteOriginalFile(filePath);
        festivalEventRepository.delete(festivalEvent);

        return FestivalEventRes.of(festivalEvent, filePath);

    }

    private String saveMainFile(MultipartFile mainFile) throws IOException {
        String fileName = createStoreFileName(mainFile.getOriginalFilename());
        mainFile.transferTo(new File(filePath + fileName));

        return fileName;
    }

    private List<String> saveSubFiles(List<MultipartFile> subFiles) throws IOException {
        List<String> subFileNames = new ArrayList<>();
        for (MultipartFile subFile : subFiles) {
            String fileName = createStoreFileName(subFile.getOriginalFilename());
            subFileNames.add(fileName);
            subFile.transferTo(new File(filePath + fileName));
        }
        return subFileNames;
    }


}
