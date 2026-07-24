package com.example.workreport.dto;

public class LoginResponse {
    private String userID;
    private String fullName;
    private String role;
    private String sinhVienID;
    private String giangVienID;
    public LoginResponse() {}

    public LoginResponse(String userID, String fullName, String role, String sinhVienID, String giangVienID) {
        this.userID = userID;
        this.fullName = fullName;
        this.role = role;
        this.sinhVienID = sinhVienID;
        this.giangVienID = giangVienID;
    }

    public String getUserID() {return userID;}
    public void setUserID(String userID) {this.userID = userID;}
    public String getFullName() {return fullName;}
    public void setFullName(String fullName) {this.fullName = fullName;}
    public String getRole() {return role;}
    public void setRole(String role) {this.role = role;}
    public String getSinhVienID() {return sinhVienID;}
    public void setSinhVienID(String sinhVienID) {this.sinhVienID = sinhVienID;}
    public String getGiangVienID() {return giangVienID;}
    public void setGiangVienID(String giangVienID) {this.giangVienID = giangVienID;}
}

