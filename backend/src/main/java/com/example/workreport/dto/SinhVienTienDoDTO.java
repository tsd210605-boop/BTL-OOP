package com.example.workreport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SinhVienTienDoDTO {
    private String sinhVienID;
    private String fullName;
    private String lop;
    private String lopHocPhanID;
    private Long tongBaiTap;
    private Long hoanThanh; // Số bài đã nộp (Da nop)
    private Long dangLam;   // Số bài chưa nộp nhưng bài tập đang hoạt động
    private Long quaHan;    // Số bài chưa nộp và bài tập đã quá hạn
    private Double diemTrungBinh;
    private Long soNhomThamGia;
}