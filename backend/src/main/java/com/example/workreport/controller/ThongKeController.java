package com.example.workreport.controller;

import com.example.workreport.dto.ApiResponse;
import com.example.workreport.dto.ThongKeDTOs.*;
import com.example.workreport.service.ThongKeService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/thongke")
public class ThongKeController {

    private final ThongKeService thongKeService;

    public ThongKeController(ThongKeService thongKeService) {
        this.thongKeService = thongKeService;
    }

    @GetMapping("/tong-quan")
    @PreAuthorize("hasAnyRole('SINH_VIEN', 'GIANG_VIEN')")
    public ResponseEntity<ApiResponse<TongQuanDTO>> getTongQuan(
            @RequestParam String hocKy,
            @RequestParam(required = false) String monHoc,
            Authentication auth) {
        String svId = extractUserId(auth);
        return ResponseEntity.ok(ApiResponse.ok(thongKeService.getTongQuan(svId, hocKy, monHoc)));
    }

    @GetMapping("/tien-do-nop-bai")
    @PreAuthorize("hasAnyRole('SINH_VIEN', 'GIANG_VIEN')")
    public ResponseEntity<ApiResponse<TienDoNopBaiDTO>> getTienDoNopBai(
            @RequestParam String hocKy,
            @RequestParam(required = false) String monHoc,
            Authentication auth) {
        String svId = extractUserId(auth);
        return ResponseEntity.ok(ApiResponse.ok(thongKeService.getTienDoNopBai(svId, hocKy, monHoc)));
    }

    @GetMapping("/diem-trung-binh")
    @PreAuthorize("hasAnyRole('SINH_VIEN', 'GIANG_VIEN')")
    public ResponseEntity<ApiResponse<DiemTrungBinhDTO>> getDiemTrungBinh(
            @RequestParam String hocKy,
            @RequestParam(required = false) String monHoc,
            Authentication auth) {
        String svId = extractUserId(auth);
        return ResponseEntity.ok(ApiResponse.ok(thongKeService.getDiemTrungBinh(svId, hocKy, monHoc)));
    }

    @GetMapping("/trang-thai-bai-nop")
    @PreAuthorize("hasAnyRole('SINH_VIEN', 'GIANG_VIEN')")
    public ResponseEntity<ApiResponse<TrangThaiBaiNopDTO>> getTrangThaiBaiNop(
            @RequestParam String hocKy,
            @RequestParam(required = false) String monHoc,
            Authentication auth) {
        String svId = extractUserId(auth);
        return ResponseEntity.ok(ApiResponse.ok(thongKeService.getTrangThaiBaiNop(svId, hocKy, monHoc)));
    }

    @GetMapping("/chi-tiet-bai-tap")
    @PreAuthorize("hasAnyRole('SINH_VIEN', 'GIANG_VIEN')")
    public ResponseEntity<ApiResponse<ChiTietBaiTapDTO>> getChiTietBaiTap(
            @RequestParam String hocKy,
            @RequestParam(required = false) String monHoc,
            Authentication auth) {
        String svId = extractUserId(auth);
        return ResponseEntity.ok(ApiResponse.ok(thongKeService.getChiTietBaiTap(svId, hocKy, monHoc)));
    }

    @GetMapping("/xuat-excel")
    @PreAuthorize("hasAnyRole('SINH_VIEN', 'GIANG_VIEN')")
    public ResponseEntity<byte[]> xuatExcel(
            @RequestParam String hocKy,
            @RequestParam(required = false) String monHoc,
            Authentication auth) throws Exception {
        String svId = extractUserId(auth);
        byte[] data = thongKeService.xuatExcel(svId, hocKy, monHoc);
        return ResponseEntity.ok()
                .header("Content-Disposition",
                        "attachment; filename=thongke_" + hocKy + ".xlsx")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(data);
    }

    private String extractUserId(Authentication auth) {
        UserDetails ud = (UserDetails) auth.getPrincipal();
        return ud.getUsername();
    }
}