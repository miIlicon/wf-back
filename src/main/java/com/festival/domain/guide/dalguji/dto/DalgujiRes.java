package com.festival.domain.guide.dalguji.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class DalgujiRes {

    private List<String> subFilePaths;

    public DalgujiRes(List<String> subFilePaths) {
        this.subFilePaths = subFilePaths;
    }
}
