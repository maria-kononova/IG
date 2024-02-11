package com.example.ig.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@Table(name="\"Post\"")
public class Post {
    @Id
    private long id;
    private long idGroup;
    private int type;
    private String description;
    @ElementCollection
    private List<String> img;
    private Date datePublication;
    private int likes;
    private int views;
    private int comments;

    public Post(long id, long idGroup, int type, String description, List<String> img, Date datePublication, int likes, int views, int comments) {
        this.id = id;
        this.idGroup = idGroup;
        this.type = type;
        this.description = description;
        this.img = img;
        this.datePublication = datePublication;
        this.likes = likes;
        this.views = views;
        this.comments = comments;
    }
}
