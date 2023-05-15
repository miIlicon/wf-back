package com.festival.domain.foodTruck.data.entity;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.festival.domain.info.festivalPub.data.entity.pub.Pub;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FoodTruckImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "main_file_name", nullable = false)
    private String mainFileName;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> subFileNames = new ArrayList<>();


    @OneToOne(mappedBy = "foodTruckImage", fetch = FetchType.LAZY)
    private FoodTruck foodTruck;

    public FoodTruckImage(String mainFilePath, FoodTruck foodTruck) {
        this.mainFileName = mainFilePath;
        this.foodTruck = foodTruck;
    }

    public void saveSubFileNames(List<String> subFileNames) {
        this.subFileNames = subFileNames;
    }

    public void connectFileNames(String mainFileName, List<String> subFileNames) {
        this.mainFileName = mainFileName;
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
