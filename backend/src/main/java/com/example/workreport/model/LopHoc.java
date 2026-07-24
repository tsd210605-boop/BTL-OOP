package com.example.workreport.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "LOP_HOC")
public class LopHoc {

    @Id
    @Column(name = "LopID")
    private String lopID;

    @Column(name = "TenLop")
    private String tenLop;

    @ManyToOne
    @JoinColumn(name = "MonHocID")
    private MonHoc monHoc;

    @ManyToOne
    @JoinColumn(name = "GiangVienID")
    private GiangVien giangVien;

    @ManyToOne
    @JoinColumn(name = "HocKyID")
    private HocKy hocKy;

    @OneToMany(mappedBy = "lopHoc")
    private List<BaiTap> baiTapList;

    @OneToMany(mappedBy = "lopHoc")
    private List<LopHocSinhVien> sinhVienList;

    public LopHoc() {}

    public LopHoc(String lopID, String tenLop) {
        this.lopID = lopID;
        this.tenLop = tenLop;
    }

    public String getLopID() {
        return lopID;
    }
    public void setLopID(String lopID) {
        this.lopID = lopID;
    }
    public String getTenLop() {
        return tenLop;
    }
    public void setTenLop(String tenLop) {
        this.tenLop = tenLop;
    }
    public MonHoc getMonHoc() {
        return monHoc;
    }
    public void setMonHoc(MonHoc monHoc) {
        this.monHoc = monHoc;
    }
    public GiangVien getGiangVien() {
        return giangVien;
    }
    public void setGiangVien(GiangVien giangVien) {this.giangVien = giangVien;}
    public HocKy getHocKy() {
        return hocKy;
    }
    public void setHocKy(HocKy hocKy) {
        this.hocKy = hocKy;
    }
    public List<BaiTap> getBaiTapList() {
        return baiTapList;
    }
    public void setBaiTapList(List<BaiTap> baiTapList) {this.baiTapList = baiTapList;}
    public List<LopHocSinhVien> getSinhVienList() {
        return sinhVienList;
    }
    public void setSinhVienList(List<LopHocSinhVien> sinhVienList) {this.sinhVienList = sinhVienList;}
}
