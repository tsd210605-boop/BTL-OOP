package com.example.workreport.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "TAI_LIEU")
public class TaiLieu {

    @Id
    @Column(name = "TaiLieuID")
    private String taiLieuID;

    @Column(name = "TenFile")
    private String tenFile;

    @Column(name = "DuongDanFile")
    private String duongDanFile;

    @Column(name = "LoaiFile")
    private String loaiFile;

    @Column(name = "NgayCapNhat")
    private LocalDate ngayCapNhat;

    @ManyToOne
    @JoinColumn(name = "BaiTapID")
    private BaiTap baiTap;

    public String getTaiLieuID() {
        return taiLieuID;
    }
    public void setTaiLieuID(String taiLieuID) {
        this.taiLieuID = taiLieuID;
    }
    public String getTenFile() {
        return tenFile;
    }
    public void setTenFile(String tenFile) {
        this.tenFile = tenFile;
    }
    public String getDuongDanFile() {
        return duongDanFile;
    }
    public void setDuongDanFile(String duongDanFile) {
        this.duongDanFile = duongDanFile;
    }
    public String getLoaiFile() {
        return loaiFile;
    }
    public void setLoaiFile(String loaiFile) {
        this.loaiFile = loaiFile;
    }
    public LocalDate getNgayCapNhat() {
        return ngayCapNhat;
    }
    public void setNgayCapNhat(LocalDate ngayCapNhat) {
        this.ngayCapNhat = ngayCapNhat;
    }
    public BaiTap getBaiTap() {
        return baiTap;
    }
    public void setBaiTap(BaiTap baiTap) {
        this.baiTap = baiTap;
    }
}
