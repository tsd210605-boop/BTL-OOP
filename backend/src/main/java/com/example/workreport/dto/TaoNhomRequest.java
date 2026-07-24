package com.example.workreport.dto;

import java.util.List;

public class TaoNhomRequest {
    private String tenNhom;
    private String baiTapID;
    private String truongNhom;
    private List<String> thanhVien;

    // Getters and Setters
    public String getTenNhom() {
        return tenNhom;
    }

    public void setTenNhom(String tenNhom) {
        this.tenNhom = tenNhom;
    }

    public String getBaiTapID() {
        return baiTapID;
    }

    public void setBaiTapID(String baiTapID) {
        this.baiTapID = baiTapID;
    }

    public String getTruongNhom() {
        return truongNhom;
    }

    public void setTruongNhom(String truongNhom) {
        this.truongNhom = truongNhom;
    }

    public List<String> getThanhVien() {
        return thanhVien;
    }

    public void setThanhVien(List<String> thanhVien) {
        this.thanhVien = thanhVien;
    }
}
