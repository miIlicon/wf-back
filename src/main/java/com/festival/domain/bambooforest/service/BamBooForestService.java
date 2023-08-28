package com.festival.domain.bambooforest.service;

import com.festival.domain.bambooforest.dto.BamBooForestCreateReq;
import com.festival.domain.bambooforest.dto.BamBooForestRes;
import com.festival.domain.bambooforest.model.BamBooForest;
import com.festival.domain.bambooforest.model.BamBooForestStatus;
import com.festival.domain.bambooforest.repository.BamBooForestRepository;
import com.festival.domain.bambooforest.service.vo.BamBooForestSearchCond;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BamBooForestService {

    private final BamBooForestRepository bamBooForestRepository;

    @Transactional
    public Long create(BamBooForestCreateReq bambooForestCreateReq) {
        BamBooForest bamBooForest = BamBooForest.of(bambooForestCreateReq);
        BamBooForest savedBamBooForest = bamBooForestRepository.save(bamBooForest);
        return savedBamBooForest.getId();
    }

    @Transactional
    public void delete(Long id) {
        BamBooForest bamBooForest = bamBooForestRepository.findById(id).orElseThrow();
        bamBooForest.changeStatus(BamBooForestStatus.TERMINATE);
    }

    public Page<BamBooForestRes> getBamBooForestList(String status, Pageable pageable) {
        BamBooForestSearchCond bamBooForestSearchCond = BamBooForestSearchCond.builder()
                .status(status)
                .pageable(pageable)
                .build();
        return bamBooForestRepository.getList(bamBooForestSearchCond);
    }
}
