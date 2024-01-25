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
    private long idGroup;
    private int type;
    private String description;
    private String img;
    private Date datePublication;
    private int likes;
    private int views;

    public String getMonthAsString(){
        switch (datePublication.getMonth()){
            case(0): return "янв";
            case(1): return "фев";
            case(2): return "мар";
            case(3): return "апр";
            case(4): return "мая";
            case(5): return "июн";
            case(6): return "июл";
            case(7): return "авг";
            case(8): return "сен";
            case(9): return "окт";
            case(10): return "ноя";
            case(11): return "дек";
        }
        return "";
    }
}
