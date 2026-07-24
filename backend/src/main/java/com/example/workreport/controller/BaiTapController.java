package com.example.workreport.controller;

import com.example.workreport.dto.ApiResponse;
import com.example.workreport.dto.BaiTapChiTietDTO;
import com.example.workreport.dto.BaiTapGiangVienDTO;
import com.example.workreport.dto.BaiTapSinhVienDTO;
import com.example.workreport.dto.BaoCaoSinhVienDTO;
import com.example.workreport.dto.TaoBaiTapRequest;
import com.example.workreport.service.BaiTapService;
import com.example.workreport.service.LichHocService;
import com.example.workreport.dto.ChamDiemRequest;
import com.example.workreport.repository.BaiTapRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/api/baitap")
@CrossOrigin(origins = "*")
public class BaiTapController {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final BaiTapService baiTapService;
    private final BaiTapRepository baiTapRepository;
    private LichHocService lichHocService;

    public BaiTapController(BaiTapService baiTapService, BaiTapRepository baiTapRepository) {
        this.baiTapService = baiTapService;
        this.baiTapRepository = baiTapRepository;
    }

    @GetMapping("/sinhvien/{sinhVienID}")
    public ResponseEntity<ApiResponse<List<BaiTapSinhVienDTO>>> getBaiTapBySinhVien(@PathVariable String sinhVienID) {
        return ResponseEntity.ok(ApiResponse.ok(baiTapService.getBaiTapBySinhVien(sinhVienID)));
    }

    @GetMapping("/giangvien/{giangVienID}")
    public ResponseEntity<ApiResponse<List<BaiTapGiangVienDTO>>> getByGiangVien(@PathVariable String giangVienID) {
        return ResponseEntity.ok(ApiResponse.ok(baiTapService.getBaiTapByGiangVien(giangVienID)));
    }

    @GetMapping("/{baiTapID}/chitiet")
    public ResponseEntity<ApiResponse<List<BaiTapChiTietDTO>>> getChiTiet(@PathVariable String baiTapID) {
        return ResponseEntity.ok(ApiResponse.ok(baiTapService.getChiTietBaiTap(baiTapID)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> taoBaiTap(@RequestBody TaoBaiTapRequest request) {
        baiTapService.taoBaiTap(request);
        return ResponseEntity.ok(ApiResponse.ok("Tạo bài tập thành công"));
    }

    @PutMapping("/{baiTapID}")
    public ResponseEntity<ApiResponse<String>> capNhatBaiTap(@PathVariable String baiTapID, @RequestBody TaoBaiTapRequest request) {
        baiTapService.capNhatBaiTap(baiTapID, request);
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật bài tập thành công"));
    }

    @DeleteMapping("/{baiTapID}")
    public ResponseEntity<ApiResponse<String>> xoaBaiTap(@PathVariable String baiTapID) {
        baiTapService.xoaBaiTap(baiTapID);
        return ResponseEntity.ok(ApiResponse.ok("Xóa bài tập thành công"));
    }

    @GetMapping("/lop/{lopID}")
    public ResponseEntity<?> getBaiTapTheoLop(@PathVariable String lopID) {
        try {
            List<com.example.workreport.dto.BaiTapGiangVienDTO> list = baiTapService.getBaiTapByLopDTO(lopID);
            return ResponseEntity.ok(com.example.workreport.dto.ApiResponse.ok(list));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/lop/{lopID}/report")
    public ResponseEntity<?> getReportTheoLop(@PathVariable String lopID) {
        try {
            com.example.workreport.dto.LopReportDTO data = baiTapService.getReportDataByLop(lopID);
            return ResponseEntity.ok(ApiResponse.ok(data));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/cham-diem")
    public ResponseEntity<?> chamDiemBaiNop(@RequestBody ChamDiemRequest request) {
        try {
            baiTapService.chamDiemBaiNop(request);
            return ResponseEntity.ok(ApiResponse.ok("Ghi nhận chấm điểm thành công!"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/sinhvien/lophoc/{sinhVienID}")
    public ResponseEntity<?> getLopHocSinhVien(@PathVariable String sinhVienID) {
        try {
            List<Object[]> rows = baiTapRepository.findLopHocBySinhVienID(sinhVienID);

            List<Map<String, Object>> dsLop = rows.stream().map(row -> {
                Map<String, Object> map = new HashMap<>();
                map.put("lhpID", row[0] != null ? row[0].toString() : "");
                map.put("tenLop", row[1] != null ? row[1].toString() : "");
                map.put("tenMon", row[2] != null ? row[2].toString() : "");
                map.put("tinChi", row[3] != null ? ((Number) row[3]).intValue() : 0);
                return map;
            }).toList();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", dsLop);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/sinhvien/nop-bai")
    public ResponseEntity<?> sinhVienNopBai(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("baiTapID") String baiTapID,
            @RequestParam("sinhVienID") String sinhVienID,
            @RequestParam("hinhThucNop") String hinhThucNop,
            @RequestParam(value = "linkBaiLam", required = false, defaultValue = "") String linkBaiLam
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            String duLieuFileHoacLink = "";

            // 🌟 ĐÃ SỬA CHUẨN CÚ PHÁP: Bỏ dấu ngoặc kép thừa ra ngoài hinhThucNop
            if ("file".equals(hinhThucNop) && file != null && !file.isEmpty()) {

                // 1. Lấy đường dẫn tuyệt đối đến thư mục gốc của dự án (nơi có file pom.xml)
                String projectRoot = System.getProperty("user.dir");

                // 2. Chỉ định chính xác vị trí lưu ở thư mục gốc bên ngoài
                String uploadDir = projectRoot + File.separator + "uploads" + File.separator + "baocao";
                System.out.println("=======> ĐƯỜNG DẪN FILE ĐANG LƯU THỰC TẾ LÀ: " + uploadDir);

                File folder = new File(uploadDir);
                if (!folder.exists()) {
                    folder.mkdirs(); // Tự động tạo thư mục uploads/baocao bên ngoài nếu chưa có
                }

                // 3. Đặt tên file chống trùng
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path copyLocation = Paths.get(uploadDir + File.separator + fileName);

                // Ghi file xuống ổ cứng
                Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

                // 4. Chuỗi lưu xuống Database vẫn giữ nguyên đường dẫn ảo để Web đọc được
                duLieuFileHoacLink = "/uploads/baocao/" + fileName;
            } else {
                duLieuFileHoacLink = linkBaiLam != null ? linkBaiLam.trim() : "";
            }

            // 2. Kiểm tra loại bài tập là Cá nhân hay Nhóm từ DB trước
            String loaiBaiTap = jdbcTemplate.queryForObject(
                    "SELECT Loai FROM BAI_TAP WHERE BaiTapID = ?", String.class, baiTapID
            );

            String finalSinhVienID = null;
            String finalNhomID = null;

            if (loaiBaiTap != null) loaiBaiTap = loaiBaiTap.trim().toUpperCase();
            System.out.println(">>> Nop Bai: loaiBaiTap = " + loaiBaiTap + ", sinhVienID = " + sinhVienID + ", baiTapID = " + baiTapID);

            if (loaiBaiTap.contains("NHAN") || loaiBaiTap.contains("NHÂN")) {
                finalSinhVienID = sinhVienID;
            } else if (loaiBaiTap.contains("NHOM") || loaiBaiTap.contains("NHÓM")) {
                String sqlFindNhom = """
                    SELECT nm.NhomID FROM NHOM_MEMBER nm 
                    JOIN NHOM n ON nm.NhomID = n.NhomID 
                    WHERE UPPER(nm.SinhVienID) = UPPER(?) AND n.BaiTapID = ? LIMIT 1
                """;
                try {
                    finalNhomID = jdbcTemplate.queryForObject(sqlFindNhom, String.class, sinhVienID, baiTapID);
                    System.out.println(">>> Found NhomID: " + finalNhomID);
                } catch (Exception e) {
                    System.out.println(">>> NhomID Error: " + e.getMessage());
                    finalNhomID = null; // Chưa phân nhóm
                }
            }

            // 3. Thực hiện lệnh INSERT tường minh, không lo lệch tham số
            String generateBaoCaoID = "BC" + System.currentTimeMillis();
            System.out.println(">>> Inserting BAO_CAO: BaoCaoID=" + generateBaoCaoID + ", SinhVienID=" + finalSinhVienID + ", NhomID=" + finalNhomID);
            String sqlInsert = """
                INSERT INTO BAO_CAO (BaoCaoID, NgayNop, FileBaoCao, TrangThai, BaiTapID, SinhVienID, NhomID)
                VALUES (?, NOW(), ?, 'Da nop', ?, ?, ?)
            """;

            jdbcTemplate.update(sqlInsert, generateBaoCaoID, duLieuFileHoacLink, baiTapID, finalSinhVienID, finalNhomID);

            response.put("success", true);
            response.put("message", "Nộp bài thành công!");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/lop/{lopID}/sinhvien/{sinhVienID}/report")
    public ResponseEntity<?> getReportTheoLopChoSinhVien(
            @PathVariable("lopID") String lopID,
            @PathVariable("sinhVienID") String sinhVienID
    ) {
        try {
            BaoCaoSinhVienDTO data = baiTapService.getReportDataForSinhVien(lopID, sinhVienID);
            return ResponseEntity.ok(ApiResponse.ok(data));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
    @GetMapping("/lichhoc/tuan/{sinhVienID}")
    public ResponseEntity<Map<String, Object>> getLichHocTrongTuan(@PathVariable String sinhVienID) {
        Map<String, Object> response = new HashMap<>();
        List<com.example.workreport.dto.LichHocTuanDTO> list = lichHocService.getLichHocTuan(sinhVienID);
        response.put("success", true);
        response.put("data", list);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/lichhoc/deadlines/{sinhVienID}")
    public ResponseEntity<Map<String, Object>> getDeadlinesSinhVien(@PathVariable String sinhVienID) {
        Map<String, Object> response = new HashMap<>();
        List<com.example.workreport.dto.BaiTapDeadlineDTO> list = lichHocService.getDeadlinesBySinhVien(sinhVienID);
        response.put("success", true);
        response.put("data", list);
        return ResponseEntity.ok(response);
    }
}