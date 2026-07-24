package com.example.workreport.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import com.example.workreport.dto.ApiResponse;

import java.util.*;

@RestController
@RequestMapping("/api")
public class MetaController {
    @GetMapping("/hoc-ky")
    public ResponseEntity<ApiResponse<List<Map<String, String>>>> getHocKy() {
        List<Map<String, String>> data = List.of(
            Map.of("ma", "HK1-2025-2026", "ten", "Học kỳ I · 2025–2026"),
            Map.of("ma", "HK2-2025-2026", "ten", "Học kỳ II · 2025–2026"),
            Map.of("ma", "HK1-2024-2025", "ten", "Học kỳ I · 2024–2025"),
            Map.of("ma", "HK2-2024-2025", "ten", "Học kỳ II · 2024–2025")
        );
        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    @GetMapping("/mon-hoc")
    public ResponseEntity<ApiResponse<List<Map<String, String>>>> getMonHoc() {
        List<Map<String, String>> data = List.of(
            Map.of("ma", "CNPM", "ten", "Công nghệ phần mềm"),
            Map.of("ma", "CSDL", "ten", "Cơ sở dữ liệu"),
            Map.of("ma", "MANG", "ten", "Mạng máy tính")
        );
        return ResponseEntity.ok(ApiResponse.ok(data));
    }
}