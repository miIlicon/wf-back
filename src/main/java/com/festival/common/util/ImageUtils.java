package com.festival.common.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class ImageUtils {


    public String upload(MultipartFile file, String kind) {
        return createFileName(file.getOriginalFilename(), kind);
    }

    public List<String> uploadMulti(List<MultipartFile> files, String kind) {
        return files.stream()
                .map(file -> upload(file, kind))
                .collect(Collectors.toList());
    }

    private String createFileName(String originalFilename, String kind) {
        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        return kind + "_" + UUID.randomUUID() + ext;
    }
}
