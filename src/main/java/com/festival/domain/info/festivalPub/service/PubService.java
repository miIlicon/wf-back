package com.festival.domain.info.festivalPub.service;

import com.festival.domain.admin.data.entity.Admin;
import com.festival.domain.admin.exception.AdminException;
import com.festival.domain.admin.repository.AdminRepository;
import com.festival.domain.info.festivalPub.data.dto.request.PubRequest;
import com.festival.domain.info.festivalPub.data.dto.response.PubResponse;
import com.festival.domain.info.festivalPub.data.entity.file.PubImage;
import com.festival.domain.info.festivalPub.data.entity.file.SubFilePath;
import com.festival.domain.info.festivalPub.data.entity.pub.Pub;
import com.festival.domain.info.festivalPub.repository.PubImageRepository;
import com.festival.domain.info.festivalPub.repository.SubFilePathRepository;
import com.festival.domain.info.festivalPub.repository.PubRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@Transactional
@RequiredArgsConstructor
public class PubService {

    private final PubRepository pubRepository;
    private final AdminRepository adminRepository;
    private final SubFilePathRepository filePathRepository;
    private final PubImageRepository pubImageRepository;

    private final EntityManager em;

    @Value("${file.path}")
    private String filePath;

    public PubResponse create(Long adminId, PubRequest pubRequest) throws IOException {

        Admin saveAdmin = new Admin();
        adminRepository.save(saveAdmin);

        em.flush();
        em.clear();

        Admin admin = adminRepository.findById(adminId).orElseThrow(() -> new AdminException("관리자를 찾을 수 없습니다."));

        Pub pub = new Pub(pubRequest, admin);
        pubRepository.save(pub);
        admin.addPub(pub);

        String mainFileName = createStoreFileName(pubRequest.getMainFile().getOriginalFilename());
        pubRequest.getMainFile().transferTo(new File(filePath + mainFileName));

        PubImage pubImage = new PubImage(pubRequest, mainFileName, filePath + mainFileName, pub);
        pubImageRepository.save(pubImage);

        List<SubFilePath> subFilePaths = saveSubImages(pubRequest.getSubFiles(), pubImage);
        pubImage.setSubFilePath(subFilePaths);
        pub.setPubImage(pubImage);

        return PubResponse.of(pub, filePath);
    }

    private List<SubFilePath> saveSubImages(List<MultipartFile> subFiles, PubImage pubImage) throws IOException {

        List<SubFilePath> subFilePaths = new ArrayList<>();

        for (MultipartFile subFile : subFiles) {
            if (!subFile.isEmpty()) {
                SubFilePath savePath = new SubFilePath(subFile.getOriginalFilename(),subFile.getContentType(), createStoreFileName(subFile.getOriginalFilename()), pubImage);
                filePathRepository.save(savePath);
                subFilePaths.add(savePath);
                subFile.transferTo(new File(filePath + savePath.getFilePath()));
            }
        }
        return subFilePaths;
    }

    private static String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private static String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
