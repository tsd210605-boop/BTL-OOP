package com.example.workreport.dto;

public class ThemSinhVienRequest {
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String khoa;
    private String lop;

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
    public String getFullName() {return fullName;}
    public void setFullName(String fullName) {this.fullName = fullName;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public String getKhoa() {return khoa;}
    public void setKhoa(String khoa) {this.khoa = khoa;}
    public String getLop() {return lop;}
    public void setLop(String lop) {this.lop = lop;}
}

