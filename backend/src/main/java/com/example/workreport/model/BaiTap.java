package com.example.workreport.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "BAI_TAP")
public class BaiTap {

    @Id
    @Column(name = "BaiTapID", length = 50)
    private String baiTapID;

    @Column(name = "TenBaiTap", length = 100)
    private String tenBaiTap;

    @Column(name = "MoTa")
    private String moTa;

    @Column(name = "Deadline")
    private LocalDateTime deadline;

    @Column(name = "Loai", length = 50)
    private String loai;

    @Column(name = "DiemToiDa")
    private Float diemToiDa;

    @Column(name = "TrangThai")
    private String trangThai;

    @ManyToOne
    @JoinColumn(name = "LopID")
    private LopHoc lopHoc;

    @OneToMany(mappedBy = "baiTap")
    private List<Nhom> nhomList;

    @OneToMany(mappedBy = "baiTap")
    private List<BaoCao> baoCaoList;

    public BaiTap() {}

    public BaiTap(String baiTapID, String tenBaiTap, LocalDateTime deadline, String loai) {
        this.baiTapID = baiTapID;
        this.tenBaiTap = tenBaiTap;
        this.deadline = deadline;
        this.loai = loai;
    }

    public String getBaiTapID() {
        return baiTapID;
    }
    public void setBaiTapID(String baiTapID) {
        this.baiTapID = baiTapID;
    }
    public String getTenBaiTap() {
        return tenBaiTap;
    }
    public void setTenBaiTap(String tenBaiTap) {
        this.tenBaiTap = tenBaiTap;
    }
    public String getMoTa() {
        return moTa;
    }
    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }
    public LocalDateTime getDeadline() {
        return deadline;
    }
    public void setDeadline(LocalDateTime deadline) {this.deadline = deadline;}
    public String getLoai() {
        return loai;
    }
    public void setLoai(String loai) {
        this.loai = loai;
    }
    public Float getDiemToiDa() {
        return diemToiDa;
    }
    public void setDiemToiDa(Float diemToiDa) {
        this.diemToiDa = diemToiDa;
    }
    public String getTrangThai() {
        return trangThai;
    }
    public void setTrangThai(String trangThai) {this.trangThai = trangThai;}
    public LopHoc getLopHoc() {
        return lopHoc;
    }
    public void setLopHoc(LopHoc lopHoc) {
        this.lopHoc = lopHoc;
    }
    public List<Nhom> getNhomList() {
        return nhomList;
    }
    public void setNhomList(List<Nhom> nhomList) {this.nhomList = nhomList;}
    public List<BaoCao> getBaoCaoList() {
        return baoCaoList;
    }
    public void setBaoCaoList(List<BaoCao> baoCaoList) {this.baoCaoList = baoCaoList;}
}
