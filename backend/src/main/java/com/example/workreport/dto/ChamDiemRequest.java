package com.example.workreport.dto;

import lombok.Data;

@Data
public class ChamDiemRequest {
    private String baiTapID;
    private String sinhVienID;
    private Double diem;
    private String nhanXet;
}