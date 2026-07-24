package com.example.workreport.service;

import com.example.workreport.dto.ThongKeDTOs.*;

public interface ThongKeService {
    TongQuanDTO getTongQuan(String sinhVienID, String hocKy, String monHocID);
    TienDoNopBaiDTO getTienDoNopBai(String sinhVienID, String hocKy, String monHocID);
    DiemTrungBinhDTO getDiemTrungBinh(String sinhVienID, String hocKy, String monHocID);
    TrangThaiBaiNopDTO getTrangThaiBaiNop(String sinhVienID, String hocKy, String monHocID);
    ChiTietBaiTapDTO getChiTietBaiTap(String sinhVienID, String hocKy, String monHocID);
    byte[] xuatExcel(String sinhVienID, String hocKy, String monHocID) throws Exception;
}

