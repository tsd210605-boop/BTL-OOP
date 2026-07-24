package com.example.workreport.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.workreport.dto.ApiResponse;
import com.example.workreport.model.MonHoc;
import com.example.workreport.repository.MonHocRepository;

@RestController
@RequestMapping("/api/monhoc")
public class MonHocController {

    private final MonHocRepository repository;

    public MonHocController(MonHocRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MonHoc>>> getAllMonHoc() {
        return ResponseEntity.ok(ApiResponse.ok(repository.findAll()));
    }
}