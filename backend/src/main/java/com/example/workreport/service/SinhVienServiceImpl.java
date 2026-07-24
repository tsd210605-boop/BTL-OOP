package com.example.workreport.service;

import com.example.workreport.dto.SinhVienTienDoDTO;
import com.example.workreport.dto.SinhVienBaiTapDTO;
import com.example.workreport.dto.OverviewDTO;
import com.example.workreport.dto.SinhVienProfileDTO;
import com.example.workreport.repository.SinhVienRepository;
import com.example.workreport.repository.UserRepository;
import com.example.workreport.repository.BaiTapRepository;
import com.example.workreport.model.SinhVien;
import com.example.workreport.model.User;
import com.example.workreport.exception.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SinhVienServiceImpl implements SinhVienService {

    @Autowired
    private SinhVienRepository sinhVienRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BaiTapRepository baiTapRepository;

    @Override
    public List<SinhVienTienDoDTO> getTienDoSinhVien(String giangVienID) {
        List<Object[]> rows = sinhVienRepository.findTienDoSinhVienByGiangVien(giangVienID);

        return rows.stream().map(row -> {
            String lopHocPhan = (row.length > 9 && row[9] != null) ? row[9].toString() : "Chưa rõ lớp HP";

            return new SinhVienTienDoDTO(
                    row[0] != null ? row[0].toString() : "",
                    row[1] != null ? row[1].toString() : "Chưa rõ họ tên",
                    row[2] != null ? row[2].toString() : "",
                    lopHocPhan,
                    row[3] != null ? ((Number) row[3]).longValue() : 0L,
                    row[4] != null ? ((Number) row[4]).longValue() : 0L,
                    row[5] != null ? ((Number) row[5]).longValue() : 0L,
                    row[6] != null ? ((Number) row[6]).longValue() : 0L,
                    row[7] != null ? ((Number) row[7]).doubleValue() : 0.0,
                    row[8] != null ? ((Number) row[8]).longValue() : 0L
            );
        }).toList();
    }

    @Override
    public List<SinhVienBaiTapDTO> getChiTietBaiTapTheoLop(String lopID, String sinhVienID) {
        List<Object[]> rows = baiTapRepository.findBaiTapByLopAndSinhVien(lopID, sinhVienID);

        return rows.stream().map(row -> new SinhVienBaiTapDTO(
                row[0] != null ? row[0].toString() : "",
                row[1] != null ? row[1].toString() : "",
                row[2] != null ? row[2].toString() : "",
                row[3] != null ? row[3].toString() : "Chua nop",
                row[4] != null ? row[4].toString() : null,
                row[5] != null ? row[5].toString() : null,
                row[6] != null ? row[6].toString() : null,
                row[7] != null ? ((Number) row[7]).doubleValue() : null
        )).toList();
    }

    @Override
    public OverviewDTO getOverviewDataByLop(String lopID) {
        List<Object[]> activeRows = baiTapRepository.findAssignmentsByLop(lopID);
        List<Object[]> recentRows = baiTapRepository.findRecentSubmissionsByLop(lopID);

        List<OverviewDTO.OverviewAssignment> activeList = activeRows.stream().map(row -> {
            long daNop = row[4] != null ? ((Number) row[4]).longValue() : 0L;
            long tongCanThu = row[6] != null ? ((Number) row[6]).longValue() : 0L;
            long choCham = row[5] != null ? ((Number) row[5]).longValue() : 0L;
            int percent = tongCanThu > 0 ? (int) Math.round(((double) daNop / tongCanThu) * 100) : 0;

            return new OverviewDTO.OverviewAssignment(
                    row[0].toString(),
                    row[1].toString(),
                    row[2] != null ? row[2].toString() : "",
                    row[3].toString(),
                    daNop + "/" + tongCanThu,
                    percent,
                    choCham + " bài chờ chấm"
            );
        }).toList();

        List<OverviewDTO.OverviewSubmission> recentList = recentRows.stream().map(row -> {
            return new OverviewDTO.OverviewSubmission(
                    row[0] != null ? row[0].toString() : "Sinh viên nhóm",
                    row[1].toString(),
                    row[2].toString(),
                    row[3].toString(),
                    row[4] != null ? ((Number) row[4]).doubleValue() : null
            );
        }).toList();

        long totalActive = activeList.size();
        long totalSub = recentList.size();
        long pendingSub = activeRows.stream().mapToLong(row -> row[5] != null ? ((Number) row[5]).longValue() : 0L).sum();
        long gradedSub = Math.max(0, totalSub - pendingSub);

        return new OverviewDTO(
                totalActive,
                totalSub,
                gradedSub,
                pendingSub,
                activeList,
                recentList
        );
    }

    @Override
    public SinhVienProfileDTO getProfileByUserID(String id) {
        SinhVien sv = sinhVienRepository.findByUser_UserID(id).orElse(null);
        if (sv == null) {
            sv = sinhVienRepository.findById(id).orElse(null);
        }
        if (sv == null) {
            throw new com.example.workreport.exception.AppException(
                    "Không tìm thấy thông tin sinh viên liên kết với tài khoản này", "500", 500
            );
        }

        User user = sv.getUser();
        SinhVienProfileDTO dto = new SinhVienProfileDTO();

        dto.setStringVienID(sv.getSinhVienID());
        dto.setFullName(user.getFullname());
        dto.setEmail(user.getEmail());
        dto.setAvatar(user.getAvatar());
        dto.setLop(sv.getLop());
        dto.setKhoa(sv.getKhoa());
        dto.setGpa(sv.getGpa());

        dto.setPhone(sv.getPhone());
        dto.setBio(sv.getBio());
        dto.setSkills(sv.getSkills());

        return dto;
    }

    @Override
    @Transactional
    public void updateProfile(String userID, SinhVienProfileDTO dto) {
        SinhVien sv = sinhVienRepository.findByUser_UserID(userID).orElse(null);
        if (sv == null) {
            sv = sinhVienRepository.findById(userID).orElse(null);
        }
        if (sv == null) {
            throw new com.example.workreport.exception.AppException(
                    "Hệ thống không tìm thấy dữ liệu sinh viên phù hợp để chỉnh sửa", "500", 500
            );
        }

        User user = sv.getUser();
        user.setFullname(dto.getFullName());
        userRepository.save(user);

        sv.setPhone(dto.getPhone());
        sv.setBio(dto.getBio());
        sv.setSkills(dto.getSkills());
        sinhVienRepository.save(sv);
    }
}