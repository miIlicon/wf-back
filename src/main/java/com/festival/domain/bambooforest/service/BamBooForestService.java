package com.festival.domain.bambooforest.service;

import com.festival.common.exception.custom_exception.ForbiddenException;
import com.festival.common.exception.custom_exception.NotFoundException;
import com.festival.domain.bambooforest.dto.BamBooForestCreateReq;
import com.festival.domain.bambooforest.dto.BamBooForestRes;
import com.festival.domain.bambooforest.model.BamBooForest;
import com.festival.domain.bambooforest.model.BamBooForestStatus;
import com.festival.domain.bambooforest.repository.BamBooForestRepository;
import com.festival.domain.bambooforest.service.vo.BamBooForestSearchCond;
import com.festival.domain.member.model.Member;
import com.festival.domain.member.model.MemberRole;
import com.festival.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.festival.common.exception.ErrorCode.*;
import static com.festival.common.util.SecurityUtils.checkingAdmin;
import static com.festival.common.util.SecurityUtils.checkingRole;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BamBooForestService {

    private final BamBooForestRepository bamBooForestRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long create(BamBooForestCreateReq bambooForestCreateReq) {
        BamBooForest bamBooForest = BamBooForest.of(bambooForestCreateReq);
        BamBooForest savedBamBooForest = bamBooForestRepository.save(bamBooForest);
        return savedBamBooForest.getId();
    }

    @Transactional
    public void delete(Long id, String accessUsername) {
        BamBooForest findBamBooForest = bamBooForestRepository.findById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND_BAMBOO));
        Member accessUser = memberRepository.findByLoginId(accessUsername).orElseThrow(() -> new NotFoundException(NOT_FOUND_MEMBER));
        if (!checkingAdmin(accessUser.getMemberRoles())) {
            throw new ForbiddenException(FORBIDDEN_DELETE);
        }
        findBamBooForest.changeStatus(BamBooForestStatus.TERMINATE);
    }

    public Page<BamBooForestRes> getBamBooForestList(String status, Pageable pageable) {
        BamBooForestSearchCond bamBooForestSearchCond = BamBooForestSearchCond.builder()
                .status(status)
                .pageable(pageable)
                .build();
        return bamBooForestRepository.getList(bamBooForestSearchCond);
    }


}
