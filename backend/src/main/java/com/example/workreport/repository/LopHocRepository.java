package com.example.workreport.repository;

import com.example.workreport.model.LopHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LopHocRepository extends JpaRepository<LopHoc, String> {
    List<LopHoc> findByGiangVien_GiangVienID(String giangVienID);
    List<LopHoc> findByMonHoc_MonHocID(String monHocID);
    List<LopHoc> findByHocKy_HocKyID(String hocKyID);
    List<LopHoc> findByTenLopContainingIgnoreCase(String keyword);
}
