package com.example.workreport.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "PHAN_HOI")
public class PhanHoi {

    @Id
    @Column(name = "PhanHoiID")
    private String phanHoiID;

    @Column(name = "NoiDung")
    private String noiDung;

    @Column(name = "NgayPhanHoi")
    private LocalDate ngayPhanHoi;

    @ManyToOne
    @JoinColumn(name = "BaoCaoID")
    private BaoCao baoCao;

    @ManyToOne
    @JoinColumn(name = "GiangVienID")
    private GiangVien giangVien;

    public String getPhanHoiID() {
        return phanHoiID;
    }
    public void setPhanHoiID(String phanHoiID) {
        this.phanHoiID = phanHoiID;
    }
    public String getNoiDung() {
        return noiDung;
    }
    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }
    public LocalDate getNgayPhanHoi() {
        return ngayPhanHoi;
    }
    public void setNgayPhanHoi(LocalDate ngayPhanHoi) {
        this.ngayPhanHoi = ngayPhanHoi;
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
