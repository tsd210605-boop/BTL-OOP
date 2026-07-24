package com.example.workreport.dto;

public class BaiTapChiTietDTO {
    private String sinhVienID;
    private String fullName;
    private String lop;
    private String tenNhom;
    private String trangThai;
    private String trangThaiCham;
    private Double diem;
    private String fileBaoCao;
    private String ngayNop;
    private String tenBaiTap;
    private String moTa;
    private String nhanXet;

    public BaiTapChiTietDTO(String sinhVienID, String fullName, String lop, String tenNhom,
                            String trangThai, String trangThaiCham, Double diem,
                            String fileBaoCao, String ngayNop, String tenBaiTap, String moTa, String nhanXet) {
        this.sinhVienID = sinhVienID;
        this.fullName = fullName;
        this.lop = lop;
        this.tenNhom = tenNhom;
        this.trangThai = trangThai;
        this.trangThaiCham = trangThaiCham;
        this.diem = diem;
        this.fileBaoCao = fileBaoCao;
        this.ngayNop = ngayNop;
        this.tenBaiTap = tenBaiTap;
        this.moTa = moTa;
        this.nhanXet = nhanXet;
    }

    public String getSinhVienID() {return sinhVienID;}
    public String getFullName() {return fullName;}
    public String getLop() {return lop;}
    public String getTenNhom() {return tenNhom;}
    public String getTrangThai() {return trangThai;}
    public String getTrangThaiCham() {return trangThaiCham;}
    public Double getDiem() {return diem;}
    public String getFileBaoCao() {return fileBaoCao;}
    public String getNgayNop() {return ngayNop;}
    public String getTenBaiTap() {return tenBaiTap;}
    public String getMoTa() {return moTa;}
    public String getNhanXet() {return nhanXet;}
    public void setNhanXet(String nhanXet) {this.nhanXet = nhanXet;}
}
