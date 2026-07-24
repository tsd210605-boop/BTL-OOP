package com.example.workreport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LopReportDTO {
    private Double diemTBChung;
    private Long tongBaiTap;
    private Long tongSinhVien;
    private Integer tyLeHoanThanh;
    private List<TopStudent> topStudents;

    @Data
    @AllArgsConstructor
    public static class TopStudent {
        private int rank;
        private String sinhVienID;
        private String fullName;
        private String lopHanhChinh;
        private Double diemTB;
        private Long soBaiDaCham;
    }
    // Bổ sung vào cuối file LopReportDTO.java
    private List<ChartAssignmentData> chartData;

    @Data
    @AllArgsConstructor
    public static class ChartAssignmentData {
        private String tenBaiTap;
        private Long daNop;
        private Long tongCanThu;
        private Double diemTrungBinh;
    }
}