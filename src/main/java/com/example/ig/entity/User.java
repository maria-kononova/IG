package com.example.ig.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="\"User\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotBlank(message = "login is required")
    private String login; //or UID?
    @NotBlank(message = "email is required")
    private String email;
    @NotBlank(message = "password is required")
    private String password;
    @NotBlank(message = "salt is required")
    private String salt;
    @Column(columnDefinition="TEXT")
    private String img;

    public User(String login, String email, String password){
        this.email = email;
        this.login = login;
        this.salt = generateSalt();
        this.password = getHash(password, this.salt);
        this.setImg("https://freepngimg.com/thumb/playstation_3/88138-playstation-photography-carnivoran-avatar-monochrome-free-download-png-hq.png");
    }

    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[15];
        random.nextBytes(salt);
        return new String(salt, StandardCharsets.UTF_8);
    }

    public void updatePassword(String newPassword){
        this.salt = generateSalt();
        this.password = getHash(newPassword, this.salt);
    }

    public boolean checkPassword(String passwordForCheck){
        return password.equals(getHash(passwordForCheck, salt));
    }

    private String getHash(String password, String salt) {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update((password + salt).getBytes());
            byte[] bytes = md.digest();

            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
