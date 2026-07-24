package com.example.workreport.service;

import com.example.workreport.dto.ThongKeDTOs;
import com.example.workreport.dto.ThongKeDTOs.ChiTietBaiTapDTO;
import com.example.workreport.dto.ThongKeDTOs.DiemTrungBinhDTO;
import com.example.workreport.dto.ThongKeDTOs.TienDoNopBaiDTO;
import com.example.workreport.dto.ThongKeDTOs.TongQuanDTO;
import com.example.workreport.dto.ThongKeDTOs.TrangThaiBaiNopDTO;

import com.example.workreport.model.BaiTap;
import com.example.workreport.model.LopHoc;
import com.example.workreport.model.MonHoc;

import com.example.workreport.repository.BaiTapRepository;
import com.example.workreport.repository.BaoCaoRepository;
import com.example.workreport.repository.SinhVienRepository;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ThongKeServiceImpl implements ThongKeService {
    private final BaiTapRepository baiTapRepo;
    private final BaoCaoRepository baoCaoRepo;
    private final SinhVienRepository sinhVienRepo;

    public ThongKeServiceImpl(
            BaiTapRepository baiTapRepo,
            BaoCaoRepository baoCaoRepo,
            SinhVienRepository sinhVienRepo
    ) {
        this.baiTapRepo = baiTapRepo;
        this.baoCaoRepo = baoCaoRepo;
        this.sinhVienRepo = sinhVienRepo;
    }

    @Override
    public TongQuanDTO getTongQuan(String sinhVienID, String hocKy, String monHocID) {
        List<BaiTap> baiTaps = layBaiTap(sinhVienID, monHocID);
        List<String> baiTapIDs = baiTaps.stream().map(BaiTap::getBaiTapID).collect(Collectors.toList());
        List<Double> diems = baiTapIDs.isEmpty() ? List.of() : baoCaoRepo.findAllDiemBySinhVien(sinhVienID, baiTapIDs);

        double diemTB = diems.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        int soBaiDaCham = baiTapIDs.isEmpty() ? 0 : baoCaoRepo.countDaChamBySinhVien(sinhVienID, baiTapIDs);
        int dangHD = (int) baiTaps.stream().filter(b -> b.getDeadline() != null && b.getDeadline().isAfter(LocalDateTime.now())).count();

        List<Object[]> lopInfoList = sinhVienRepo.findLopInfoBySinhVienID(sinhVienID);
        Object[] lopInfo = (lopInfoList != null && !lopInfoList.isEmpty()) ? lopInfoList.get(0) : null;
        String tenLop = lopInfo != null ? String.valueOf(lopInfo[0]) : "";
        int tongSinhVien = lopInfo != null ? ((Number) lopInfo[1]).intValue() : 0;
        ChiTietBaiTapDTO chiTietDTO = getChiTietBaiTap(sinhVienID, hocKy, monHocID);
        long soNop = chiTietDTO.getRows().stream().filter(r -> !"missing".equals(r.getTrangThai())).count();
        double tyLe = baiTaps.isEmpty() ? 0 : (double) soNop / baiTaps.size() * 100;
        return TongQuanDTO.builder()
                .diemTBChung(round1(diemTB))
                .soBaiDaCham(soBaiDaCham)
                .xepLoaiDiem(xepLoaiDiem(diemTB))
                .tongBaiTap(baiTaps.size())
                .soBaiDangHoatDong(dangHD)
                .soBaiDaKetThuc(baiTaps.size() - dangHD)
                .tongSinhVien(tongSinhVien)
                .tenLop(tenLop)
                .tyLeHoanThanh(round1(tyLe))
                .xepLoaiHoanThanh(xepLoaiHoanThanh(tyLe))
                .build();
    }

    @Override
    public TienDoNopBaiDTO getTienDoNopBai(String sinhVienID, String hocKy, String monHocID) {
        List<BaiTap> baiTaps = layBaiTap(sinhVienID, monHocID);
        List<TienDoNopBaiDTO.BaiTapBarDTO> bars = new ArrayList<>();
        for (BaiTap bt : baiTaps) {
            String id = bt.getBaiTapID();
            long tongSV = baiTapRepo.countSinhVienByBaiTapID(id);
            long daNop = baoCaoRepo.countDaNopByBaiTapID(id);
            long daCham = baoCaoRepo.countDaChamByBaiTapID(id);
            long chuaNop = Math.max(tongSV - daNop, 0);
            bars.add(
                    TienDoNopBaiDTO.BaiTapBarDTO
                            .builder()
                            .baiTapID(id)
                            .tenBaiTap(bt.getTenBaiTap())
                            .daNop(daNop)
                            .chuaNop(chuaNop)
                            .daCham(daCham)
                            .build()
            );
        }
        return new TienDoNopBaiDTO(bars);
    }

    @Override
    public DiemTrungBinhDTO getDiemTrungBinh(String sinhVienID, String hocKy, String monHocID) {
        List<BaiTap> baiTaps = layBaiTap(sinhVienID, monHocID);
        List<DiemTrungBinhDTO.DiemTheoKyDTO> rows = new ArrayList<>();
        for (BaiTap bt : baiTaps) {
            double diemCaNhan = baoCaoRepo.findDiemCaNhan(bt.getBaiTapID(), sinhVienID).orElse(0.0);
            double diemTB = baoCaoRepo.avgDiemByBaiTapID(bt.getBaiTapID()).orElse(0.0);
            rows.add(
                    DiemTrungBinhDTO.DiemTheoKyDTO
                            .builder()
                            .tenBaiTap(bt.getTenBaiTap())
                            .diemCaNhan(round1(diemCaNhan))
                            .diemTBLop(round1(diemTB))
                            .build()
            );
        }
        return new DiemTrungBinhDTO(rows);
    }

    @Override
    public TrangThaiBaiNopDTO getTrangThaiBaiNop(String sinhVienID, String hocKy, String monHocID) {
        List<BaiTap> baiTaps = layBaiTap(sinhVienID, monHocID);
        List<String> baiTapIDs = baiTaps.stream().map(BaiTap::getBaiTapID).collect(Collectors.toList());
        long daNop = baoCaoRepo.countDaNopBySinhVien(sinhVienID, baiTapIDs);
        long daCham = baoCaoRepo.countDaChamBySinhVien(sinhVienID, baiTapIDs);
        long tong = baiTaps.size();
        long chuaNop = Math.max(tong - daNop, 0);
        return TrangThaiBaiNopDTO.builder()
                .tongBaiNop(tong)
                .daNop(daNop)
                .daCham(daCham)
                .chuaNop(chuaNop)
                .nopTre(0L)
                .build();
    }

    @Override
    public ChiTietBaiTapDTO getChiTietBaiTap(String sinhVienID, String hocKy, String monHocID) {
        List<BaiTap> baiTaps = layBaiTap(sinhVienID, monHocID);
        List<ChiTietBaiTapDTO.BaiTapRowDTO> rows = baiTaps.stream()
                .map(bt -> {
                    String trangThai;
                    String nhan;
                    String ngayNop = "—";
                    Double diem = null;
                    List<Object[]> chiTiet = baoCaoRepo.findChiTietBySinhVienAndBaiTap(sinhVienID, bt.getBaiTapID());
                    if (!chiTiet.isEmpty()) {
                        Object[] r = chiTiet.get(0);
                        diem = r[4] != null ? ((Number) r[4]).doubleValue() : null;
                        ngayNop = r[2] != null ? r[2].toString() : "—";
                        String rawTrangThai = r[5] != null ? r[5].toString() : "missing";
                        trangThai = normalizeTrangThai(rawTrangThai);
                        nhan = mapNhan(rawTrangThai);
                    } else {
                        trangThai = "missing";
                        nhan = "Chưa nộp";
                    }
                    String tenMonHoc = "";
                    LopHoc lopHoc = bt.getLopHoc();
                    if (lopHoc != null && lopHoc.getMonHoc() != null) {
                        MonHoc monHoc = lopHoc.getMonHoc();
                        tenMonHoc = monHoc.getTenMonHoc() != null ? monHoc.getTenMonHoc() : "";
                    }
                    return ChiTietBaiTapDTO.BaiTapRowDTO.builder()
                                    .tenBaiTap(bt.getTenBaiTap())
                                    .tenMonHoc(tenMonHoc)
                                    .diem(diem)
                                    .trangThai(trangThai)
                                    .nhanTrangThai(nhan)
                                    .ngayNop(ngayNop)
                                    .build();
                }).collect(Collectors.toList());
        return new ChiTietBaiTapDTO(rows);
    }

    @Override
    public byte[] xuatExcel(String sinhVienID, String hocKy, String monHocID) throws Exception {
        ChiTietBaiTapDTO dto = getChiTietBaiTap(sinhVienID, hocKy, monHocID);
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("ThongKe");
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Tên bài tập");
        header.createCell(1).setCellValue("Môn học");
        header.createCell(2).setCellValue("Điểm");
        header.createCell(3).setCellValue("Trạng thái");
        int rowIdx = 1;
        for (ChiTietBaiTapDTO.BaiTapRowDTO row : dto.getRows()) {
            Row r = sheet.createRow(rowIdx++);
            r.createCell(0).setCellValue(row.getTenBaiTap());
            r.createCell(1).setCellValue(row.getTenMonHoc());
            r.createCell(2).setCellValue(row.getDiem() != null ? row.getDiem() : 0);
            r.createCell(3).setCellValue(row.getNhanTrangThai());
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return out.toByteArray();
    }

    private List<BaiTap> layBaiTap(String sinhVienID, String monHocID) {
        if (monHocID != null && !monHocID.isBlank()) {
            return baiTapRepo.findByLopOfSinhVienAndMonHoc(sinhVienID, monHocID);
        }
        return baiTapRepo.findByLopOfSinhVien(sinhVienID);
    }

    private double round1(double v) {
        return Math.round(v * 10.0) / 10.0;
    }

    private String xepLoaiDiem(double diem) {
        if (diem >= 8.5) {
            return "Giỏi";
        }
        if (diem >= 7) {
            return "Khá";
        }
        if (diem >= 5) {
            return "Trung bình";
        }
        return "Yếu";
    }

    private String xepLoaiHoanThanh(double percent) {
        if (percent >= 90) {
            return "Xuất sắc";
        }
        if (percent >= 70) {
            return "Tốt";
        }
        if (percent >= 50) {
            return "Trung bình";
        }
        return "Kém";
    }

    private String normalizeTrangThai(String raw) {
        if (raw == null) {
            return "missing";
        }
        raw = raw.toLowerCase();
        if (raw.contains("graded") || raw.contains("đã chấm")) {
            return "graded";
        }
        if (raw.contains("submitted") || raw.contains("đã nộp")) {
            return "submitted";
        }
        if (raw.contains("draft") || raw.contains("đang làm")) {
            return "draft";
        }
        return "missing";
    }

    private String mapNhan(String raw) {
        if (raw == null) {
            return "Chưa nộp";
        }
        raw = raw.toLowerCase();
        if (raw.contains("graded") || raw.contains("đã chấm")) {
            return "Đã chấm";
        }
        if (raw.contains("submitted") || raw.contains("đã nộp")) {
            return "Đã nộp";
        }
        if (raw.contains("draft") || raw.contains("đang làm")) {
            return "Đang làm";
        }
        return "Chưa nộp";
    }
}
