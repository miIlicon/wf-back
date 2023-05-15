package com.festival.domain.info.festivalPub.service;

import com.amazonaws.services.s3.AmazonS3;
import com.festival.common.base.CommonIdResponse;
import com.festival.common.utils.ImageServiceUtils;
import com.festival.common.vo.SearchCond;
import com.festival.domain.admin.data.entity.Admin;
import com.festival.domain.admin.exception.AdminNotFoundException;
import com.festival.domain.admin.exception.AdminNotMatchException;
import com.festival.domain.admin.repository.AdminRepository;
import com.festival.domain.info.festivalPub.data.dto.request.PubRequest;
import com.festival.domain.info.festivalPub.data.dto.response.PubListResponse;
import com.festival.domain.info.festivalPub.data.dto.response.PubResponse;
import com.festival.domain.info.festivalPub.data.entity.file.PubImage;
import com.festival.domain.info.festivalPub.data.entity.pub.Pub;
import com.festival.domain.info.festivalPub.exception.PubNotFoundException;
import com.festival.domain.info.festivalPub.repository.PubImageRepository;
import com.festival.domain.info.festivalPub.repository.PubRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PubService {

    private final PubRepository pubRepository;
    private final PubImageRepository pubImageRepository;

    private final AdminRepository adminRepository;
    private final ImageServiceUtils utils;

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("https://${cloud.aws.s3.bucket}.s3.ap-northeast-2.amazonaws.com/")
    private String filePath;

    public CommonIdResponse create(PubRequest pubRequest, MultipartFile mainFile, List<MultipartFile> subFiles) throws IOException {

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Admin admin = adminRepository.findByUsername(name).orElseThrow(() -> new AdminNotFoundException("관리자를 찾을 수 없습니다."));

        Pub pub = new Pub(pubRequest);
        pub.connectAdmin(admin);
        pubRepository.save(pub);

        String mainFileName = utils.saveMainFile(mainFile);
        List<String> subFileNames = utils.saveSubImages(subFiles);

        PubImage pubImage = new PubImage(pub);
        pubImage.connectFileNames(mainFileName, subFileNames);

        pubImageRepository.save(pubImage);
        pub.connectPubImage(pubImage);

        return new CommonIdResponse(pub.getId());
    }

    public CommonIdResponse modify(Long pubId, PubRequest pubRequest, MultipartFile mainFile, List<MultipartFile> subFiles) throws IOException {

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Admin admin = adminRepository.findByUsername(name).orElseThrow(() -> new AdminNotFoundException("관리자를 찾을 수 없습니다."));

        Pub pub = pubRepository.findById(pubId).orElseThrow(() -> new PubNotFoundException("주점을 찾을 수 없습니다."));

        if (pub.getAdmin().equals(admin)) {

            PubImage pubImage = pub.getPubImage();
            pubImage.deleteFile(amazonS3, bucket);

            String mainFileName = utils.saveMainFile(mainFile);
            List<String> subFileNames = utils.saveSubImages(subFiles);

            pubImage.connectFileNames(mainFileName, subFileNames);
            pub.modify(pubRequest);

            return new CommonIdResponse(pub.getId());
        } else {
            throw new AdminNotMatchException("권한이 없습니다.");
        }
    }

    public CommonIdResponse delete(Long pubId) {

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Admin admin = adminRepository.findByUsername(name).orElseThrow(() -> new AdminNotFoundException("관리자를 찾을 수 없습니다."));

        Pub pub = pubRepository.findById(pubId).orElseThrow(() -> new PubNotFoundException("주점을 찾을 수 없습니다."));

        if (pub.getAdmin().equals(admin)) {
            pub.getPubImage().deleteFile(amazonS3, bucket);
            pubRepository.delete(pub);

            return new CommonIdResponse(pub.getId());
        } else {
            throw new AdminNotMatchException("권한이 없습니다.");
        }
    }

    @Transactional(readOnly = true)
    public PubResponse getPub( Long pubId) {
        Pub pub = pubRepository.findById(pubId).orElseThrow(() -> new PubNotFoundException("주점을 찾을 수 없습니다."));
        return PubResponse.of(pub, filePath);
    }

    @Transactional(readOnly = true)
    public Page<PubListResponse> getPubs(int offset, boolean state) {
        Pageable pageable = PageRequest.of(offset, 20);
        SearchCond cond = new SearchCond(state);

        Page<Pub> findPubs = pubRepository.findByIdPubs(cond, pageable);
        return findPubs.map(pub -> PubListResponse.of(pub, filePath));
    }
}
