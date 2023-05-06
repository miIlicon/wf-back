package com.festival.domain.info.festivalPub.service;

import com.festival.domain.admin.data.entity.Admin;
import com.festival.domain.admin.exception.AdminException;
import com.festival.domain.admin.repository.AdminRepository;
import com.festival.domain.info.festivalPub.data.dto.reqest.PubRequest;
import com.festival.domain.info.festivalPub.data.dto.response.PubResponse;
import com.festival.domain.info.festivalPub.data.entity.file.PubImage;
import com.festival.domain.info.festivalPub.repository.FestivalPubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FestivalPubService {

    private final FestivalPubRepository festivalPubRepository;
    private final AdminRepository adminRepository;

    public PubResponse create(Long adminId, PubRequest pubRequest) {

        Admin admin = adminRepository.findById(adminId).orElseThrow(() -> new AdminException("관리자를 찾을 수 없습니다."));

        PubImage pubImage = PubImage.of(pubRequest);

        Diary diary = new Diary(diaryRequest.getTitle(), diaryRequest.getContent(), user, diaryContent);
        user.diariesAdd(diary);

        diaryContentRepository.save(diaryContent);
        diaryRepository.save(diary);

        return DiaryWithFileResponse.of(diary, diary.getDiaryContent().getFilePath());
    }


}
