package com.festival.domain.booth.service;

import com.festival.domain.booth.controller.dto.BoothListReq;
import com.festival.domain.booth.controller.dto.BoothReq;
import com.festival.domain.booth.controller.dto.BoothRes;
import com.festival.domain.booth.model.Booth;
import com.festival.domain.booth.repository.BoothCustomRepository;
import com.festival.domain.booth.repository.BoothRepository;
import com.festival.domain.booth.service.vo.BoothListSearchCond;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BoothService {

    private BoothRepository boothRepository;
    private BoothCustomRepository boothCustomRepository;

    public Long createBooth(BoothReq boothReq) {
        return boothRepository.save(Booth.of(boothReq)).getId();
    }
    public Long updateBooth(BoothReq boothReq, Long id) {
        Booth booth = boothRepository.findById(id).orElse(null);
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

    public List<BoothRes> getBoothList(BoothListReq boothListReq, Pageable pageable) {
        boothCustomRepository.getList(BoothListSearchCond.builder()
                .status(boothListReq.getStatus())
                .type(boothListReq.getType()).build(), pageable);

        return null;
    }
}
