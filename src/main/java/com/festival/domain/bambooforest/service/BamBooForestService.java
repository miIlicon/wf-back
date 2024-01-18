package com.festival.domain.bambooforest.service;

import com.festival.common.exception.custom_exception.AlreadyDeleteException;
import com.festival.common.exception.custom_exception.ForbiddenException;
import com.festival.common.exception.custom_exception.NotFoundException;
import com.festival.domain.bambooforest.dto.BamBooForestPageRes;
import com.festival.domain.bambooforest.dto.BamBooForestReq;
import com.festival.domain.bambooforest.model.BamBooForest;
import com.festival.domain.bambooforest.repository.BamBooForestRepository;
import com.festival.domain.member.model.Member;
import com.festival.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.festival.common.exception.ErrorCode.*;
import static com.festival.common.util.SecurityUtils.checkingAdminRole;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BamBooForestService {

    private final BamBooForestRepository bamBooForestRepository;

    private final MemberService memberService;

    @Transactional
    public Long createBamBooForest(BamBooForestReq bambooForestReq) {
        BamBooForest bamBooForest = BamBooForest.of(bambooForestReq);
        BamBooForest savedBamBooForest = bamBooForestRepository.save(bamBooForest);
        return savedBamBooForest.getId();
    }

    @Transactional
    public void deleteBamBooForest(Long id) {
        BamBooForest findBamBooForest = checkingDeletedStatus(bamBooForestRepository.findById(id));

        Member accessUser = memberService.getAuthenticationMember();
        if (!checkingAdminRole(accessUser.getRole())) {
            throw new ForbiddenException(FORBIDDEN_DELETE);
        }

        findBamBooForest.deletedBambooForest();
    }

    public BamBooForestPageRes getBamBooForestList(Pageable pageable) {
        return bamBooForestRepository.getList(pageable);
    }

    private BamBooForest checkingDeletedStatus(Optional<BamBooForest> bamBooForest) {
        if (bamBooForest.isEmpty()) {
            throw new NotFoundException(NOT_FOUND_BAMBOO);
        }

        if (bamBooForest.get().isDeleted()) {
            throw new AlreadyDeleteException(ALREADY_DELETED);
        }
        return bamBooForest.get();
    }
}
