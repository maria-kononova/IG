package com.example.ig.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="\"Comments\"")
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long idPost;
    private long idUser;
    private long idComments;
    private String text;
    private int likes;
    private Date date;

    public Comments(long idPost, long idUser, long idComments, String text, int likes, Date date) {
        this.idPost = idPost;
        this.idUser = idUser;
        this.idComments = idComments;
        this.text = text;
        this.likes = likes;
        this.date = date;
    }
}