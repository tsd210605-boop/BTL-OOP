package com.example.workreport.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
@Table(name = "SINH_VIEN")
public class SinhVien {

    @Id
    @Column(name = "SinhVienID")
    private String sinhVienID;

    @Column(name = "Khoa")
    private String khoa;

    @Column(name = "Lop")
    private String lop;

    @Column(name = "GPA")
    private Float gpa;

    @Column(name = "SoDienThoai")
    private String phone;

    @Column(name = "GioiThieu", columnDefinition = "TEXT")
    private String bio;

    @Column(name = "KyNang", columnDefinition = "TEXT")
    private String skills;

    @OneToOne
    @JoinColumn(name = "UserID")
    private User user;

    @OneToMany(mappedBy = "sinhVien")
    @JsonIgnore
    private List<BaoCao> baoCaoList;

    @OneToMany(mappedBy = "sinhVien")
    @JsonIgnore
    private List<NhomMember> nhomList;

    @OneToMany(mappedBy = "truongNhom")
    @JsonIgnore
    private List<Nhom> nhomTruongList;

    @OneToMany(mappedBy = "sinhVien")
    @JsonIgnore
    private List<LopHocSinhVien> lopHocList;

    // =========================================================================
    // GETTERS & SETTERS CHUẨN CHỈ CỦA THỰC THỂ
    // =========================================================================
    public String getSinhVienID() { return sinhVienID; }
    public void setSinhVienID(String sinhVienID) { this.sinhVienID = sinhVienID; }

    public String getKhoa() { return khoa; }
    public void setKhoa(String khoa) { this.khoa = khoa; }

    public String getLop() { return lop; }
    public void setLop(String lop) { this.lop = lop; }

    public Float getGpa() { return gpa; }
    public void setGpa(Float gpa) { this.gpa = gpa; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<BaoCao> getBaoCaoList() { return baoCaoList; }
    public void setBaoCaoList(List<BaoCao> baoCaoList) { this.baoCaoList = baoCaoList; }

    public List<NhomMember> getNhomList() { return nhomList; }
    public void setNhomList(List<NhomMember> nhomList) { this.nhomList = nhomList; }

    public List<Nhom> getNhomTruongList() { return nhomTruongList; }
    public void setNhomTruongList(List<Nhom> nhomTruongList) { this.nhomTruongList = nhomTruongList; }

    public List<LopHocSinhVien> getLopHocList() { return lopHocList; }
    public void setLopHocList(List<LopHocSinhVien> lopHocList) { this.lopHocList = lopHocList; }
}