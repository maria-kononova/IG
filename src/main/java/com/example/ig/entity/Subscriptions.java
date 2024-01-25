package com.example.ig.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Data
@Entity
@Table(name="\"Subscriptions\"")
@AllArgsConstructor
@NoArgsConstructor
public class Subscriptions implements Serializable {
    private static final long serialVersionUID = -909206262878526790L;
    @EmbeddedId
    private GroupUser groupUserId;
}
