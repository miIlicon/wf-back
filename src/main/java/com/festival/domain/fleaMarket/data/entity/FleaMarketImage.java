package com.festival.domain.fleaMarket.data.entity;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FleaMarketImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "market_image_id")
    private Long id;

    @Column(name = "main_file_name", nullable = false)
    private String mainFileName;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "flea_market_sub_image", joinColumns = @JoinColumn(name = "market_image_id"))
    @Column(name = "file_name")
    private List<String> subFileNames = new ArrayList<>();

    @OneToOne(mappedBy = "fleaMarketImage", fetch = FetchType.LAZY)
    private FleaMarket fleaMarket;

    public FleaMarketImage(FleaMarket fleaMarket) {
        this.fleaMarket = fleaMarket;
    }

    public void connectFileNames(String mainFileName, List<String> subFileNames) {
        this.mainFileName = mainFileName;
        this.subFileNames = subFileNames;
    }

    public void saveSubFileNames(List<String> subFileNames) {
        this.subFileNames = subFileNames;
    }

    public void modifySubFileNames(String filePath, List<String> subFilePath) {
        for (String subFile : this.subFileNames) {
            File file = new File(filePath + subFile);
            file.delete();
        }
        this.subFileNames = subFilePath;
    }

    public void modifyMainFileName(String filePath, String mainFilePath, MultipartFile mainFile) throws IOException {
        File file = new File(filePath + this.mainFileName);
        file.delete();

        this.mainFileName = mainFilePath;
        mainFile.transferTo(new File(filePath + mainFilePath));
    }

    public void deleteFile(AmazonS3 amazonS3, String bucket) {

        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, this.mainFileName);
        amazonS3.deleteObject(deleteObjectRequest);

        for (String subFile : this.subFileNames) {
            deleteObjectRequest = new DeleteObjectRequest(bucket, subFile);
            amazonS3.deleteObject(deleteObjectRequest);
        }
    }
}