package com.festival.domain.guide.dalguji.service;

import com.festival.common.exception.ErrorCode;
import com.festival.common.exception.custom_exception.ForbiddenException;
import com.festival.common.exception.custom_exception.NotFoundException;
import com.festival.common.util.SecurityUtils;
import com.festival.domain.guide.dalguji.dto.DalgujiReq;
import com.festival.domain.guide.dalguji.dto.DalgujiRes;
import com.festival.domain.guide.dalguji.model.Dalguji;
import com.festival.domain.guide.dalguji.repository.DalgujiRepository;
import com.festival.domain.image.model.Image;
import com.festival.domain.image.service.ImageService;
import com.festival.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class DalgujiService {

    private final DalgujiRepository dalgujiRepository;

    private final MemberService memberService;
    private final ImageService imageService;

    @Transactional
    public String createDalgujiImage(DalgujiReq dalgujiReq) {
        List<Image> images = dalgujiReq.getDalgujiFiles().stream().map(file -> imageService.uploadImage(file, "Dalguji")).toList();
        Dalguji dalguji = new Dalguji(dalgujiReq.getCollege(), images);

        dalguji.connectMember(memberService.getAuthenticationMember());
        return dalgujiRepository.save(dalguji).getCollege();
    }

    @Transactional
    public String addDalgugiImage(String college, DalgujiReq dalgujiReq) {
        Dalguji dalguji = dalgujiRepository.findById(college)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_OBJECT));

        if (!SecurityUtils.checkingRole(dalguji.getMember(), memberService.getAuthenticationMember())) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_UPDATE);
        }

        List<Image> images = dalgujiReq.getDalgujiFiles().stream().map(file -> imageService.uploadImage(file, "Dalguji")).toList();
        dalguji.addImages(images);
        return dalguji.getCollege();
    }

    @Transactional
    public void deleteDalgujiImage(String college, int imageId) {
        Dalguji dalguji = dalgujiRepository.findById(college)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_OBJECT));

        if (!SecurityUtils.checkingRole(dalguji.getMember(), memberService.getAuthenticationMember())) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_DELETE);
        }

        Image deletedImage = dalguji.getImages().get(imageId);
        imageService.deleteImage(deletedImage);
        dalguji.deleteImage(deletedImage);
    }

    @Transactional
    public void deleteDalgujiImagesAll(String college) {
        Dalguji dalguji = dalgujiRepository.findById(college)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_OBJECT));

        if (!SecurityUtils.checkingRole(dalguji.getMember(), memberService.getAuthenticationMember())) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_DELETE);
        }

        imageService.deleteImages(dalguji.getImages());
        dalgujiRepository.delete(dalguji);
    }

    public DalgujiRes getDalgujiImages(String college) {
        Dalguji dalguji = dalgujiRepository.findById(college)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_OBJECT));

        List<String> filePaths = dalguji.getImages().stream().map(Image::getFilePath).toList();
        return new DalgujiRes(filePaths);
    }
}
