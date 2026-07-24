package com.example.workreport.service;

import com.example.workreport.dto.BaiTapChiTietDTO;
import com.example.workreport.dto.BaiTapGiangVienDTO;
import com.example.workreport.dto.BaiTapSinhVienDTO;
import com.example.workreport.dto.TaoBaiTapRequest;
import com.example.workreport.dto.LopReportDTO;
import com.example.workreport.dto.BaoCaoSinhVienDTO;
import com.example.workreport.dto.ChamDiemRequest;
import com.example.workreport.model.BaiTap;
import com.example.workreport.model.LopHoc;
import com.example.workreport.repository.BaiTapRepository;
import com.example.workreport.repository.LopHocRepository;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class BaiTapService {
    private final BaiTapRepository baiTapRepository;
    private final LopHocRepository lopHocRepository;

    public BaiTapService(
            BaiTapRepository baiTapRepository,
            LopHocRepository lopHocRepository
    ) {
        this.baiTapRepository = baiTapRepository;
        this.lopHocRepository = lopHocRepository;
    }

    public List<BaiTap> getAllBaiTap() {
        return baiTapRepository.findAll();
    }

    public BaiTap getBaiTapById(String baiTapID) {
        return baiTapRepository.findById(baiTapID).orElseThrow(() -> new RuntimeException("Không tìm thấy bài tập"));
    }

    public List<BaiTap> getByLopHoc(String lopID) {
        return baiTapRepository.findByLopHoc_LopID(lopID);
    }

    public List<BaiTap> getByGiangVienEntity(String giangVienID) {
        return baiTapRepository.findByLopHoc_GiangVien_GiangVienID(giangVienID);
    }

    public BaiTap taoBaiTap(TaoBaiTapRequest request) {
        LopHoc lopHoc = lopHocRepository.findById(request.getLopID()).orElseThrow(() -> new RuntimeException("Không tìm thấy lớp học"));
        BaiTap baiTap = new BaiTap();
        baiTap.setBaiTapID("BT-" + UUID.randomUUID().toString().substring(0, 8));
        baiTap.setTenBaiTap(request.getTenBaiTap());
        baiTap.setMoTa(request.getMoTa());
        baiTap.setDeadline(request.getDeadline());
        baiTap.setLoai(request.getLoai());
        baiTap.setDiemToiDa(request.getDiemToiDa());
        baiTap.setTrangThai("Dang hoat dong");
        baiTap.setLopHoc(lopHoc);
        return baiTapRepository.save(baiTap);
    }

    public BaiTap capNhatBaiTap(String baiTapID, TaoBaiTapRequest request) {
        BaiTap baiTap = baiTapRepository.findById(baiTapID).orElseThrow(() -> new RuntimeException("Không tìm thấy bài tập"));
        baiTap.setTenBaiTap(request.getTenBaiTap());
        baiTap.setMoTa(request.getMoTa());
        baiTap.setDeadline(request.getDeadline());
        baiTap.setLoai(request.getLoai());
        baiTap.setDiemToiDa(request.getDiemToiDa());
        return baiTapRepository.save(baiTap);
    }

    public void xoaBaiTap(String baiTapID) {
        if (!baiTapRepository.existsById(baiTapID)) {
            throw new RuntimeException("Bài tập không tồn tại");
        }
        baiTapRepository.deleteById(baiTapID);
    }

    public List<BaiTapSinhVienDTO> getBaiTapBySinhVien(String sinhVienID) {
        List<Object[]> rows = baiTapRepository.findBaiTapBySinhVienIDNative(sinhVienID);

        return rows.stream().map(row -> {
            Double diemTB = 0.0;
            if (row[6] != null) {
                if (row[6] instanceof Number) { diemTB = ((Number) row[6]).doubleValue(); }
                else { try { diemTB = Double.parseDouble(row[6].toString()); } catch (Exception e) { diemTB = 0.0; } }
            }

            Long soNopBai = parseLongSafe(row[7]);
            Long tongSinhVien = parseLongSafe(row[8]);
            Long soChamBai = parseLongSafe(row[9]);

            return new BaiTapSinhVienDTO(
                    row[0] != null ? row[0].toString() : "",                   // baiTapID
                    row[1] != null ? row[1].toString() : "",                   // tenBaiTap
                    row[2] != null ? row[2].toString() : "",                   // moTa
                    row[3] != null ? row[3].toString() : "",                   // deadline
                    row[4] != null ? row[4].toString() : "",                   // trangThai
                    row[5] != null ? row[5].toString() : "",                   // loai
                    diemTB,                                                    // diemTB
                    soNopBai,                                                  // soNopBai
                    tongSinhVien,                                              // tongSinhVien
                    soChamBai,                                                 // soChamBai
                    row[10] != null ? row[10].toString() : "Chua nop",         // trangThaiNop
                    row[11] != null ? row[11].toString() : "",                 // lopID
                    row[12] != null ? row[12].toString() : "Giảng viên chưa để lại lời phê.", // nhanXet
                    row[13] != null ? row[13].toString() : ""                  // fileBaoCao
            );
        }).toList();
    }

    public List<BaiTapGiangVienDTO> getBaiTapByGiangVien(String giangVienID) {
        List<Object[]> rows = baiTapRepository.findBaiTapByGiangVienID(giangVienID);
        return rows.stream().map(row -> {
            Long daNop = row[7] != null ? ((Number) row[7]).longValue() : 0L;
            Long choCham = row[8] != null ? ((Number) row[8]).longValue() : 0L;
            Long tongMucCanThu = row[10] != null ? ((Number) row[10]).longValue() : 0L;
            Long daCham = Math.max(daNop - choCham, 0L);
            Double diemTB = row[9] != null ? ((Number) row[9]).doubleValue() : 0.0;
            Double diemToiDa = row[6] != null ? ((Number) row[6]).doubleValue() : 10.0;

            return new BaiTapGiangVienDTO(
                    row[0] != null ? row[0].toString() : "",
                    row[1] != null ? row[1].toString() : "",
                    row[2] != null ? row[2].toString() : "",
                    row[5] != null ? row[5].toString() : "Dang hoat dong",
                    row[4] != null ? row[4].toString() : "---",
                    row[3] != null ? row[3].toString() : "Ca nhan",
                    diemToiDa.intValue(),
                    diemTB,
                    daNop,
                    tongMucCanThu,
                    daCham,
                    choCham
            );
        }).toList();
    }

    public List<BaiTapChiTietDTO> getChiTietBaiTap(String baiTapID) {
        List<Object[]> rows = baiTapRepository.findChiTietBaiTap(baiTapID);
        return rows.stream().map(row -> {
            Double diemSo = null;
            if (row[5] != null) {
                if (row[5] instanceof Number) {
                    diemSo = ((Number) row[5]).doubleValue();
                } else {
                    try { diemSo = Double.parseDouble(row[5].toString()); } catch (Exception e) { diemSo = null; }
                }
            }

            return new BaiTapChiTietDTO(
                    row[0] != null ? row[0].toString() : "",                   // sinhVienID
                    row[1] != null ? row[1].toString() : "",                   // fullName
                    row[2] != null ? row[2].toString() : "",                   // lop
                    row[3] != null ? row[3].toString() : "",                   // tenNhom
                    row[8] != null ? row[8].toString() : "Chua nop",           // trangThaiNop
                    row[4] != null ? row[4].toString() : "Chua cham",          // trangThaiCham
                    diemSo,                                                    // diem bốc từ biến diemSo an toàn
                    row[6] != null ? row[6].toString() : "",                   // fileBaoCao
                    row[7] != null ? row[7].toString() : "",                   // ngayNop
                    row[9] != null ? row[9].toString() : "",                   // tenBaiTap
                    row[10] != null ? row[10].toString() : "",                 // moTa
                    row[11] != null ? row[11].toString() : ""                  // nhanXet
            );
        }).toList();
    }

    public List<BaiTapGiangVienDTO> getBaiTapByLopDTO(String lopID) {
        List<Object[]> rows = baiTapRepository.findBaiTapByLopIDNative(lopID);

        return rows.stream().map(row -> {
                    try {
                        String baiTapID   = row[0] != null ? row[0].toString() : "";
                        String tenBaiTap  = row[1] != null ? row[1].toString() : "";
                        String moTa       = row[2] != null ? row[2].toString() : "";
                        String loai       = row[3] != null ? row[3].toString() : "Ca nhan";
                        String deadline   = row[4] != null ? row[4].toString() : "---";
                        String trangThai  = row[5] != null ? row[5].toString() : "Dang hoat dong";

                        Double diemToiDa = 10.0;
                        if (row[6] != null) {
                            diemToiDa = ((Number) row[6]).doubleValue();
                        }

                        Long daNop = row[7] != null ? ((Number) row[7]).longValue() : 0L;
                        Long choCham = row[8] != null ? ((Number) row[8]).longValue() : 0L;
                        Long tongMucCanThu = row[10] != null ? ((Number) row[10]).longValue() : 0L;
                        Long daCham = Math.max(daNop - choCham, 0L);

                        Double diemTB = 0.0;
                        if (row[9] != null) {
                            diemTB = ((Number) row[9]).doubleValue();
                        }

                        return new BaiTapGiangVienDTO(
                                baiTapID,
                                tenBaiTap,
                                moTa,
                                trangThai,
                                deadline,
                                loai,
                                diemToiDa.intValue(),
                                diemTB,
                                daNop,
                                tongMucCanThu,
                                daCham,
                                choCham
                        );
                    } catch (Exception e) {
                        System.err.println("Lỗi bóc tách dòng bài tập: " + e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }

    public LopReportDTO getReportDataByLop(String lopID) {
        List<Object[]> statsRows = baiTapRepository.findReportStatsByLop(lopID);
        List<Object[]> topRows = baiTapRepository.findTopStudentsByLop(lopID);

        Double diemTBChung = 0.0;
        Long tongBaiTap = 0L;
        Long tongSinhVien = 0L;
        Integer tyLeHoanThanh = 0;

        if (!statsRows.isEmpty() && statsRows.get(0) != null) {
            Object[] stats = statsRows.get(0);
            try {
                diemTBChung   = stats[0] != null ? Double.parseDouble(stats[0].toString()) : 0.0;
                tongBaiTap    = stats[1] != null ? Long.parseLong(stats[1].toString()) : 0L;
                tongSinhVien  = stats[2] != null ? Long.parseLong(stats[2].toString()) : 0L;
                tyLeHoanThanh = stats[3] != null ? (int) Math.round(Double.parseDouble(stats[3].toString())) : 0;
            } catch (Exception e) {
                System.err.println("Lỗi parse dữ liệu stats report: " + e.getMessage());
            }
        }

        java.util.concurrent.atomic.AtomicInteger index = new java.util.concurrent.atomic.AtomicInteger(1);
        List<LopReportDTO.TopStudent> topStudents = topRows.stream().map(row -> {
            try {
                return new LopReportDTO.TopStudent(
                        index.getAndIncrement(),
                        row[0].toString(),
                        row[1].toString(),
                        row[2] != null ? row[2].toString() : "",
                        row[3] != null ? Double.parseDouble(row[3].toString()) : 0.0,
                        row[4] != null ? Long.parseLong(row[4].toString()) : 0L
                );
            } catch (Exception e) {
                System.err.println("Lỗi bóc dòng top student: " + e.getMessage());
                return null;
            }
        }).filter(Objects::nonNull).toList();

        List<BaiTapGiangVienDTO> assignments = getBaiTapByLopDTO(lopID);

        List<LopReportDTO.ChartAssignmentData> chartData = assignments.stream().map(a ->
                new LopReportDTO.ChartAssignmentData(
                        a.getTenBaiTap(),
                        a.getDaNop(),
                        a.getTongSinhVien(),
                        a.getDiemTrungBinh() != null ? a.getDiemTrungBinh() : 0.0
                )
        ).toList();

        return new LopReportDTO(diemTBChung, tongBaiTap, tongSinhVien, tyLeHoanThanh, topStudents, chartData);
    }

    @org.springframework.transaction.annotation.Transactional
    public void chamDiemBaiNop(ChamDiemRequest request) {
        String baoCaoID = baiTapRepository.findBaoCaoIdByTaiKhoan(request.getBaiTapID(), request.getSinhVienID());

        if (baoCaoID == null || baoCaoID.isEmpty()) {
            throw new RuntimeException("Không tìm thấy tệp tin báo cáo nộp bài của đối tượng này để chấm điểm!");
        }

        baiTapRepository.upsertDanhGiaDiem(baoCaoID, request.getDiem(), request.getNhanXet());
    }

    public List<BaiTapSinhVienDTO> getBaiTapDashboardSinhVien(String sinhVienID) {
        List<Object[]> rows = baiTapRepository.findBaiTapBySinhVienIDNative(sinhVienID);

        return rows.stream().map(row -> {
            Double diemTB = 0.0;
            if (row[6] != null) {
                if (row[6] instanceof Number) { diemTB = ((Number) row[6]).doubleValue(); }
                else { try { diemTB = Double.parseDouble(row[6].toString()); } catch (Exception e) { diemTB = 0.0; } }
            }

            Long soNopBai = parseLongSafe(row[7]);
            Long tongSinhVien = parseLongSafe(row[8]);
            Long soChamBai = parseStudySafe(row[9]);

            return new BaiTapSinhVienDTO(
                    row[0] != null ? row[0].toString() : "",
                    row[1] != null ? row[1].toString() : "",
                    row[2] != null ? row[2].toString() : "",
                    row[3] != null ? row[3].toString() : "",
                    row[4] != null ? row[4].toString() : "",
                    row[5] != null ? row[5].toString() : "",
                    diemTB,
                    soNopBai,
                    tongSinhVien,
                    soChamBai,
                    row[10] != null ? row[10].toString() : "Chua nop",
                    row[11] != null ? row[11].toString() : "",
                    row[12] != null ? row[12].toString() : "Giảng viên chưa để lại lời phê.",
                    row[13] != null ? row[13].toString() : ""
            );
        }).toList();
    }

    private Long parseLongSafe(Object obj) {
        if (obj == null) return 0L;
        if (obj instanceof Number) return ((Number) obj).longValue();
        try { return Long.parseLong(obj.toString()); } catch (NumberFormatException e) { return 0L; }
    }

    private Long parseStudySafe(Object obj) {
        return parseLongSafe(obj);
    }

    public BaoCaoSinhVienDTO getReportDataForSinhVien(String lopID, String sinhVienID) {
        List<Object[]> assignments = baiTapRepository.findBaiTapByLopIDNative(lopID);
        List<Object[]> progress = new ArrayList<>();
        try {
            progress = baiTapRepository.findBaiTapByLopAndSinhVien(lopID, sinhVienID);
        } catch (Exception e) {
            System.err.println("Lỗi lấy tiến độ: " + e.getMessage());
        }

        List<BaoCaoSinhVienDTO.ChartData> chartDataList = new ArrayList<>();
        int totalAssignments = assignments.size();
        int soBaiChuaNopHoacTre = 0;

        for (Object[] row : assignments) {
            String bID = row[0] != null ? row[0].toString() : "";
            String tenBT = row[1] != null ? row[1].toString() : "";
            Double dtbLop = row[9] != null ? ((Number) row[9]).doubleValue() : 0.0;

            Double diemCuaMe = null;
            String textNop = "Chua nop";

            for (Object[] p : progress) {
                if (p[0] != null && p[0].toString().equals(bID)) {
                    textNop = p[3] != null ? p[3].toString() : "Chua nop";
                    if ("Da cham".equalsIgnoreCase(textNop) && p[7] != null) {
                        diemCuaMe = ((Number) p[7]).doubleValue();
                    }
                    break;
                }
            }

            if ("Chua nop".equalsIgnoreCase(textNop) || "Chua cham".equalsIgnoreCase(textNop)) {
                soBaiChuaNopHoacTre++;
            }

            chartDataList.add(new BaoCaoSinhVienDTO.ChartData(tenBT, diemCuaMe, dtbLop));
        }

        int tyLeHoanThanh = totalAssignments > 0 ? ((totalAssignments - soBaiChuaNopHoacTre) * 100) / totalAssignments : 0;

        List<Object[]> topRows = baiTapRepository.findTopStudentsByLop(lopID);
        List<BaoCaoSinhVienDTO.TopStudent> bxh = new ArrayList<>();
        int rankHienTai = 0;
        int currentRankIndex = 1;

        for (Object[] row : topRows) {
            String svID = row[0].toString();
            String name = row[1].toString();
            String cl = row[2] != null ? row[2].toString() : "";
            Double d = row[3] != null ? Double.parseDouble(row[3].toString()) : 0.0;

            if (svID.equalsIgnoreCase(sinhVienID)) {
                rankHienTai = currentRankIndex;
            }
            bxh.add(new BaoCaoSinhVienDTO.TopStudent(name, svID, cl, d));
            currentRankIndex++;
        }

        Double diemTrungBinhHocPhan = baiTapRepository.findDiemTrungBinhHocPhanCuaRiengSinhVien(lopID, sinhVienID);
        if (diemTrungBinhHocPhan == null) {
            diemTrungBinhHocPhan = 0.0;
        }

        diemTrungBinhHocPhan = Math.round(diemTrungBinhHocPhan * 10.0) / 10.0;

        return new BaoCaoSinhVienDTO(diemTrungBinhHocPhan, rankHienTai, tyLeHoanThanh, soBaiChuaNopHoacTre, bxh, chartDataList);
    }
}