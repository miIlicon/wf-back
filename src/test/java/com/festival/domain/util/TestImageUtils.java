package com.festival.domain.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

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
