package com.example.workreport.model;

import jakarta.persistence.*;

@Entity
@Table(name = "NHOM_MEMBER")
public class NhomMember {

    @EmbeddedId
    private NhomMemberKey id;

    @ManyToOne
    @MapsId("nhomID")
    @JoinColumn(name = "NhomID")
    private Nhom nhom;

    @ManyToOne
    @MapsId("sinhVienID")
    @JoinColumn(name = "SinhVienID")
    private SinhVien sinhVien;

    public NhomMember() {}

    public NhomMemberKey getId() {
        return id;
    }
    public void setId(NhomMemberKey id) {
        this.id = id;
    }
    public Nhom getNhom() {
        return nhom;
    }
    public void setNhom(Nhom nhom) {
        this.nhom = nhom;
    }
    public SinhVien getSinhVien() {
        return sinhVien;
    }
    public void setSinhVien(SinhVien sinhVien) {
        this.sinhVien = sinhVien;
    }
}
