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
    @ManyToOne
    @JoinColumn(name = "idPost")
    private Post idPost;
    @OneToOne
    @JoinColumn(name = "idUser")
    private User idUser;
    @OneToOne
    @JoinColumn(name = "idComments")
    private Comments idComments;
    private String text;
    private int likes;
    private Date date;
    private Time time;
}