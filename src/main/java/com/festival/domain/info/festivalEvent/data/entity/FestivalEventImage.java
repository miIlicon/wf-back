package com.festival.domain.info.festivalEvent.data.entity;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import jakarta.persistence.*;
import lombok.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FestivalEventImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String mainFileName;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> subFileNames = new ArrayList<>();

    @OneToOne(mappedBy = "festivalEventImage")
    private FestivalEvent festivalEvent;

    public void connectFestivalEvent(FestivalEvent festivalEvent) {
        this.festivalEvent = festivalEvent;
    }


    @Builder
    public FestivalEventImage(String mainFileName, List<String> subFileNames) throws IOException {
        this.mainFileName = mainFileName;
        this.subFileNames = subFileNames;
    }
    public static FestivalEventImage of(String mainFileName, List<String> subFileNames) throws IOException {
        return FestivalEventImage.builder()
                    .mainFileName(mainFileName)
                    .subFileNames(subFileNames)
                .build();
    }

    public void modify(String mainFilePath, List<String> subFileNames) {
        this.mainFileName = mainFilePath;
        this.subFileNames = subFileNames;
    }

    public void deleteOriginalFile(AmazonS3 amazonS3, String bucket) {
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, this.mainFileName);
        amazonS3.deleteObject(deleteObjectRequest);

        for (String subFile : this.subFileNames) {
            deleteObjectRequest = new DeleteObjectRequest(bucket, subFile);
            amazonS3.deleteObject(deleteObjectRequest);
        }
    }
}