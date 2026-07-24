package com.example.workreport.dto;

import java.time.LocalDateTime;

public class TaoBaiTapRequest {
    private String tenBaiTap;
    private String moTa;
    private LocalDateTime deadline;
    private Float diemToiDa;
    private String loai;
    private String lopID;

    public String getTenBaiTap() {return tenBaiTap;}
    public void setTenBaiTap(String tenBaiTap) {this.tenBaiTap = tenBaiTap;}
    public String getMoTa() {return moTa;}
    public void setMoTa(String moTa) {this.moTa = moTa;}
    public LocalDateTime getDeadline() {return deadline;}
    public void setDeadline(LocalDateTime deadline) {this.deadline = deadline;}
    public Float getDiemToiDa() {return diemToiDa;}
    public void setDiemToiDa(Float diemToiDa) {this.diemToiDa = diemToiDa;}
    public String getLoai() {return loai;}
    public void setLoai(String loai) {this.loai = loai;}
    public String getLopID() {return lopID;}
    public void setLopID(String lopID) {this.lopID = lopID;}
}

