package com.example.ig.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@Entity
@Table(name="\"Likes\"")
@AllArgsConstructor
@NoArgsConstructor
public class Likes implements Serializable {
    private static final long serialVersionUID = -909206262878526790L;
    @EmbeddedId
    private LikeUser postUserId;
}
