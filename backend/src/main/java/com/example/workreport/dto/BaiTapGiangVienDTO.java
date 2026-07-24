package com.example.workreport.dto;

public class BaiTapGiangVienDTO {
    private String baiTapID;
    private String tenBaiTap;
    private String moTa;
    private String trangThai;
    private String deadline;
    private String loai;
    private Integer diemToiDa;
    private Double diemTrungBinh;
    private Long daNop;
    private Long tongSinhVien;
    private Long daCham;
    private Long choCham;

    public BaiTapGiangVienDTO(String baiTapID, String tenBaiTap, String moTa, String trangThai, String deadline, String loai, Integer diemToiDa, Double diemTrungBinh, Long daNop, Long tongSinhVien, Long daCham, Long choCham) {
        this.baiTapID = baiTapID;
        this.tenBaiTap = tenBaiTap;
        this.moTa = moTa;
        this.trangThai = trangThai;
        this.deadline = deadline;
        this.loai = loai;
        this.diemToiDa = diemToiDa;
        this.diemTrungBinh = diemTrungBinh;
        this.daNop = daNop;
        this.tongSinhVien = tongSinhVien;
        this.daCham = daCham;
        this.choCham = choCham;
    }

    public String getBaiTapID() {return baiTapID;}
    public String getTenBaiTap() {return tenBaiTap;}
    public String getMoTa() {return moTa;}
    public String getTrangThai() {return trangThai;}
    public String getDeadline() {return deadline;}
    public String getLoai() {return loai;}
    public Integer getDiemToiDa() {return diemToiDa;}
    public Double getDiemTrungBinh() {return diemTrungBinh;}
    public Long getDaNop() {return daNop;}
    public Long getTongSinhVien() {return tongSinhVien;}
    public Long getDaCham() {return daCham;}
    public Long getChoCham() {return choCham;}
}
