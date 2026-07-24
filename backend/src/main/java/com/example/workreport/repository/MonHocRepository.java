package com.example.workreport.repository;

import com.example.workreport.model.MonHoc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MonHocRepository extends JpaRepository<MonHoc, String> {
    Optional<MonHoc> findByTenMonHoc(String tenMonHoc);
    List<MonHoc> findByTenMonHocContainingIgnoreCase(String keyword);
    boolean existsByTenMonHoc(String tenMonHoc);
}
