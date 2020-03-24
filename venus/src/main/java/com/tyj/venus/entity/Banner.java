package com.tyj.venus.entity;

        import lombok.Data;

        import javax.persistence.*;
        import java.util.Date;

@Data
@Entity
@Table(name = "t_banner")
public class Banner {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "title")
    private String title;

    @Column(name = "tag")
    private String tag;

}
