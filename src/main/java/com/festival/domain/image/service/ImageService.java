package com.festival.domain.image.service;

import com.festival.common.exception.ErrorCode;
import com.festival.common.exception.custom_exception.NotFoundException;
import com.festival.common.util.ImageUtils;
import com.festival.domain.image.model.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static com.festival.common.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageUtils imageUtils;

    /**
     * @Description
     * 실제 S3와 연동되는 API입니다.

    public Image uploadImage(MultipartFile mainFile, List<MultipartFile> subFiles, String kind){
        String mainFilePath = imageUtils.upload(mainFile, kind);
        List<String> subFilePaths = imageUtils.uploadMulti(subFiles,kind);

        return Image.builder()
                .mainFilePath(mainFilePath)
                .subFilePaths(subFilePaths).build();
    }
     */

    /**
     * @Description
     * 이미지 경로만 DB에 저장하는 방식으로 구현해두었습니다.
     */
    public Image createImage(MultipartFile mainFile, List<MultipartFile> subFiles, String kind){
        String mainFilePath = null;
        List<String> subFilePaths = null;

        if (mainFile != null) {
            mainFilePath = createImage(mainFile, kind);
        }
        if (subFiles != null) {
            subFilePaths = createSubImages(subFiles, kind);
        }

        return Image.builder()
                .mainFilePath(mainFilePath)
                .subFilePaths(subFilePaths)
                .build();
    }

    private List<String> createSubImages(List<MultipartFile> subFiles, String kind) {
        return subFiles.stream().
                map(f -> createImage(f, kind))
                .collect(Collectors.toList());
    }

    private String createImage(MultipartFile mainFile, String kind) {
        return imageUtils.createFileName(mainFile.getOriginalFilename(), kind);
    }
}
