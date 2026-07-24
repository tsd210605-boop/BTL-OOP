package com.example.workreport.dto;

public class ThemMonHocRequest {
    private String monHocID;
    private String tenMonHoc;
    private String moTa;
    private Integer soTinChi;
    public String getMonHocID() {
        return monHocID;
    }
    public void setMonHocID(String monHocID) {this.monHocID = monHocID;}
    public String getTenMonHoc() {
        return tenMonHoc;
    }
    public void setTenMonHoc(String tenMonHoc) {this.tenMonHoc = tenMonHoc;}
    public String getMoTa() {
        return moTa;
    }
    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }
    public Integer getSoTinChi() {
        return soTinChi;
    }
    public void setSoTinChi(Integer soTinChi) {this.soTinChi = soTinChi;}
}
