package com.example.workreport.repository;

import com.example.workreport.model.LopHocSinhVien;
import com.example.workreport.model.LopHocSinhVienKey;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LopHocSinhVienRepository extends JpaRepository<LopHocSinhVien, LopHocSinhVienKey> {
    List<LopHocSinhVien> findBySinhVien_SinhVienID(String sinhVienID);
    List<LopHocSinhVien> findByLopHoc_LopID(String lopID);
    boolean existsBySinhVien_SinhVienIDAndLopHoc_LopID(String sinhVienID, String lopID);
}