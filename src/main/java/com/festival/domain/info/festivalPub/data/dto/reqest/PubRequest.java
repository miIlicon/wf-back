package com.festival.domain.info.festivalPub.data.dto.reqest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PubRequest {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @NotEmpty(message = "프로필 사진을 추가해주세요.")
    private MultipartFile mainFile;

    private List<MultipartFile> subFiles;

    public PubRequest(String title, String content, MultipartFile mainFile) {
        this.title = title;
        this.content = content;
        this.mainFile = mainFile;
    }

    public PubRequest(String title, String content, MultipartFile mainFile, List<MultipartFile> subFiles) {
        this.title = title;
        this.content = content;
        this.mainFile = mainFile;
        this.subFiles = subFiles;
    }
}
