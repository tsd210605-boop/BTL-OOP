package com.example.workreport.service;

import com.example.workreport.dto.BaiTapDeadlineDTO;
import com.example.workreport.dto.LichHocTuanDTO;

import java.util.List;

public interface LichHocService {
    List<BaiTapDeadlineDTO> getDeadlinesBySinhVien(String sinhVienID);
    List<BaiTapDeadlineDTO> getDeadlinesByMonth(String sinhVienID, int month, int year);
    List<LichHocTuanDTO> getLichHocTuan(String sinhVienID);
}