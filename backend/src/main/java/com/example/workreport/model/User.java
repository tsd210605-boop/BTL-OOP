package com.example.workreport.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "USERS")
public class User {

    @Id
    @Column(name = "UserID")
    private String userID;

    @Column(name = "Username", unique = true)
    private String username;

    @Column(name = "Password")
    @JsonIgnore
    private String password;

    @Column(name = "Fullname")
    private String fullname;

    @Column(name = "Email", unique = true)
    private String email;

    @Column(name = "Role")
    private String role;

    @Column(name = "Avatar")
    private String avatar;

    @Column(name = "SoDienThoai")
    private String phone;

    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private SinhVien sinhVien;

    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private GiangVien giangVien;

    public User() {}

    // =========================================================================
    // GETTERS & SETTERS (Đầy đủ không thiếu hàm nào)
    // =========================================================================
    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
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
    public String getFullname() {
        return fullname;
    }
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public SinhVien getSinhVien() {
        return sinhVien;
    }
    public void setSinhVien(SinhVien sinhVien) {
        this.sinhVien = sinhVien;
    }
    public GiangVien getGiangVien() {
        return giangVien;
    }
    public void setGiangVien(GiangVien giangVien) {
        this.giangVien = giangVien;
    }
}