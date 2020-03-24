package com.tyj.venus.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "t_news")
public class News {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "title")
    private String title;

    @Column(name = "content" ,columnDefinition ="TEXT")
    private String content;

    @Column(name = "description")
    private String description;

    @Column(name = "category")
    private String category;

    @Column(name = "promulgator")
    private String promulgator;

    @Column(name = "release_date")
    private Date releaseDate ;

    @Column(name = "comment")
    private String comment;

    @Column(name = "comment_count")
    private Integer commentCount;

    @Column(name = "created_at")
    private Date createdAt;


}
