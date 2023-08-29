package com.festival.domain.booth.service;

import com.festival.common.util.ImageUtils;
import com.festival.domain.booth.controller.dto.BoothListReq;
import com.festival.domain.booth.controller.dto.BoothReq;
import com.festival.domain.booth.controller.dto.BoothRes;
import com.festival.domain.booth.model.Booth;
import com.festival.domain.booth.repository.BoothRepository;
import com.festival.domain.booth.service.vo.BoothListSearchCond;
import com.festival.domain.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BoothService {

    private final BoothRepository boothRepository;
    private final ImageService imageService;

    public Long createBooth(BoothReq boothReq) {
        Booth booth = Booth.of(boothReq);
        imageService.uploadImage(boothReq.getMainFile(), boothReq.getSubFiles(), boothReq.getType());
        return boothRepository.save(booth).getId();
    }



    public Long updateBooth(BoothReq boothReq, Long id) {
        Booth booth = boothRepository.findById(id).orElse(null);
        /**
         * @Todo
         *  delete로직 작성해야함
         */
        imageService.uploadImage(boothReq.getMainFile(), boothReq.getSubFiles(), boothReq.getType());
        booth.update(boothReq);
        return id;
    }

    public void deleteBooth(Long id) {
        Booth booth = boothRepository.findById(id).orElse(null);
        booth.delete();
    }

    public BoothRes getBooth(Long id) {
        return BoothRes.of(boothRepository.findById(id).orElse(null));
    }

/*    public List<BoothRes> getBoothList(BoothListReq boothListReq, Pageable pageable) {
        boothRepository.getList(BoothListSearchCond.builder()
                .status(boothListReq.getStatus())
                .type(boothListReq.getType()).build(), pageable);

        return null;
    }*/
}
