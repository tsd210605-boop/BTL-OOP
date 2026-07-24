package com.example.workreport.repository;

import com.example.workreport.model.TietHoc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TietHocRepository extends JpaRepository<TietHoc, String> {
    @Query(value = """
        SELECT
            th.TietHocID AS tietHocID,
            lh.LopID AS lopID,
            lh.TenLop AS tenLop,
            mh.MonHocID AS maMonHoc,
            mh.TenMonHoc AS tenMonHoc,
            u.Fullname AS tenGiangVien,
            th.ThuTrongTuan AS thuTrongTuan,
            th.TietBatDau AS tietBatDau,
            th.SoTiet AS soTiet,
            th.PhongHoc AS phongHoc,
            th.LoaiBuoi AS loaiBuoi
        FROM LOP_HOC_SINH_VIEN lhsv
        JOIN LOP_HOC lh ON lh.LopID = lhsv.LopID
        JOIN TIET_HOC th ON th.LopID = lh.LopID
        JOIN MON_HOC mh ON mh.MonHocID = lh.MonHocID
        JOIN GIANG_VIEN gv ON gv.GiangVienID = lh.GiangVienID
        JOIN USERS u ON u.UserID = gv.UserID
        WHERE lhsv.SinhVienID = :sinhVienID
        ORDER BY
            th.ThuTrongTuan,
            th.TietBatDau
    """, nativeQuery = true)
    List<Object[]> findTietHocBySinhVien(
            @Param("sinhVienID")
            String sinhVienID
    );
}