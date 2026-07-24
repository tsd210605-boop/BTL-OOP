package com.example.workreport.dto;

import java.util.List;

public class BaoCaoSinhVienDTO {
    private Double diemTrungBinhHocPhan;
    private Integer rankHienTai;
    private Integer tyLeHoanThanh;
    private Integer soBaiTreDeadline;
    private List<TopStudent> bxhLop;
    private List<ChartData> analyticsChartData;

    public BaoCaoSinhVienDTO(Double diemTrungBinhHocPhan, Integer rankHienTai, Integer tyLeHoanThanh,
                             Integer soBaiTreDeadline, List<TopStudent> bxhLop, List<ChartData> analyticsChartData) {
        this.diemTrungBinhHocPhan = diemTrungBinhHocPhan;
        this.rankHienTai = rankHienTai;
        this.tyLeHoanThanh = tyLeHoanThanh;
        this.soBaiTreDeadline = soBaiTreDeadline;
        this.bxhLop = bxhLop;
        this.analyticsChartData = analyticsChartData;
    }

    // Getters và Setters của Class cha
    public Double getDiemTrungBinhHocPhan() { return diemTrungBinhHocPhan; }
    public void setDiemTrungBinhHocPhan(Double diemTrungBinhHocPhan) { this.diemTrungBinhHocPhan = diemTrungBinhHocPhan; }
    public Integer getRankHienTai() { return rankHienTai; }
    public void setRankHienTai(Integer rankHienTai) { this.rankHienTai = rankHienTai; }
    public Integer getTyLeHoanThanh() { return tyLeHoanThanh; }
    public void setTyLeHoanThanh(Integer tyLeHoanThanh) { this.tyLeHoanThanh = tyLeHoanThanh; }
    public Integer getSoBaiTreDeadline() { return soBaiTreDeadline; }
    public void setSoBaiTreDeadline(Integer soBaiTreDeadline) { this.soBaiTreDeadline = soBaiTreDeadline; }
    public List<TopStudent> getBxhLop() { return bxhLop; }
    public void setBxhLop(List<TopStudent> bxhLop) { this.bxhLop = bxhLop; }
    public List<ChartData> getAnalyticsChartData() { return analyticsChartData; }
    public void setAnalyticsChartData(List<ChartData> analyticsChartData) { this.analyticsChartData = analyticsChartData; }

    // =========================================================================
    // 🌟 ĐÃ SỬA: Khai báo Inner Class đúng chuẩn cấu trúc lồng nhau (Nested Classes)
    // =========================================================================
    public static class TopStudent {
        private String fullName;
        private String sinhVienID;
        private String lop;
        private Double diemTB;

        public TopStudent(String fullName, String sinhVienID, String lop, Double diemTB) {
            this.fullName = fullName;
            this.sinhVienID = sinhVienID;
            this.lop = lop;
            this.diemTB = diemTB;
        }
        public String getFullName() { return fullName; }
        public String getSinhVienID() { return sinhVienID; }
        public String getLop() { return lop; }
        public Double getDiemTB() { return diemTB; }
    }

    public static class ChartData {
        private String tenBaiTap;
        private Double diemCaNhan;
        private Double diemTrungBinhLop;

        public ChartData(String tenBaiTap, Double diemCaNhan, Double diemTrungBinhLop) {
            this.tenBaiTap = tenBaiTap;
            this.diemCaNhan = diemCaNhan;
            this.diemTrungBinhLop = diemTrungBinhLop;
        }
        public String getTenBaiTap() { return tenBaiTap; }
        public Double getDiemCaNhan() { return diemCaNhan; }
        public Double getDiemTrungBinhLop() { return diemTrungBinhLop; }
    }
}