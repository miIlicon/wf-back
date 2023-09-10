package com.festival.domain.program.dto;

import com.festival.domain.program.model.Program;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgramRes {

    private Long id;
    private String title;
    private String subTitle;
    private String content;
    private Float latitude;
    private Float longitude;
    private String status;
    private String type;
    private String mainFilePath;
    private List<String> subFilePaths;

    public static ProgramRes of(Program program) {
        return ProgramRes.builder()
                .id(program.getId())
                .title(program.getTitle())
                .subTitle(program.getSubTitle())
                .content(program.getContent())
                .type(program.getType().toString())
                .status(program.getStatus().toString())
                .longitude(program.getLongitude())
                .latitude(program.getLatitude())
                .mainFilePath(program.getImage().getMainFilePath())
                .subFilePaths(program.getImage().getSubFilePaths())
                .build();
    }
}
