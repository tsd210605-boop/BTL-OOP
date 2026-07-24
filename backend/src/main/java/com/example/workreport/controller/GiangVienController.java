package com.example.workreport.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.workreport.dto.ApiResponse;
import com.example.workreport.dto.CapNhatProfileRequest;
import com.example.workreport.model.LopHoc;
import com.example.workreport.model.SinhVien;
import com.example.workreport.model.GiangVien;
import com.example.workreport.model.LopHocSinhVien;
import com.example.workreport.repository.GiangVienRepository;
import com.example.workreport.repository.LopHocRepository;
import com.example.workreport.repository.SinhVienRepository;
import com.example.workreport.repository.LopHocSinhVienRepository;
import com.example.workreport.service.GiangVienService;
import java.util.Map;

@RestController
@RequestMapping("/api/giangvien")
@CrossOrigin(origins = "*")
public class GiangVienController {

    private final GiangVienRepository repository;
    private final GiangVienService giangVienService;
    private final LopHocRepository lopHocRepository;
    private final SinhVienRepository sinhVienRepository;
    private final LopHocSinhVienRepository lopHocSinhVienRepository;

    public GiangVienController(
            GiangVienRepository repository,
            GiangVienService giangVienService,
            LopHocRepository lopHocRepository,
            SinhVienRepository sinhVienRepository,
            LopHocSinhVienRepository lopHocSinhVienRepository
    ) {
        this.repository = repository;
        this.giangVienService = giangVienService;
        this.lopHocRepository = lopHocRepository;
        this.sinhVienRepository = sinhVienRepository;
        this.lopHocSinhVienRepository = lopHocSinhVienRepository;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<GiangVien>>>
    getAllGiangVien() {

        return ResponseEntity.ok(
                ApiResponse.ok(repository.findAll())
        );
    }

    @GetMapping("/profile/{userID}")
    public ResponseEntity<?> getProfile(
            @PathVariable String userID
    ) {

        try {

            return ResponseEntity.ok(
                    ApiResponse.ok(
                            giangVienService.getProfile(userID)
                    )
            );

        } catch (Exception e) {

            return ResponseEntity.badRequest().body(
                    ApiResponse.badRequest(e.getMessage())
            );
        }
    }

    @PutMapping("/profile/{userID}")
    public ResponseEntity<?> updateProfile(
            @PathVariable String userID,
            @RequestBody CapNhatProfileRequest request
    ) {

        try {

            return ResponseEntity.ok(
                    ApiResponse.ok(
                            giangVienService.updateProfile(
                                    userID,
                                    request
                            )
                    )
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.badRequest(e.getMessage())
            );
        }
    }

    @PostMapping("/lop/{lopID}/sinhvien")
    public ResponseEntity<?> addSinhVienToLop(
            @PathVariable String lopID,
            @RequestBody Map<String, String> request
    ) {
        try {
            String sinhVienID = request.get("sinhVienID");
            if (sinhVienID == null || sinhVienID.isEmpty()) {
                return ResponseEntity.badRequest().body(ApiResponse.badRequest("Mã sinh viên không được để trống"));
            }

            LopHoc lopHoc = lopHocRepository.findById(lopID)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp học phần " + lopID));

            SinhVien sinhVien = sinhVienRepository.findById(sinhVienID)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên " + sinhVienID));

            if (lopHocSinhVienRepository.existsBySinhVien_SinhVienIDAndLopHoc_LopID(sinhVienID, lopID)) {
                return ResponseEntity.badRequest().body(ApiResponse.badRequest("Sinh viên đã có trong lớp học phần này"));
            }

            LopHocSinhVien lopHocSinhVien = new LopHocSinhVien(lopHoc, sinhVien);
            lopHocSinhVienRepository.save(lopHocSinhVien);

            return ResponseEntity.ok(ApiResponse.ok(Map.of("message", "Thêm sinh viên thành công")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.badRequest(e.getMessage()));
        }
    }
}