package com.festival.common.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceUtils {

    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    public String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    public List<String> saveSubImages(List<MultipartFile> subFiles) throws IOException {

        List<String> subFilePaths = new ArrayList<>();

        for (MultipartFile subFile : subFiles) {
            if (subFile.getOriginalFilename() != null) {
                String subFileName = createStoreFileName(subFile.getOriginalFilename());
                ObjectMetadata objMeta = new ObjectMetadata();
                objMeta.setContentLength(subFile.getInputStream().available());

                amazonS3.putObject(bucket, subFileName, subFile.getInputStream(), objMeta);
            }
        }
        return subFilePaths;
    }

    public String saveMainFile(MultipartFile mainFile) throws IOException {
        String mainFileName = createStoreFileName(mainFile.getOriginalFilename());
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(mainFile.getInputStream().available());

        amazonS3.putObject(bucket, mainFileName, mainFile.getInputStream(), objMeta);
        return mainFileName;
    }
}
