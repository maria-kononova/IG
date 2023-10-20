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
@Table(name="\"Post\"")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    @JoinColumn(name = "idGroup")
    private Group idGroup;
    private int type;
    private String description;
    private String avtar;
    private String banner;
    private Date datePublication;
    private Time timePublication;
    private int likes;
    private int views;
}
