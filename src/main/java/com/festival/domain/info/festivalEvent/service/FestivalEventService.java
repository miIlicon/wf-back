package com.festival.domain.info.festivalEvent.service;

import com.festival.domain.admin.data.entity.Admin;
import com.festival.domain.admin.repository.AdminRepository;
import com.festival.domain.info.festivalEvent.data.dto.FestivalEventReq;
import com.festival.domain.info.festivalEvent.data.dto.FestivalEventRes;
import com.festival.domain.info.festivalEvent.data.entity.FestivalEvent;
import com.festival.domain.info.festivalEvent.data.entity.FestivalEventImage;
import com.festival.domain.info.festivalEvent.repository.FestivalEventImageRepository;
import com.festival.domain.info.festivalEvent.repository.FestivalEventRepository;
import com.festival.domain.info.festivalPub.data.dto.response.PubResponse;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FestivalEventService {
    private final AdminRepository adminRepository;
    private final FestivalEventRepository festivalEventRepository;
    private final FestivalEventImageRepository festivalEventImageRepository;
    public FestivalEventRes create(FestivalEventReq festivalEventReq, MultipartFile mainFile, List<MultipartFile> subFiles) throws IOException {
        Admin admin = new Admin();
        adminRepository.save(admin);

        FestivalEventImage festivalEventImage = FestivalEventImage.of(mainFile, subFiles);
        festivalEventImageRepository.save(festivalEventImage);

        FestivalEvent festivalEvent = FestivalEvent.of(festivalEventReq, festivalEventImage, admin);
        festivalEventRepository.save(festivalEvent);

        FestivalEventRes festivalEventRes = new FestivalEventRes();
        return festivalEventRes;
    }
}
