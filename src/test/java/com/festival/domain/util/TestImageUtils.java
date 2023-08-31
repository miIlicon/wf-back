package com.festival.domain.util;

import jakarta.validation.constraints.NotNull;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestImageUtils {
    /**
     * @return MockMultipartFile
     * @description 테스트용 이미지 생성
     */
    public static MockMultipartFile generateMockImageFile(String fileName) throws IOException {
        return new MockMultipartFile(
                fileName,
                "test_image.jpg",
                "image/jpg",
                new ClassPathResource("/static/image/test_image.jpg").getInputStream()
        );
    }
}
