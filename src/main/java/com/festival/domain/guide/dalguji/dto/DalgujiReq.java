package com.festival.domain.guide.dalguji.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor
public class DalgujiReq {

    @NotBlank(message = "대학교 이름을 입력해주세요.")
    private String college;

    @NotNull(message = "파일을 선택해주세요.")
    private List<MultipartFile> dalgujiFiles;

    public DalgujiReq(String college, List<MultipartFile> dalgujiFiles) {
        this.college = college;
        this.dalgujiFiles = dalgujiFiles;
    }
}
