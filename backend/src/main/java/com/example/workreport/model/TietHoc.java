package com.example.workreport.model;

import jakarta.persistence.*;

@Entity
@Table(name = "TIET_HOC")
public class TietHoc {

    @Id
    @Column(name = "TietHocID", length = 50)
    private String tietHocID;

    @Column(name = "LopID", length = 50)
    private String lopID;

    @Column(name = "ThuTrongTuan")
    private Integer thuTrongTuan;

    @Column(name = "TietBatDau")
    private Integer tietBatDau;

    //Số tiết (số slot kéo dài) 
    @Column(name = "SoTiet")
    private Integer soTiet;

    @Column(name = "PhongHoc", length = 50)
    private String phongHoc;

    // "Lý thuyết" / "Thực hành" / "Bài tập" */
    @Column(name = "LoaiBuoi", length = 50)
    private String loaiBuoi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LopID", insertable = false, updatable = false)
    private LopHoc lopHoc;

    public TietHoc() {}

    public TietHoc(String tietHocID, String lopID, Integer thuTrongTuan, Integer tietBatDau, Integer soTiet, String phongHoc, String loaiBuoi) {
        this.tietHocID    = tietHocID;
        this.lopID        = lopID;
        this.thuTrongTuan = thuTrongTuan;
        this.tietBatDau   = tietBatDau;
        this.soTiet       = soTiet;
        this.phongHoc     = phongHoc;
        this.loaiBuoi     = loaiBuoi;
    }

    public String  getTietHocID() { return tietHocID; }
    public void    setTietHocID(String v) { this.tietHocID = v; }
    public String  getLopID() { return lopID; }
    public void    setLopID(String v) { this.lopID = v; }

    public Integer getThuTrongTuan()             { return thuTrongTuan; }
    public void    setThuTrongTuan(Integer v)    { this.thuTrongTuan = v; }

    public Integer getTietBatDau()               { return tietBatDau; }
    public void    setTietBatDau(Integer v)      { this.tietBatDau = v; }

    public Integer getSoTiet()                   { return soTiet; }
    public void    setSoTiet(Integer v)          { this.soTiet = v; }

    public String  getPhongHoc()                 { return phongHoc; }
    public void    setPhongHoc(String v)         { this.phongHoc = v; }

    public String  getLoaiBuoi()                 { return loaiBuoi; }
    public void    setLoaiBuoi(String v)         { this.loaiBuoi = v; }

    public LopHoc  getLopHoc()                   { return lopHoc; }
    public void    setLopHoc(LopHoc v)           { this.lopHoc = v; }
}