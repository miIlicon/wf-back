package com.festival.domain.image.service;

import com.festival.common.util.ImageUtils;
import com.festival.domain.booth.model.Booth;
import com.festival.domain.image.model.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageUtils imageUtils;

    public Image uploadImage(MultipartFile mainFile, List<MultipartFile> subFiles, String kind){
        String mainFilePath = imageUtils.upload(mainFile, kind);
        List<String> subFilePaths = imageUtils.uploadMulti(subFiles,kind);

        return Image.builder()
                .mainFilePath(mainFilePath)
                .subFilePaths(subFilePaths).build();
    }

}
