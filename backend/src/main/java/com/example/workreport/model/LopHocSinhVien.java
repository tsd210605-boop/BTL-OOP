package com.example.workreport.model;

import jakarta.persistence.*;

@Entity
@Table(name = "LOP_HOC_SINH_VIEN")
public class LopHocSinhVien {

    @EmbeddedId
    private LopHocSinhVienKey id;

    @ManyToOne
    @MapsId("lopID")
    @JoinColumn(name = "LopID")
    private LopHoc lopHoc;

    @ManyToOne
    @MapsId("sinhVienID")
    @JoinColumn(name = "SinhVienID")
    private SinhVien sinhVien;

    public LopHocSinhVien() {}

    public LopHocSinhVien(LopHoc lopHoc, SinhVien sinhVien) {
        this.lopHoc = lopHoc;
        this.sinhVien = sinhVien;
        this.id = new LopHocSinhVienKey(lopHoc.getLopID(), sinhVien.getSinhVienID());
    }

    public LopHocSinhVienKey getId() {
        return id;
    }
    public void setId(LopHocSinhVienKey id) {
        this.id = id;
    }
    public LopHoc getLopHoc() {
        return lopHoc;
    }
    public void setLopHoc(LopHoc lopHoc) {
        this.lopHoc = lopHoc;
    }
    public SinhVien getSinhVien() {
        return sinhVien;
    }
    public void setSinhVien(SinhVien sinhVien) {
        this.sinhVien = sinhVien;
    }
}