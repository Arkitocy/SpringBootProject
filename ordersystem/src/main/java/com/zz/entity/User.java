package com.zz.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
public class User implements Serializable {
    public static Long serialVersionUID = 12122321l;
    @Id
    @Column(length = 100)
    private String id;
    private String username;
    private String password;
    private String email;
    private String headimgid;
    private String invitecode;
    private String beinvitedcode;
    private String role;

    public User(String id, String username, String password, String email, String headimgid) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.headimgid = headimgid;
    }


    public User() {

    }

}
