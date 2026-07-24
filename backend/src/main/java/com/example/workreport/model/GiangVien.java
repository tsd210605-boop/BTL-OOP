package com.example.workreport.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
@Table(name = "GIANG_VIEN")
public class GiangVien {

    @Id
    @Column(name = "GiangVienID")
    private String giangVienID;

    @Column(name = "HocVi")
    private String hocVi;

    @Column(name = "BoMon")
    private String boMon;

    @Column(name = "SoDienThoai")
    private String soDienThoai;

    @Column(name = "Khoa")
    private String khoa;

    @Column(name = "PhongLamViec")
    private String phongLamViec;

    @Column(name = "GioTiepSinhVien")
    private String gioTiepSinhVien;

    @Column(name = "GioiThieu", columnDefinition = "TEXT")
    private String gioiThieu;

    @Column(name = "ChuyenMon", columnDefinition = "TEXT")
    private String chuyenMon;

    @OneToOne
    @JoinColumn(name = "UserID")
    private User user;

    @OneToMany(mappedBy = "giangVien")
    @JsonIgnore
    private List<LopHoc> lopHocList;

    @OneToMany(mappedBy = "giangVien")
    @JsonIgnore
    private List<DanhGia> danhGiaList;

    @OneToMany(mappedBy = "giangVien")
    @JsonIgnore
    private List<PhanHoi> phanHoiList;

    public String getGiangVienID() {
        return giangVienID;
    }
    public void setGiangVienID(String giangVienID) {
        this.giangVienID = giangVienID;
    }
    public String getHocVi() {
        return hocVi;
    }
    public void setHocVi(String hocVi) {
        this.hocVi = hocVi;
    }
    public String getBoMon() {
        return boMon;
    }
    public void setBoMon(String boMon) {
        this.boMon = boMon;
    }
    public String getSoDienThoai() {
        return soDienThoai;
    }
    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }
    public String getKhoa() {
        return khoa;
    }
    public void setKhoa(String khoa) {
        this.khoa = khoa;
    }
    public String getPhongLamViec() {
        return phongLamViec;
    }
    public void setPhongLamViec(String phongLamViec) {
        this.phongLamViec = phongLamViec;
    }
    public String getGioTiepSinhVien() {
        return gioTiepSinhVien;
    }
    public void setGioTiepSinhVien(String gioTiepSinhVien) {
        this.gioTiepSinhVien = gioTiepSinhVien;
    }
    public String getGioiThieu() {
        return gioiThieu;
    }
    public void setGioiThieu(String gioiThieu) {
        this.gioiThieu = gioiThieu;
    }
    public String getChuyenMon() {
        return chuyenMon;
    }
    public void setChuyenMon(String chuyenMon) {
        this.chuyenMon = chuyenMon;
    }
    public User getUser() {return user;}
    public void setUser(User user) {
        this.user = user;
    }
    public List<LopHoc> getLopHocList() {
        return lopHocList;
    }
    public void setLopHocList(List<LopHoc> lopHocList) {
        this.lopHocList = lopHocList;
    }
    public List<DanhGia> getDanhGiaList() {
        return danhGiaList;
    }
    public void setDanhGiaList(List<DanhGia> danhGiaList) {
        this.danhGiaList = danhGiaList;
    }
    public List<PhanHoi> getPhanHoiList() {
        return phanHoiList;
    }
    public void setPhanHoiList(List<PhanHoi> phanHoiList) {
        this.phanHoiList = phanHoiList;
    }
}
