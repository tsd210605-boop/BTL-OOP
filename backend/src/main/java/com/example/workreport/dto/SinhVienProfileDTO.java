package com.example.workreport.dto;

import lombok.Data;

@Data
public class SinhVienProfileDTO {
    private String fullName;
    private String sinhVienID;
    private String lop;
    private String khoa;
    private Float gpa;
    private String email;
    private String phone;
    private String bio;
    private String skills;
    private String avatar;

    public SinhVienProfileDTO() {}


    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getSinhVienID() { return sinhVienID; }
    public void setStringVienID(String sinhVienID) { this.sinhVienID = sinhVienID; }

    public String getLop() { return lop; }
    public void setLop(String lop) { this.lop = lop; }

    public String getKhoa() { return khoa; }
    public void setKhoa(String khoa) { this.khoa = khoa; }

    public Float getGpa() { return gpa; }
    public void setGpa(Float gpa) { this.gpa = gpa; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
}