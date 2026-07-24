package com.example.workreport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SinhVienBaiTapDTO {
    private String baiTapID;
    private String tenBaiTap;
    private String moTa;
    private String trangThaiNop;   // "Chua nop", "Da nop", "Da cham"
    private String fileBaoCao;
    private String ngayNop;
    private String nhanXet;
    private Double diem;
}