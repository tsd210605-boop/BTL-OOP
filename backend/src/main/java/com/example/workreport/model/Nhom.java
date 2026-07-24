package com.example.workreport.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "NHOM")
public class Nhom {

    @Id
    @Column(name = "NhomID")
    private String nhomID;

    @Column(name = "TenNhom")
    private String tenNhom;

    @ManyToOne
    @JoinColumn(name = "BaiTapID")
    private BaiTap baiTap;

    @ManyToOne
    @JoinColumn(name = "TruongNhom")
    private SinhVien truongNhom;

    @OneToMany(mappedBy = "nhom")
    private List<NhomMember> members;

    @OneToMany(mappedBy = "nhom")
    private List<BaoCao> baoCaoList;

    @OneToMany(mappedBy = "nhom")
    private List<TienDo> tienDoList;

    public String getNhomID() {
        return nhomID;
    }
    public void setNhomID(String nhomID) {
        this.nhomID = nhomID;
    }
    public String getTenNhom() {
        return tenNhom;
    }
    public void setTenNhom(String tenNhom) {
        this.tenNhom = tenNhom;
    }
    public BaiTap getBaiTap() {
        return baiTap;
    }
    public void setBaiTap(BaiTap baiTap) {
        this.baiTap = baiTap;
    }
    public SinhVien getTruongNhom() {
        return truongNhom;
    }
    public void setTruongNhom(SinhVien truongNhom) {
        this.truongNhom = truongNhom;
    }
    public List<NhomMember> getMembers() {
        return members;
    }
    public void setMembers(List<NhomMember> members) {
        this.members = members;
    }
    public List<BaoCao> getBaoCaoList() {
        return baoCaoList;
    }
    public void setBaoCaoList(List<BaoCao> baoCaoList) {
        this.baoCaoList = baoCaoList;
    }
    public List<TienDo> getTienDoList() {
        return tienDoList;
    }
    public void setTienDoList(List<TienDo> tienDoList) {
        this.tienDoList = tienDoList;
    }
}
