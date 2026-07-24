package com.example.workreport.dto;

public class LichHocTuanDTO {
    private String tietHocID;
    private String lopID;
    private String tenLop;
    private String tenMonHoc;
    private String maMonHoc;
    private String tenGiangVien;
    private String phongHoc;
    private String loaiBuoi;
    private String ghiChu;
    private String colorClass;
    private int dayIndex;
    private int startSlot;
    private int spanSlots;

    public LichHocTuanDTO() {}

    public LichHocTuanDTO(String tietHocID, String lopID, String tenLop, String tenMonHoc, String maMonHoc,
                          String tenGiangVien, String phongHoc, String loaiBuoi, String ghiChu, String colorClass,
                          int dayIndex, int startSlot, int spanSlots) {
        this.tietHocID = tietHocID;
        this.lopID = lopID;
        this.tenLop = tenLop;
        this.tenMonHoc = tenMonHoc;
        this.maMonHoc = maMonHoc;
        this.tenGiangVien = tenGiangVien;
        this.phongHoc = phongHoc;
        this.loaiBuoi = loaiBuoi;
        this.ghiChu = ghiChu;
        this.colorClass = colorClass;
        this.dayIndex = dayIndex;
        this.startSlot = startSlot;
        this.spanSlots = spanSlots;
    }

    public String getTietHocID() { return tietHocID; }
    public void setTietHocID(String tietHocID) { this.tietHocID = tietHocID; }

    public String getLopID() { return lopID; }
    public void setLopID(String lopID) { this.lopID = lopID; }

    public String getTenLop() { return tenLop; }
    public void setTenLop(String tenLop) { this.tenLop = tenLop; }

    public String getTenMonHoc() { return tenMonHoc; }
    public void setTenMonHoc(String tenMonHoc) { this.tenMonHoc = tenMonHoc; }

    public String getMaMonHoc() { return maMonHoc; }
    public void setMaMonHoc(String maMonHoc) { this.maMonHoc = maMonHoc; }

    public String getTenGiangVien() { return tenGiangVien; }
    public void setTenGiangVien(String tenGiangVien) { this.tenGiangVien = tenGiangVien; }

    public String getPhongHoc() { return phongHoc; }
    public void setPhongHoc(String phongHoc) { this.phongHoc = phongHoc; }

    public String getLoaiBuoi() { return loaiBuoi; }
    public void setLoaiBuoi(String loaiBuoi) { this.loaiBuoi = loaiBuoi; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public String getColorClass() { return colorClass; }
    public void setColorClass(String colorClass) { this.colorClass = colorClass; }

    public int getDayIndex() { return dayIndex; }
    public void setDayIndex(int dayIndex) { this.dayIndex = dayIndex; }

    public int getStartSlot() { return startSlot; }
    public void setStartSlot(int startSlot) { this.startSlot = startSlot; }

    public int getSpanSlots() { return spanSlots; }
    public void setSpanSlots(int spanSlots) { this.spanSlots = spanSlots; }
}