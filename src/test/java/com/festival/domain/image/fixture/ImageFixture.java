package com.festival.domain.image.fixture;

import com.festival.domain.image.model.Image;

import java.util.List;

public class ImageFixture {
    public static Image IMAGE = Image.builder()
            .mainFilePath("/mainFile")
            .subFilePaths(List.of("/subFile1", "/subFile2"))
            .build();
}
