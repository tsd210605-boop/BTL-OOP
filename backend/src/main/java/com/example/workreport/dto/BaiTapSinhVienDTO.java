package com.example.workreport.dto;

public class BaiTapSinhVienDTO {
    private String baiTapID;
    private String tenBaiTap;
    private String moTa;
    private String deadline;
    private String trangThai;
    private String loai;
    private Double diemTB;
    private Long soNopBai;
    private Long tongSinhVien;
    private Long soChamBai;
    private String trangThaiNop;
    private String lopID; 
    private String nhanXet;
    private String fileBaoCao;

    public BaiTapSinhVienDTO(String baiTapID, String tenBaiTap, String moTa, String deadline, String trangThai, String loai, Double diemTB, Long soNopBai, Long tongSinhVien, Long soChamBai, String trangThaiNop, String lopID, String nhanXet, String fileBaoCao) {
        this.baiTapID = baiTapID;
        this.tenBaiTap = tenBaiTap;
        this.moTa = moTa;
        this.deadline = deadline;
        this.trangThai = trangThai;
        this.loai = loai;
        this.diemTB = diemTB != null ? diemTB : 0.0;
        this.soNopBai = soNopBai != null ? soNopBai : 0L;
        this.tongSinhVien = tongSinhVien != null ? tongSinhVien : 1L;
        this.soChamBai = soChamBai != null ? soChamBai : 0L;
        this.trangThaiNop = trangThaiNop != null ? trangThaiNop : "Chưa làm";
        this.lopID = lopID; 
        this.nhanXet = nhanXet != null ? nhanXet : "Giảng viên không để lại lời nhắn.";
        this.fileBaoCao = fileBaoCao != null ? fileBaoCao : "";
    }

    public String getLopID() {return lopID;}
    public String getNhanXet() {return nhanXet;}
    public String getFileBaoCao() {return fileBaoCao;}
    // Giữ nguyên các hàm Getters cũ của bạn ở dưới...
    public String getBaiTapID() { return baiTapID; }
    public String getTenBaiTap() { return tenBaiTap; }
    public String getMoTa() { return moTa; }
    public String getDeadline() { return deadline; }
    public String getTrangThai() { return trangThai; }
    public String getLoai() { return loai; }
    public Double getDiemTB() { return diemTB; }
    public Long getSoNopBai() { return soNopBai; }
    public Long getTongSinhVien() { return tongSinhVien; }
    public Long getSoChamBai() { return soChamBai; }
    public String getTrangThaiNop() { return trangThaiNop; }
}