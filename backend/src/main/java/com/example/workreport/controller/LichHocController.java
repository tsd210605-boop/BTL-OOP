package com.example.workreport.controller;

import com.example.workreport.dto.LichHocTuanDTO;
import com.example.workreport.service.LichHocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api") // 🌟 QUAY LẠI GỐC CHUẨN: Đón đầu chính xác luồng định tuyến an toàn
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class LichHocController {

    @Autowired
    private LichHocService lichHocService;

    // 🚀 Khớp chuẩn 100% URL: /api/lichhoc/tuan/{sinhVienID}
    @GetMapping("/lichhoc/tuan/{sinhVienID}")
    public ResponseEntity<Map<String, Object>> getLichHocTrongTuan(@PathVariable String sinhVienID) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<LichHocTuanDTO> list = lichHocService.getLichHocTuan(sinhVienID);
            response.put("success", true);
            response.put("data", list);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/lichhoc/deadline/{sinhVienID}")
    public ResponseEntity<Map<String, Object>> getDeadline(
            @PathVariable String sinhVienID
    ) {

        Map<String, Object> response = new HashMap<>();

        try {

            response.put("success", true);

            response.put(
                    "data",
                    lichHocService.getDeadlinesBySinhVien(sinhVienID)
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            response.put("success", false);

            response.put("message", e.getMessage());

            return ResponseEntity.status(500).body(response);
        }
    }
}