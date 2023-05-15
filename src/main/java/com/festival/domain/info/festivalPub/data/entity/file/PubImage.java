package com.festival.domain.info.festivalPub.data.entity.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.festival.domain.info.festivalPub.data.entity.pub.Pub;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PubImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "main_file_name", nullable = false)
    private String mainFileName;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "pub_sub_image", joinColumns = @JoinColumn(name = "pub_image_id"))
    @Column(name = "file_name")
    private List<String> subFileNames = new ArrayList<>();


    @OneToOne(mappedBy = "pubImage", fetch = FetchType.LAZY)
    private Pub pub;

    public PubImage(Pub pub) {
        this.pub = pub;
    }

    public void connectFileNames(String mainFileName, List<String> subFileNames) {
        this.mainFileName = mainFileName;
        this.subFileNames = subFileNames;
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
