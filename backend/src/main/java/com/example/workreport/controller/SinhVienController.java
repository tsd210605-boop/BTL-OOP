package com.example.workreport.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.workreport.dto.ApiResponse;
import com.example.workreport.dto.SinhVienProfileDTO;
import com.example.workreport.dto.SinhVienTienDoDTO;
import com.example.workreport.dto.SinhVienBaiTapDTO;
import com.example.workreport.dto.OverviewDTO;
import com.example.workreport.exception.AppException;
import com.example.workreport.model.SinhVien;
import com.example.workreport.model.BaiTap;
import com.example.workreport.repository.SinhVienRepository;
import com.example.workreport.repository.BaiTapRepository;
import com.example.workreport.service.SinhVienService;

@RestController
@RequestMapping("/api/sinhvien")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class SinhVienController {

    private final SinhVienRepository sinhVienRepository;
    private final BaiTapRepository baiTapRepository;

    @Autowired
    private SinhVienService sinhVienService;

    public SinhVienController(
            SinhVienRepository sinhVienRepository,
            BaiTapRepository baiTapRepository
    ) {
        this.sinhVienRepository = sinhVienRepository;
        this.baiTapRepository = baiTapRepository;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SinhVien>>> getAllSinhVien() {
        return ResponseEntity.ok(ApiResponse.ok(sinhVienRepository.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SinhVienProfileDTO>> getSinhVienById(@PathVariable String id) {
        SinhVienProfileDTO profile = sinhVienService.getProfileByUserID(id);
        return ResponseEntity.ok(ApiResponse.ok(profile));
    }

    @GetMapping("/{id}/baitap")
    public ResponseEntity<ApiResponse<List<BaiTap>>> getBaiTapBySinhVien(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.ok(baiTapRepository.findByLopOfSinhVien(id)));
    }

    @GetMapping("/profile/{userID}")
    public ResponseEntity<ApiResponse<SinhVienProfileDTO>> getProfileByUserID(@PathVariable String userID) {
        SinhVienProfileDTO profile = sinhVienService.getProfileByUserID(userID);
        return ResponseEntity.ok(ApiResponse.ok(profile));
    }

    @PutMapping("/profile/{userID}")
    public ResponseEntity<?> updateSinhVienProfile(@PathVariable String userID, @RequestBody SinhVienProfileDTO dto) {
        try {
            sinhVienService.updateProfile(userID, dto);
            return ResponseEntity.ok(ApiResponse.ok("Cập nhật thông tin hồ sơ thành công"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("{\"success\":false,\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/giangvien/{giangVienID}")
    public ResponseEntity<?> getDanhSachTienDo(@PathVariable String giangVienID) {
        try {
            List<SinhVienTienDoDTO> list = sinhVienService.getTienDoSinhVien(giangVienID);
            return ResponseEntity.ok(ApiResponse.ok(list));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("{\"success\":false,\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/{id}/lop/{lopID}/tien-do-bai-tap")
    public ResponseEntity<ApiResponse<List<SinhVienBaiTapDTO>>> getChiTietBaiTapTheoLop(
            @PathVariable String id,
            @PathVariable String lopID) {
        List<SinhVienBaiTapDTO> list = sinhVienService.getChiTietBaiTapTheoLop(lopID, id);
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @GetMapping("/lop/{lopID}/overview")
    public ResponseEntity<?> getOverviewDataByLop(@PathVariable String lopID) {
        try {
            OverviewDTO data = sinhVienService.getOverviewDataByLop(lopID);
            return ResponseEntity.ok(ApiResponse.ok(data));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("{\"success\":false,\"message\":\"" + e.getMessage() + "\"}");
        }
    }
}