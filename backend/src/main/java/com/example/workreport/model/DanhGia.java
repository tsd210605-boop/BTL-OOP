package com.example.workreport.model;

import jakarta.persistence.*;

@Entity
@Table(name = "DANH_GIA")
public class DanhGia {

    @Id
    @Column(name = "DanhGiaID")
    private String danhGiaID;

    @Column(name = "Diem")
    private Float diem;

    @Column(name = "NhanXet")
    private String nhanXet;

    @ManyToOne
    @JoinColumn(name = "BaoCaoID")
    private BaoCao baoCao;

    @ManyToOne
    @JoinColumn(name = "GiangVienID")
    private GiangVien giangVien;

    public String getDanhGiaID() {
        return danhGiaID;
    }
    public void setDanhGiaID(String danhGiaID) {
        this.danhGiaID = danhGiaID;
    }
    public Float getDiem() {
        return diem;
    }
    public void setDiem(Float diem) {
        this.diem = diem;
    }
    public String getNhanXet() {
        return nhanXet;
    }
    public void setNhanXet(String nhanXet) {
        this.nhanXet = nhanXet;
    }
    public BaoCao getBaoCao() {
        return baoCao;
    }
    public void setBaoCao(BaoCao baoCao) {
        this.baoCao = baoCao;
    }
    public GiangVien getGiangVien() {
        return giangVien;
    }
    public void setGiangVien(GiangVien giangVien) {
        this.giangVien = giangVien;
    }
}
