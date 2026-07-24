package com.example.workreport.service;

import com.example.workreport.dto.BaiTapDeadlineDTO;
import com.example.workreport.dto.LichHocTuanDTO;
import com.example.workreport.repository.LichHocRepository;
import org.springframework.stereotype.Service;
import com.example.workreport.repository.BaoCaoRepository;
import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class LichHocServiceImpl implements LichHocService {

    private final LichHocRepository lichHocRepository;
    private final BaoCaoRepository baoCaoRepository;

    public LichHocServiceImpl(
            LichHocRepository lichHocRepository,
            BaoCaoRepository baoCaoRepository
    ) {

        this.lichHocRepository = lichHocRepository;
        this.baoCaoRepository = baoCaoRepository;
    }

    private static final List<String> COLORS = Arrays.asList("ev-blue", "ev-purple", "ev-green", "ev-orange", "ev-yellow");

    private String generateColorClass(String key) {
        if (key == null || key.isBlank()) return "ev-blue";
        int hash = 0;
        for (char c : key.toCharArray()) hash = (hash << 5) - hash + c;
        return COLORS.get(Math.abs(hash) % COLORS.size());
    }

    @Override
    public List<LichHocTuanDTO> getLichHocTuan(String sinhVienID) {
        List<Object[]> rows = lichHocRepository.findLichHocTuongThichSinhVien(sinhVienID);
        List<LichHocTuanDTO> result = new ArrayList<>();

        for (Object[] row : rows) {
            LichHocTuanDTO dto = new LichHocTuanDTO();
            dto.setTietHocID(row[0] != null ? row[0].toString() : "");
            dto.setLopID(row[1] != null ? row[1].toString() : "");
            dto.setTenLop(row[2] != null ? row[2].toString() : "");
            dto.setMaMonHoc(row[3] != null ? row[3].toString() : "");
            dto.setTenMonHoc(row[4] != null ? row[4].toString() : "");
            dto.setTenGiangVien(row[5] != null ? row[5].toString() : "");

            dto.setDayIndex(row[6] != null ? Integer.parseInt(row[6].toString()) : 0);
            dto.setStartSlot(row[7] != null ? Integer.parseInt(row[7].toString()) : 0);
            dto.setSpanSlots(row[8] != null ? Integer.parseInt(row[8].toString()) : 1);

            dto.setPhongHoc(row[9] != null ? row[9].toString() : "TBA");
            dto.setLoaiBuoi(row[10] != null ? row[10].toString() : "Lý thuyết");
            dto.setGhiChu(row[11] != null ? row[11].toString() : "");
            dto.setColorClass(generateColorClass(dto.getMaMonHoc()));

            result.add(dto);
        }
        return result;
    }

    @Override
    public List<BaiTapDeadlineDTO> getDeadlinesBySinhVien(String sinhVienID) {
        List<Object[]> rows = lichHocRepository.findToanBoDeadlineCuaSinhVien(sinhVienID);
        List<BaiTapDeadlineDTO> result = new ArrayList<>();

        DateTimeFormatter isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy");

        for (Object[] row : rows) {
            BaiTapDeadlineDTO dto = new BaiTapDeadlineDTO();
            dto.setBaiTapID(row[0] != null ? row[0].toString() : "");
            dto.setTenBaiTap(row[1] != null ? row[1].toString() : "");

            LocalDateTime deadlineTime = null;

            if (row[2] != null) {

                Timestamp ts = (Timestamp) row[2];

                deadlineTime = ts.toLocalDateTime();

                dto.setDeadline(
                        deadlineTime.format(isoFormatter)
                );

                dto.setDeadlineFmt(
                        deadlineTime.format(displayFormatter)
                );
            }

            dto.setLoai(row[3] != null ? row[3].toString() : "Cá nhân");
            dto.setMonHoc(row[4] != null ? row[4].toString() : "");
            dto.setLopID(row[5] != null ? row[5].toString() : "");
            dto.setTenLop(row[6] != null ? row[6].toString() : "");
            dto.setSinhVienID(sinhVienID);
            boolean daNop =
                    baoCaoRepository
                            .existsByBaiTap_BaiTapID(
                                    dto.getBaiTapID()
                            );

            if (daNop) {

                dto.setTrangThai("Da nop");

            } else {

                LocalDateTime now =
                        LocalDateTime.now();

                if (
                        deadlineTime != null &&
                                deadlineTime.isBefore(now)
                ) {

                    dto.setTrangThai("Qua han");

                } else {

                    dto.setTrangThai("Dang hoat dong");
                }
            }

            result.add(dto);
        }
        return result;
    }

    @Override
    public List<BaiTapDeadlineDTO> getDeadlinesByMonth(String sinhVienID, int month, int year) {
        return getDeadlinesBySinhVien(sinhVienID); // Tái tận dụng cơ chế lọc mảng thông minh ngay tại phía Client React
    }
}