package com.festival.domain.program.model;


import jakarta.persistence.*;

@Entity
public class Program {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "sub_title", nullable = false)
    private String subTitle;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "latitude", nullable = false) // 위도
    private float latitude;

    @Column(name = "longitude", nullable = false) // 경도
    private float longitude;

    @Column(name = "status", nullable = false)
    private ItemStatus status;

    @Column(name = "type", nullable = false)
    private ItemType type;

    public enum ItemStatus {
        OPERATE("운영중"), TERMINATE("종료");

        final private String itemStatus;

        ItemStatus(String status) {
            this.itemStatus = status;
        }

        public String getItemStatus() {
            return itemStatus;
        }
    }

    public enum ItemType {
        PROGRAM, GAME
    }
}
