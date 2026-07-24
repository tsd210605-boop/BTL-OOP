package com.example.workreport.repository;

import com.example.workreport.model.BaoCao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BaoCaoRepository extends JpaRepository<BaoCao, String> {
    List<BaoCao> findByBaiTap_BaiTapID(String baiTapID);
    List<BaoCao> findBySinhVien_SinhVienID(String sinhVienID);
    List<BaoCao> findByNhom_NhomID(String nhomID);
    Optional<BaoCao> findByBaiTap_BaiTapIDAndSinhVien_SinhVienID(String baiTapID, String sinhVienID);

    @Query(value = """
        SELECT COUNT(DISTINCT bc.BaoCaoID)
        FROM BAO_CAO bc
        WHERE bc.BaiTapID = :baiTapID
    """, nativeQuery = true)
    int countDaNopByBaiTapID(
            @Param("baiTapID")
            String baiTapID
    );

    @Query(value = """
        SELECT COUNT(DISTINCT bc.BaoCaoID)
        FROM BAO_CAO bc
        JOIN DANH_GIA dg ON dg.BaoCaoID = bc.BaoCaoID
        WHERE bc.BaiTapID = :baiTapID
    """, nativeQuery = true)
    int countDaChamByBaiTapID(
            @Param("baiTapID")
            String baiTapID
    );

    @Query(value = """
        SELECT dg.Diem
        FROM DANH_GIA dg
        JOIN BAO_CAO bc ON dg.BaoCaoID = bc.BaoCaoID
        LEFT JOIN NHOM_MEMBER nm ON bc.NhomID = nm.NhomID
        WHERE bc.BaiTapID = :baiTapID
        AND (
            bc.SinhVienID = :sinhVienID
            OR nm.SinhVienID = :sinhVienID
        )
        LIMIT 1
    """, nativeQuery = true)
    Optional<Double> findDiemCaNhan(
            @Param("baiTapID")
            String baiTapID,

            @Param("sinhVienID")
            String sinhVienID
    );

    @Query(value = """
        SELECT AVG(dg.Diem)
        FROM DANH_GIA dg
        JOIN BAO_CAO bc ON dg.BaoCaoID = bc.BaoCaoID
        WHERE bc.BaiTapID = :baiTapID
    """, nativeQuery = true)
    Optional<Double> avgDiemByBaiTapID(
            @Param("baiTapID")
            String baiTapID
    );

    @Query(value = """
        SELECT dg.Diem
        FROM DANH_GIA dg
        JOIN BAO_CAO bc ON dg.BaoCaoID = bc.BaoCaoID
        LEFT JOIN NHOM_MEMBER nm ON bc.NhomID = nm.NhomID
        WHERE (
            bc.SinhVienID = :sinhVienID
            OR nm.SinhVienID = :sinhVienID
        )
        AND bc.BaiTapID IN (:baiTapIDs)
    """, nativeQuery = true)
    List<Double> findAllDiemBySinhVien(
            @Param("sinhVienID")
            String sinhVienID,

            @Param("baiTapIDs")
            List<String> baiTapIDs
    );

    @Query(value = """
        SELECT COUNT(DISTINCT bc.BaoCaoID)
        FROM BAO_CAO bc
        JOIN DANH_GIA dg ON dg.BaoCaoID = bc.BaoCaoID
        LEFT JOIN NHOM_MEMBER nm ON bc.NhomID = nm.NhomID
        WHERE (
            bc.SinhVienID = :sinhVienID
            OR nm.SinhVienID = :sinhVienID
        )

        AND bc.BaiTapID IN (:baiTapIDs)
    """, nativeQuery = true)
    int countDaChamBySinhVien(
            @Param("sinhVienID")
            String sinhVienID,

            @Param("baiTapIDs")
            List<String> baiTapIDs
    );

    @Query(value = """
        SELECT COUNT(DISTINCT bc.BaoCaoID)
        FROM BAO_CAO bc
        LEFT JOIN NHOM_MEMBER nm ON bc.NhomID = nm.NhomID
        WHERE (
            bc.SinhVienID = :sinhVienID
            OR nm.SinhVienID = :sinhVienID
        )
        AND bc.BaiTapID IN (:baiTapIDs)
    """, nativeQuery = true)
    int countDaNopBySinhVien(
            @Param("sinhVienID")
            String sinhVienID,

            @Param("baiTapIDs")
            List<String> baiTapIDs
    );

    @Query(value = """
        SELECT
            bc.BaoCaoID,
            bt.TenBaiTap,
            DATE_FORMAT(bc.NgayNop, '%d/%m/%Y'),
            dg.TrangThai,
            dg.Diem,
            CASE
                WHEN dg.DanhGiaID IS NOT NULL THEN 'graded'
                WHEN bc.BaoCaoID IS NOT NULL THEN 'submitted'
                ELSE 'missing'
            END
        FROM BAO_CAO bc
        JOIN BAI_TAP bt ON bt.BaiTapID = bc.BaiTapID
        LEFT JOIN DANH_GIA dg ON dg.BaoCaoID = bc.BaoCaoID
        LEFT JOIN NHOM_MEMBER nm ON nm.NhomID = bc.NhomID
        WHERE bc.BaiTapID = :baiTapID
        AND (
            bc.SinhVienID = :sinhVienID
            OR nm.SinhVienID = :sinhVienID
        )
        LIMIT 1
    """, nativeQuery = true)
        List<Object[]> findChiTietBySinhVienAndBaiTap(
                @Param("sinhVienID")
                String sinhVienID,

                @Param("baiTapID")
                String baiTapID
        );
    boolean existsByBaiTap_BaiTapID(
            String baiTapID
    );
}