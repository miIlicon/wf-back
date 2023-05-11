package com.festival.domain.foodTruck.data.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FoodTruckImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "main_file_path", nullable = false)
    private String mainFilePath;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> subFilePaths;

    @OneToOne(mappedBy = "foodTruckImage")
    private FoodTruck foodTruck;

    public FoodTruckImage(MultipartFile mainFilePath, List<MultipartFile> subFilePaths) {
        this.mainFilePath = mainFilePath.getName();
        for (MultipartFile subFile : subFilePaths)
            this.subFilePaths.add(subFile.getName());
    }

    public void modifyMainFilePath(String filePath, String mainFilePath, MultipartFile mainFile) throws IOException {
        new File(filePath + mainFilePath).delete();
        this.mainFilePath = mainFilePath;
        mainFile.transferTo(new File(filePath + mainFilePath));
    }

    public void modifySubFilePaths(List<String> subFilePath) {
        for (String subFile : this.subFilePaths) {
            new File(subFile).delete();
        }
        this.subFilePaths = subFilePath;
    }

}
