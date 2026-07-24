package com.example.workreport.service; // Đảm bảo đúng dòng package của bạn

import com.example.workreport.dto.SinhVienTienDoDTO;
import com.example.workreport.dto.SinhVienBaiTapDTO; // 🌟 THÊM CHÍNH XÁC DÒNG IMPORT NÀY ĐỂ HẾT LỖI
import com.example.workreport.dto.OverviewDTO;
import com.example.workreport.dto.SinhVienProfileDTO;

import java.util.List;

public interface SinhVienService {

    // Hàm lấy danh sách tiến độ (màn hình ngoài)
    List<SinhVienTienDoDTO> getTienDoSinhVien(String giangVienID);

    // Đổi từ nhận giangVienID sang nhận lopID chuẩn theo luồng mới
    List<SinhVienBaiTapDTO> getChiTietBaiTapTheoLop(String lopID, String sinhVienID);

    OverviewDTO getOverviewDataByLop(String lopID);
    SinhVienProfileDTO getProfileByUserID(String userID);

    void updateProfile(String userID, SinhVienProfileDTO dto);
}