package com.example.workreport.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "TIEN_DO")
public class TienDo {

    @Id
    @Column(name = "TienDoID", length = 50)
    private String tienDoID;

    @Column(name = "NgayCapNhat")
    private LocalDate ngayCapNhat;

    @Column(name = "PhanTramHoanThanh")
    private Integer phanTramHoanThanh;

    @Column(name = "TrangThai", length = 100)
    private String trangThai;

    @Column(name = "NoiDung")
    private String noiDung;

    @ManyToOne
    @JoinColumn(name = "BaiTapID")
    private BaiTap baiTap;

    @ManyToOne
    @JoinColumn(name = "NhomID")
    private Nhom nhom;

    public TienDo() {}

    public String getTienDoID() {
        return tienDoID;
    }
    public void setTienDoID(String tienDoID) {
        this.tienDoID = tienDoID;
    }
    public LocalDate getNgayCapNhat() {
        return ngayCapNhat;
    }
    public void setNgayCapNhat(LocalDate ngayCapNhat) {
        this.ngayCapNhat = ngayCapNhat;
    }
    public Integer getPhanTramHoanThanh() {
        return phanTramHoanThanh;
    }
    public void setPhanTramHoanThanh(Integer phanTramHoanThanh) {
        this.phanTramHoanThanh = phanTramHoanThanh;
    }
    public String getTrangThai() {
        return trangThai;
    }
    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
    public String getNoiDung() {
        return noiDung;
    }
    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }
    public BaiTap getBaiTap() {
        return baiTap;
    }
    public void setBaiTap(BaiTap baiTap) {
        this.baiTap = baiTap;
    }
    public Nhom getNhom() {
        return nhom;
    }
    public void setNhom(Nhom nhom) {
        this.nhom = nhom;
    }
}
