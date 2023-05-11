package com.festival.domain.info.festivalEvent.service;

import com.festival.domain.admin.data.entity.Admin;
import com.festival.domain.admin.repository.AdminRepository;
import com.festival.domain.info.festivalEvent.data.dto.FestivalEventReq;
import com.festival.domain.info.festivalEvent.data.dto.FestivalEventRes;
import com.festival.domain.info.festivalEvent.data.entity.FestivalEvent;
import com.festival.domain.info.festivalEvent.data.entity.FestivalEventImage;
import com.festival.domain.info.festivalEvent.repository.FestivalEventImageRepository;
import com.festival.domain.info.festivalEvent.repository.FestivalEventRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FestivalEventService {
    private final AdminRepository adminRepository;
    private final FestivalEventRepository festivalEventRepository;
    private final FestivalEventImageRepository festivalEventImageRepository;
    private final EntityManager em;
    @Transactional
    public FestivalEventRes create(FestivalEventReq festivalEventReq, MultipartFile mainFile, List<MultipartFile> subFiles) throws IOException {
        Admin admin = adminRepository.findById(1L).orElse(null);


        String mainFileName = createStoreFileName(mainFile.getOriginalFilename());
        List<String> subFileNames = createSubFileNames(subFiles);

        FestivalEventImage festivalEventImage = FestivalEventImage.of(mainFileName, subFileNames);
        festivalEventImageRepository.save(festivalEventImage);

        FestivalEvent festivalEvent = FestivalEvent.of(festivalEventReq, festivalEventImage, admin);
        festivalEventRepository.save(festivalEvent);


        return FestivalEventRes.of(festivalEvent);
    }

    private List<String> createSubFileNames(List<MultipartFile> subFiles) {
        List<String> subFileNames = new ArrayList<>();

        for(MultipartFile subFile: subFiles){
            subFileNames.add(createStoreFileName(subFile.getOriginalFilename()));
        }
        return subFileNames;
    }

    public FestivalEventRes find(Long festivalEventId) {
        FestivalEvent festivalEvent = festivalEventRepository.findById(festivalEventId).orElse(null);
        return FestivalEventRes.of(festivalEvent);

    }
    public Page<FestivalEventRes> list(long l, int offset, Boolean state) {

        Pageable pageable = PageRequest.of(offset, 6);
        Page<FestivalEvent> festivalEvents = festivalEventRepository.findByAdminIdAndFestivalEventState(1L, true, pageable);
        return festivalEvents.map(FestivalEventRes::of);

    }

    @Transactional
    public FestivalEventRes modify(Long festivalEventId, FestivalEventReq festivalEventReq, MultipartFile mainFile, List<MultipartFile> subFiles) {
        FestivalEvent festivalEvent = festivalEventRepository.findById(festivalEventId).orElse(null);
        FestivalEventImage festivalEventImage = festivalEvent.getFestivalEventImage();

        String mainFileName = createStoreFileName(mainFile.getOriginalFilename());
        List<String> subFileNames = createSubFileNames(subFiles);

        festivalEventImage.modify(mainFileName, subFileNames);
        festivalEvent.modify(festivalEventReq);

        return FestivalEventRes.of(festivalEvent);
    }

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
    public void delete(Long festivalEventId) {
        FestivalEvent festivalEvent = festivalEventRepository.findById(festivalEventId).orElse(null);

        festivalEventRepository.delete(festivalEvent);

    }
}
