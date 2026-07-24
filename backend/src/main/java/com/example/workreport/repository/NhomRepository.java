package com.example.workreport.repository;

import com.example.workreport.model.Nhom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NhomRepository extends JpaRepository<Nhom, String> {
    List<Nhom> findByBaiTap_BaiTapID(String baiTapID);
    List<Nhom> findByTruongNhom_SinhVienID(String sinhVienID);
    @Query(value = """
        SELECT
            n.NhomID,
            n.TenNhom,
            sv.SinhVienID AS truongNhomID,
            u.Fullname AS truongNhomTen,
            bt.BaiTapID,
            bt.TenBaiTap,
            lh.LopID,
            lh.TenLop
        FROM NHOM n
        JOIN NHOM_MEMBER nm ON n.NhomID = nm.NhomID
        JOIN SINH_VIEN sv ON sv.SinhVienID = n.TruongNhom
        JOIN USERS u ON u.UserID = sv.UserID
        JOIN BAI_TAP bt ON bt.BaiTapID = n.BaiTapID
        JOIN LOP_HOC lh ON lh.LopID = bt.LopID
        WHERE nm.SinhVienID = :sinhVienID
    """, nativeQuery = true)
    List<Object[]> findNhomBySinhVienID(
            @Param("sinhVienID") String sinhVienID
    );

    @Query(value = """
        SELECT COUNT(*)
        FROM NHOM_MEMBER
        WHERE NhomID = :nhomID
          AND SinhVienID = :sinhVienID
    """, nativeQuery = true)
    int existsMemberInNhom(
            @Param("nhomID") String nhomID,
            @Param("sinhVienID") String sinhVienID
    );

    @Query(value = """
        SELECT n.*
        FROM NHOM n
        JOIN NHOM_MEMBER nm ON n.NhomID = nm.NhomID
        WHERE n.BaiTapID = :baiTapID
          AND nm.SinhVienID = :sinhVienID
        LIMIT 1
    """, nativeQuery = true)
    Optional<Nhom> findNhomByBaiTapAndSinhVien(
            @Param("baiTapID") String baiTapID,
            @Param("sinhVienID") String sinhVienID
    );
}
