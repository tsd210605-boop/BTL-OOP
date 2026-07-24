package com.example.workreport.controller;

import com.example.workreport.dto.ApiResponse;
import com.example.workreport.repository.NhomRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class NhomController {
    private final NhomRepository nhomRepository;

    public NhomController(NhomRepository nhomRepository) {
        this.nhomRepository = nhomRepository;
    }

    @GetMapping("/sinhvien/{sinhVienID}/nhom")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getNhomBySinhVien(@PathVariable String sinhVienID) {
        List<Object[]> results = nhomRepository.findNhomBySinhVienID(sinhVienID);
        List<Map<String, Object>> nhoms = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> nhom = new HashMap<>();
            nhom.put("nhomID", row[0]);
            nhom.put("tenNhom", row[1]);
            nhom.put("truongNhom", row[2]);
            nhom.put("lopID", row[3]);
            nhoms.add(nhom);
        }
        return ResponseEntity.ok(ApiResponse.ok(nhoms));
    }
}
