package com.zz.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    @Column(length = 100)
    private String id;
    private String username;
    private String password;
    private String email;
    private String headimgid;

    public User(String id, String username, String password, String email, String headimgid) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.headimgid = headimgid;
    }


    public User() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String type) {
        this.email = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHeadimgid() {
        return headimgid;
    }

    public void setHeadimgid(String headimgid) {
        this.headimgid = headimgid;
    }
}
