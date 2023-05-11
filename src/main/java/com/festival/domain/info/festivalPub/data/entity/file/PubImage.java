package com.festival.domain.info.festivalPub.data.entity.file;

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
public class PubImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "main_file_path", nullable = false)
    private String mainFilePath;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "pub_sub_image", joinColumns = @JoinColumn(name = "pub_image_id"))
    @Column(name = "file_path")
    private List<String> subFilePaths = new ArrayList<>();

    @OneToOne(mappedBy = "pubImage", fetch = FetchType.LAZY)
    private Pub pub;

    public PubImage(String mainFilePath, Pub pub) {
        this.mainFilePath = mainFilePath;
        this.pub = pub;
    }

    public void saveSubFilePaths(List<String> subFilePath) {
        this.subFilePaths = subFilePath;
    }

    public void modifySubFilePaths(String filePath, List<String> subFilePath) {
        boolean delete = false;
        for (String subFile : this.subFilePaths) {
            File file = new File(filePath + subFile);
            delete = file.delete();
        }
        if (delete) {
            this.subFilePaths = subFilePath;
        }
    }

    public void modifyMainFilePath(String filePath, String mainFilePath, MultipartFile mainFile) throws IOException {
        boolean delete;
        File file = new File(filePath + mainFilePath);
        delete = file.delete();
        if (delete) {
            this.mainFilePath = mainFilePath;
            mainFile.transferTo(new File(filePath + mainFilePath));
        }
    }

    public void delete(String filePath) {
        for (String subFile : this.subFilePaths) {
            File file = new File(filePath + subFile);
            file.delete();
        }
        File file = new File(filePath + mainFilePath);
        file.delete();
    }
}
