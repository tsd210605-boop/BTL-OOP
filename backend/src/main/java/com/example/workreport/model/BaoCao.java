package com.example.workreport.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "BAO_CAO")
public class BaoCao {

    @Id
    @Column(name = "BaoCaoID", length = 50)
    private String baoCaoID;

    @Column(name = "NgayNop")
    private LocalDateTime ngayNop;

    @Column(name = "FileBaoCao", length = 255)
    private String fileBaoCao;

    @Column(name = "TrangThai", length = 100)
    private String trangThai;

    @ManyToOne
    @JoinColumn(name = "BaiTapID")
    private BaiTap baiTap;

    @ManyToOne
    @JoinColumn(name = "SinhVienID")
    private SinhVien sinhVien;

    @ManyToOne
    @JoinColumn(name = "NhomID")
    private Nhom nhom;

    @OneToMany(mappedBy = "baoCao")
    private List<DanhGia> danhGiaList;

    public BaoCao() {
    }

    public String getBaoCaoID() {
        return baoCaoID;
    }
    public void setBaoCaoID(String baoCaoID) {
        this.baoCaoID = baoCaoID;
    }
    public LocalDateTime getNgayNop() {
        return ngayNop;
    }
    public void setNgayNop(LocalDateTime ngayNop) {
        this.ngayNop = ngayNop;
    }
    public String getFileBaoCao() {
        return fileBaoCao;
    }
    public void setFileBaoCao(String fileBaoCao) {
        this.fileBaoCao = fileBaoCao;
    }
    public String getTrangThai() {
        return trangThai;
    }
    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
    public BaiTap getBaiTap() {
        return baiTap;
    }
    public void setBaiTap(BaiTap baiTap) {
        this.baiTap = baiTap;
    }
    public SinhVien getSinhVien() {
        return sinhVien;
    }
    public void setSinhVien(SinhVien sinhVien) {
        this.sinhVien = sinhVien;
    }
    public Nhom getNhom() {
        return nhom;
    }
    public void setNhom(Nhom nhom) {
        this.nhom = nhom;
    }
    public List<DanhGia> getDanhGiaList() {
        return danhGiaList;
    }
    public void setDanhGiaList(List<DanhGia> danhGiaList) {
        this.danhGiaList = danhGiaList;
    }
}
