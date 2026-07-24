package com.example.workreport.dto;

public class BaiTapDeadlineDTO {
    private String baiTapID;
    private String tenBaiTap;
    private String deadline;  
    private String deadlineFmt;
    private String loai;
    private String monHoc;
    private String lopID;
    private String tenLop;
    private String sinhVienID;
    private String trangThai;

    public BaiTapDeadlineDTO() {}

    // Constructor đầy đủ cập nhật theo kiểu dữ liệu mới
    public BaiTapDeadlineDTO(String baiTapID, String tenBaiTap, String deadline, String deadlineFmt, String loai,
                             String monHoc, String lopID, String tenLop, String sinhVienID, String trangThai) {
        this.baiTapID = baiTapID;
        this.tenBaiTap = tenBaiTap;
        this.deadline = deadline;
        this.deadlineFmt = deadlineFmt;
        this.loai = loai;
        this.monHoc = monHoc;
        this.lopID = lopID;
        this.tenLop = tenLop;
        this.sinhVienID = sinhVienID;
        this.trangThai = trangThai;
    }

    public BaiTapDeadlineDTO(String baiTapID, String tenBaiTap, String monHoc, String loai, String deadline, String trangThai) {
        this.baiTapID = baiTapID;
        this.tenBaiTap = tenBaiTap;
        this.monHoc = monHoc;
        this.loai = loai;
        this.deadline = deadline;
        this.trangThai = trangThai;
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getBaiTapID() { return baiTapID; }
    public void setBaiTapID(String baiTapID) { this.baiTapID = baiTapID; }

    public String getTenBaiTap() { return tenBaiTap; }
    public void setTenBaiTap(String tenBaiTap) { this.tenBaiTap = tenBaiTap; }

    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }

    public String getDeadlineFmt() { return deadlineFmt; }
    public void setDeadlineFmt(String deadlineFmt) { this.deadlineFmt = deadlineFmt; }

    public String getLoai() { return loai; }
    public void setLoai(String loai) { this.loai = loai; }

    public String getMonHoc() { return monHoc; }
    public void setMonHoc(String monHoc) { this.monHoc = monHoc; }

    public String getLopID() { return lopID; }
    public void setLopID(String lopID) { this.lopID = lopID; }

    public String getTenLop() { return tenLop; }
    public void setTenLop(String tenLop) { this.tenLop = tenLop; }

    public String getSinhVienID() { return sinhVienID; }
    public void setSinhVienID(String sinhVienID) { this.sinhVienID = sinhVienID; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}