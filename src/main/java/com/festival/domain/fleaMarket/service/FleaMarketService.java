package com.festival.domain.fleaMarket.service;

import com.amazonaws.services.s3.AmazonS3;
import com.festival.common.base.CommonIdResponse;
import com.festival.common.utils.ImageServiceUtils;
import com.festival.common.vo.SearchCond;
import com.festival.domain.admin.data.entity.Admin;
import com.festival.domain.admin.exception.AdminNotFoundException;
import com.festival.domain.admin.exception.AdminNotMatchException;
import com.festival.domain.admin.repository.AdminRepository;
import com.festival.domain.fleaMarket.data.dto.request.FleaMarketRequest;
import com.festival.domain.fleaMarket.data.dto.response.FleaMarketListResponse;
import com.festival.domain.fleaMarket.data.dto.response.FleaMarketResponse;
import com.festival.domain.fleaMarket.data.entity.FleaMarket;
import com.festival.domain.fleaMarket.data.entity.FleaMarketImage;
import com.festival.domain.fleaMarket.exception.FleaMarketNotFoundException;
import com.festival.domain.fleaMarket.repository.FleaMarketImageRepository;
import com.festival.domain.fleaMarket.repository.FleaMarketRepository;
import jakarta.persistence.EntityManager;
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

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FleaMarketService {

    private final FleaMarketRepository fleaMarketRepository;
    private final FleaMarketImageRepository fleaMarketImageRepository;

    private final AdminRepository adminRepository;
    private final ImageServiceUtils utils;

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("https://${cloud.aws.s3.bucket}.s3.ap-northeast-2.amazonaws.com/")
    private String filePath;

    public CommonIdResponse create(FleaMarketRequest fleaMarketRequest, MultipartFile mainFile, List<MultipartFile> subFiles) throws IOException {

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Admin admin = adminRepository.findByUsername(name).orElseThrow(() -> new AdminNotFoundException("관리자를 찾을 수 없습니다."));

        FleaMarket fleaMarket = new FleaMarket(fleaMarketRequest);
        fleaMarket.connectAdmin(admin);
        fleaMarketRepository.save(fleaMarket);

        String mainFileName = utils.saveMainFile(mainFile);
        List<String> subFileNames = utils.saveSubImages(subFiles);

        FleaMarketImage fleaMarketImage = new FleaMarketImage(fleaMarket);
        fleaMarketImage.connectFileNames(mainFileName, subFileNames);

        fleaMarketImageRepository.save(fleaMarketImage);
        fleaMarket.connectMarketImage(fleaMarketImage);

        return new CommonIdResponse(fleaMarket.getId());
    }

    public CommonIdResponse modify(Long fleaMarketId, FleaMarketRequest fleaMarketRequest, MultipartFile mainFile, List<MultipartFile> subFiles) throws IOException {

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Admin admin = adminRepository.findByUsername(name).orElseThrow(() -> new AdminNotFoundException("관리자를 찾을 수 없습니다."));

        FleaMarket fleaMarket = fleaMarketRepository.findById(fleaMarketId).orElseThrow(() -> new FleaMarketNotFoundException("플리마켓을 찾을 수 없습니다."));

        if (fleaMarket.getAdmin().equals(admin)) {

            FleaMarketImage fleaMarketImage = fleaMarket.getFleaMarketImage();
            fleaMarketImage.deleteFile(amazonS3, bucket);

            String mainFileName = utils.saveMainFile(mainFile);
            List<String> subFileNames = utils.saveSubImages(subFiles);

            fleaMarketImage.connectFileNames(mainFileName, subFileNames);
            fleaMarket.modify(fleaMarketRequest);

            return new CommonIdResponse(fleaMarket.getId());
        } else {
            throw new AdminNotMatchException("권한이 없습니다.");
        }
    }

    public FleaMarketResponse delete(Long fleaMarketId) {

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Admin admin = adminRepository.findByUsername(name).orElseThrow(() -> new AdminNotFoundException("관리자를 찾을 수 없습니다."));

        FleaMarket fleaMarket = fleaMarketRepository.findById(fleaMarketId).orElseThrow(() -> new FleaMarketNotFoundException("플리마켓을 찾을 수 없습니다."));

        if (fleaMarket.getAdmin().equals(admin)) {
            fleaMarket.getFleaMarketImage().deleteFile(amazonS3, bucket);
            fleaMarketRepository.delete(fleaMarket);

            return FleaMarketResponse.of(fleaMarket, filePath);
        } else {
            throw new AdminNotMatchException("권한이 없습니다.");
        }
    }

    @Transactional(readOnly = true)
    public FleaMarketResponse getFleaMarket( Long fleaMarketId) {
        FleaMarket fleaMarket = fleaMarketRepository.findById(fleaMarketId).orElseThrow(() -> new FleaMarketNotFoundException("플리마켓을 찾을 수 없습니다."));
        return FleaMarketResponse.of(fleaMarket, filePath);
    }

    @Transactional(readOnly = true)
    public Page<FleaMarketListResponse> getFleaMarkets(int offset, boolean state) {
        Pageable pageable = PageRequest.of(offset, 20);
        SearchCond cond = new SearchCond(state);

        Page<FleaMarket> markets = fleaMarketRepository.findByIdFleaMarkets(cond, pageable);
        return markets.map(fleaMarket -> FleaMarketListResponse.of(fleaMarket, filePath));
    }
}
