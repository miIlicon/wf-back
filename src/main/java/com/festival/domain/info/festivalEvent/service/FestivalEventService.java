package com.festival.domain.info.festivalEvent.service;

import com.festival.domain.admin.data.entity.Admin;
import com.festival.domain.admin.repository.AdminRepository;
import com.festival.domain.info.festivalEvent.data.dto.FestivalEventReq;
import com.festival.domain.info.festivalEvent.data.dto.FestivalEventRes;
import com.festival.domain.info.festivalEvent.data.entity.FestivalEvent;
import com.festival.domain.info.festivalEvent.data.entity.FestivalEventImage;
import com.festival.domain.info.festivalEvent.repository.FestivalEventImageRepository;
import com.festival.domain.info.festivalEvent.repository.FestivalEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FestivalEventService {
    private final AdminRepository adminRepository;
    private final FestivalEventRepository festivalEventRepository;
    private final FestivalEventImageRepository festivalEventImageRepository;
    public FestivalEventRes create(FestivalEventReq festivalEventReq, MultipartFile mainFile, List<MultipartFile> subFiles) throws IOException {
        Admin admin = adminRepository.findById(1L).orElse(null);

        FestivalEventImage festivalEventImage = FestivalEventImage.of(mainFile, subFiles);
        festivalEventImageRepository.save(festivalEventImage);

        FestivalEvent festivalEvent = FestivalEvent.of(festivalEventReq, festivalEventImage, admin);
        festivalEventRepository.save(festivalEvent);


        return FestivalEventRes.of(festivalEvent);
    }

    public FestivalEventRes find(Long festivalEventId) {
        FestivalEvent festivalEvent = festivalEventRepository.findById(festivalEventId).orElse(null);
        return FestivalEventRes.of(festivalEvent);

        //FestivalEventRes festivalEventRes = new FestivalEventRes();
        //return festivalEventRes;
    }
    public Page<FestivalEventRes> list(long l, int offset, Boolean state) {

        Pageable pageable = PageRequest.of(offset, 6);
        Page<FestivalEvent> festivalEvents = festivalEventRepository.findByAdminIdAndFestivalEventState(1L, true, pageable);
        return festivalEvents.map(FestivalEventRes::of);

        //FestivalEventRes festivalEventRes = new FestivalEventRes();
        //return festivalEventRes;
    }
}
