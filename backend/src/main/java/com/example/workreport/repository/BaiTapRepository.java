package com.example.workreport.repository;

import com.example.workreport.model.BaiTap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BaiTapRepository
        extends JpaRepository<BaiTap, String> {

    List<BaiTap> findByLopHoc_LopID(String lopID);

    List<BaiTap> findByLopHoc_GiangVien_GiangVienID(String giangVienID);

    @Query(value = """
    SELECT
        bt.BaiTapID,                                                -- 0
        bt.TenBaiTap,                                               -- 1
        bt.MoTa,                                                    -- 2
        DATE_FORMAT(bt.Deadline, '%d/%m/%Y %H:%i:%s') AS deadline, -- 3
        CASE WHEN bt.Deadline >= NOW() THEN 'active' ELSE 'closed' END AS trangThai, -- 4
        bt.Loai,                                                    -- 5
        COALESCE(dg.Diem, 0.0) AS diemTB,                           -- 6
        CASE WHEN bc.BaoCaoID IS NOT NULL THEN 1 ELSE 0 END AS soNopBai, -- 7
        (SELECT COUNT(*) FROM LOP_HOC_SINH_VIEN WHERE LopID = bt.LopID) AS tongSinhVien, -- 8
        CASE WHEN dg.DanhGiaID IS NOT NULL THEN 1 ELSE 0 END AS soChamBai, -- 9
        CASE 
            WHEN dg.DanhGiaID IS NOT NULL THEN 'Da cham'
            WHEN bc.BaoCaoID IS NOT NULL THEN 'Da nop'
            ELSE 'Chua nop'
        END AS trangThaiNop,                                        -- 10
        bt.LopID,                                                   -- 11
        dg.NhanXet,                                                 -- 12
        bc.FileBaoCao                                               -- 13
    FROM BAI_TAP bt
    JOIN LOP_HOC_SINH_VIEN lhsv ON bt.LopID = lhsv.LopID
    LEFT JOIN (
        SELECT outer_bc.* FROM BAO_CAO outer_bc
        WHERE outer_bc.NgayNop = (
            SELECT MAX(inner_bc.NgayNop) FROM BAO_CAO inner_bc
            WHERE inner_bc.BaiTapID = outer_bc.BaiTapID
              AND (COALESCE(inner_bc.SinhVienID, '') = COALESCE(outer_bc.SinhVienID, '') AND COALESCE(inner_bc.NhomID, '') = COALESCE(outer_bc.NhomID, ''))
        )
    ) bc ON bt.BaiTapID = bc.BaiTapID
        AND (
            (UPPER(bt.Loai) = 'CA NHAN' AND UPPER(bc.SinhVienID) = UPPER(:sinhVienID))
            OR 
            (UPPER(bt.Loai) = 'NHOM' AND bc.NhomID = (
                SELECT nm.NhomID FROM NHOM_MEMBER nm 
                JOIN NHOM n ON nm.NhomID = n.NhomID
                WHERE UPPER(nm.SinhVienID) = UPPER(:sinhVienID) AND n.BaiTapID = bt.BaiTapID 
                LIMIT 1
            ))
        )
    LEFT JOIN DANH_GIA dg ON bc.BaoCaoID = dg.BaoCaoID
    WHERE UPPER(lhsv.SinhVienID) = UPPER(:sinhVienID)
    ORDER BY bt.Deadline ASC
""", nativeQuery = true)
    List<Object[]> findBaiTapBySinhVienIDNative(@Param("sinhVienID") String sinhVienID);

    @Query(value = """
        SELECT
            sv.SinhVienID,                                      -- index 0
            u.Fullname,                                         -- index 1
            sv.Lop,                                             -- index 2
            COALESCE(n.TenNhom, 'Chưa vào nhóm') AS tenNhom,     -- index 3
            CASE
                WHEN dg.DanhGiaID IS NOT NULL THEN 'Da cham'
                ELSE 'Chua cham'
            END AS trangThaiCham,                               -- index 4
            dg.Diem,                                            -- index 5
            bc.FileBaoCao,                                      -- index 6
            DATE_FORMAT(bc.NgayNop, '%d/%m/%Y %H:%i:%s') AS ngayNop, -- index 7
            CASE 
                WHEN bc.BaoCaoID IS NOT NULL THEN 'Da nop'
                ELSE 'Chua nop'
            END AS trangThaiNop,                                -- index 8
            bt.TenBaiTap,                                       -- index 9
            bt.MoTa,                                            -- index 10
            dg.NhanXet AS nhanXet                               -- index 11
        FROM BAI_TAP bt
        JOIN LOP_HOC_SINH_VIEN lhsv ON bt.LopID = lhsv.LopID
        JOIN SINH_VIEN sv ON lhsv.SinhVienID = sv.SinhVienID
        JOIN USERS u ON sv.UserID = u.UserID
        LEFT JOIN NHOM_MEMBER nm ON nm.SinhVienID = sv.SinhVienID
        LEFT JOIN NHOM n ON n.NhomID = nm.NhomID AND n.BaiTapID = bt.BaiTapID
        
        LEFT JOIN (
            SELECT outer_bc.* FROM BAO_CAO outer_bc
            WHERE outer_bc.NgayNop = (
                SELECT MAX(inner_bc.NgayNop) 
                FROM BAO_CAO inner_bc 
                WHERE inner_bc.BaiTapID = outer_bc.BaiTapID 
                  AND (
                       (inner_bc.SinhVienID IS NOT NULL AND inner_bc.SinhVienID = outer_bc.SinhVienID) 
                       OR (inner_bc.NhomID IS NOT NULL AND inner_bc.NhomID = outer_bc.NhomID)
                  )
            )
        ) bc ON bt.BaiTapID = bc.BaiTapID
            AND (
                (UPPER(bt.Loai) = 'CA NHAN' AND UPPER(bc.SinhVienID) = UPPER(sv.SinhVienID))
                OR (UPPER(bt.Loai) = 'NHOM' AND UPPER(bc.NhomID) = UPPER(n.NhomID))
            )
            
        LEFT JOIN DANH_GIA dg ON dg.BaoCaoID = bc.BaoCaoID
        WHERE bt.BaiTapID = :baiTapID
        GROUP BY
            sv.SinhVienID, u.Fullname, sv.Lop, n.TenNhom,
            bc.BaoCaoID, dg.DanhGiaID, bt.TenBaiTap, bt.MoTa, bc.FileBaoCao, bc.NgayNop, dg.NhanXet, dg.Diem
        ORDER BY n.TenNhom ASC, u.Fullname ASC
    """, nativeQuery = true)
    List<Object[]> findChiTietBaiTap(@Param("baiTapID") String baiTapID);

    @Query(value = """
        SELECT DISTINCT bt.*
        FROM BAI_TAP bt
        JOIN LOP_HOC lh ON bt.LopID = lh.LopID
        JOIN LOP_HOC_SINH_VIEN lhsv ON lh.LopID = lhsv.LopID
        WHERE lhsv.SinhVienID = :sinhVienID
        ORDER BY bt.Deadline ASC
    """, nativeQuery = true)
    List<BaiTap> findByLopOfSinhVien(
            @Param("sinhVienID")
            String sinhVienID
    );

    @Query(value = """
        SELECT DISTINCT bt.*
        FROM BAI_TAP bt
        JOIN LOP_HOC lh ON bt.LopID = lh.LopID
        JOIN LOP_HOC_SINH_VIEN lhsv ON lh.LopID = lhsv.LopID
        WHERE lhsv.SinhVienID = :sinhVienID AND lh.MonHocID = :monHocID
        ORDER BY bt.Deadline ASC
    """, nativeQuery = true)
    List<BaiTap> findByLopOfSinhVienAndMonHoc(
            @Param("sinhVienID")
            String sinhVienID,

            @Param("monHocID")
            String monHocID
    );

    @Query(value = """
        SELECT COUNT(DISTINCT lhsv.SinhVienID)
        FROM BAI_TAP bt
        JOIN LOP_HOC_SINH_VIEN lhsv ON bt.LopID = lhsv.LopID
        WHERE bt.BaiTapID = :baiTapID
    """, nativeQuery = true)
    int countSinhVienByBaiTapID(
            @Param("baiTapID")
            String baiTapID
    );

    @Query(value = """
        SELECT 
            bt.BaiTapID,                                               -- index 0 (String)
            bt.TenBaiTap,                                              -- index 1 (String)
            bt.MoTa,                                                   -- index 2 (String)
            bt.Loai,                                                   -- index 3 (String)
            DATE_FORMAT(bt.Deadline, '%d/%m/%Y') AS deadline,          -- index 4 (String)
            CASE WHEN bt.Deadline >= NOW() THEN 'Dang hoat dong' ELSE 'Da qua han' END AS trangThai, -- index 5 (String)
            bt.DiemToiDa,                                              -- index 6 (Double/Number)
            
            COALESCE(thong_ke.so_da_nop, 0) AS daNop,                  -- index 7 (Number)
            COALESCE(thong_ke.so_cho_cham, 0) AS choCham,              -- index 8 (Number)
            COALESCE(thong_ke.diem_tb, 0.0) AS diemTrungBinh,          -- index 9 (Number)
            
            CASE 
                WHEN bt.Loai = 'Nhom' THEN (SELECT COUNT(*) FROM NHOM n WHERE n.BaiTapID = bt.BaiTapID)
                ELSE (SELECT COUNT(*) FROM LOP_HOC_SINH_VIEN lhsv WHERE lhsv.LopID = bt.LopID)
            END AS tongMucCanThu                                       -- index 10 (Number)
            
        FROM BAI_TAP bt
        JOIN LOP_HOC lh ON bt.LopID = lh.LopID
        
        LEFT JOIN (
            SELECT 
                bc_outer.BaiTapID,
                COUNT(DISTINCT CASE WHEN bc_outer.SinhVienID IS NOT NULL THEN bc_outer.SinhVienID ELSE bc_outer.NhomID END) AS so_da_nop,
                
                COUNT(DISTINCT CASE 
                    WHEN dg.DanhGiaID IS NULL THEN (CASE WHEN bc_outer.SinhVienID IS NOT NULL THEN bc_outer.SinhVienID ELSE bc_outer.NhomID END)
                END) AS so_cho_cham,
                AVG(dg.Diem) AS diem_tb
            FROM BAO_CAO bc_outer
            LEFT JOIN DANH_GIA dg ON bc_outer.BaoCaoID = dg.BaoCaoID
            WHERE bc_outer.NgayNop = (
                SELECT MAX(bc_inner.NgayNop)
                FROM BAO_CAO bc_inner
                WHERE bc_inner.BaiTapID = bc_outer.BaiTapID
                  AND (
                      (bc_inner.SinhVienID IS NOT NULL AND bc_inner.SinhVienID = bc_outer.SinhVienID)
                      OR
                      (bc_inner.NhomID IS NOT NULL AND bc_inner.NhomID = bc_outer.NhomID)
                  )
            )
            GROUP BY bc_outer.BaiTapID
        ) thong_ke ON bt.BaiTapID = thong_ke.BaiTapID
        
        WHERE lh.GiangVienID = :giangVienID
        GROUP BY bt.BaiTapID, bt.TenBaiTap, bt.MoTa, bt.Loai, bt.Deadline, bt.DiemToiDa, bt.LopID, 
                 thong_ke.so_da_nop, thong_ke.so_cho_cham, thong_ke.diem_tb
        ORDER BY bt.BaiTapID DESC
    """, nativeQuery = true)
    List<Object[]> findBaiTapByGiangVienID(@Param("giangVienID") String giangVienID);

    @Query(value = """
        SELECT
            bt.BaiTapID,
            bt.TenBaiTap,
            COALESCE(mh.TenMonHoc, bt.Loai) AS monHoc,
            bt.Loai,
            bt.Deadline,
            COALESCE(ts.TrangThai,'missing') AS trangThai
        FROM BAI_TAP bt
        JOIN LOP_HOC_SINH_VIEN lhsv ON bt.LopID = lhsv.LopID
        LEFT JOIN LOP_HOC lh ON bt.LopID = lh.LopID
        LEFT JOIN MON_HOC mh ON lh.MonHocID = mh.MonHocID
        LEFT JOIN TRANG_THAI_BAITAP_SV ts ON ts.BaiTapID = bt.BaiTapID AND ts.SinhVienID = :sinhVienID
        WHERE lhsv.SinhVienID = :sinhVienID AND bt.Deadline IS NOT NULL
        ORDER BY bt.Deadline ASC
    """, nativeQuery = true)
    List<Object[]> findDeadlinesBySinhVien(
            @Param("sinhVienID")
            String sinhVienID
    );

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO TRANG_THAI_BAITAP_SV
        (
            SinhVienID,
            BaiTapID,
            TrangThai,
            NgayCapNhat
        )
        VALUES
        (
            :sinhVienID,
            :baiTapID,
            :trangThai,
            CURDATE()
        )
        ON DUPLICATE KEY UPDATE
            TrangThai = :trangThai,
            NgayCapNhat = CURDATE()
    """, nativeQuery = true)
    void upsertTrangThaiSV(
            @Param("sinhVienID")
            String sinhVienID,

            @Param("baiTapID")
            String baiTapID,

            @Param("trangThai")
            String trangThai
    );

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO TRANG_THAI_BAITAP_SV
        (
            SinhVienID,
            BaiTapID,
            TrangThai,
            NgayCapNhat
        )
        VALUES
        (
            :sinhVienID,
            :baiTapID,
            :trangThai,
            CURDATE()
        )
        ON DUPLICATE KEY UPDATE
            TrangThai =
                IF(
                    TrangThai = 'missing',
                    :trangThai,
                    TrangThai
                ),
            NgayCapNhat = CURDATE()
    """, nativeQuery = true)
    void upsertTrangThaiIfMissing(
            @Param("sinhVienID")
            String sinhVienID,

            @Param("baiTapID")
            String baiTapID,

            @Param("trangThai")
            String trangThai
    );

    @Query(value = """
        SELECT 
            bt.BaiTapID,                                                                      -- row[0]
            bt.TenBaiTap,                                                                     -- row[1]
            bt.MoTa,                                                                          -- row[2]
            CASE 
                WHEN dg.DanhGiaID IS NOT NULL THEN 'Da cham'
                WHEN bc.BaoCaoID IS NOT NULL THEN 'Da nop'
                ELSE 'Chua nop'
            END AS trangThaiNop,                                                              -- row[3]
            bc.FileBaoCao,                                                                    -- row[4]
            DATE_FORMAT(bc.NgayNop, '%H:%i - %d/%m/%Y') AS ngayNop,                           -- row[5]
            '' AS nhanXet,                                                                    -- row[6]
            dg.Diem                                                                           -- row[7]
        FROM BAI_TAP bt
        JOIN LOP_HOC lh ON bt.LopID = lh.LopID
        JOIN LOP_HOC_SINH_VIEN lhsv ON bt.LopID = lhsv.LopID
        LEFT JOIN NHOM_MEMBER nm ON nm.SinhVienID = lhsv.SinhVienID
        LEFT JOIN NHOM n ON n.NhomID = nm.NhomID AND n.BaiTapID = bt.BaiTapID
        LEFT JOIN BAO_CAO bc ON bt.BaiTapID = bc.BaiTapID 
            AND (
                (UPPER(bt.Loai) = 'CA NHAN' AND UPPER(bc.SinhVienID) = UPPER(lhsv.SinhVienID))
                OR (UPPER(bt.Loai) = 'NHOM' AND UPPER(bc.NhomID) = UPPER(n.NhomID))
            )
        LEFT JOIN DANH_GIA dg ON dg.BaoCaoID = bc.BaoCaoID
        WHERE lhsv.SinhVienID = :sinhVienID 
          AND lh.GiangVienID = :giangVienID
        GROUP BY bt.BaiTapID, bt.TenBaiTap, bt.MoTa, bc.BaoCaoID, dg.DanhGiaID, bc.FileBaoCao, bc.NgayNop, dg.Diem
        ORDER BY bt.Deadline DESC
    """, nativeQuery = true)
    List<Object[]> findBaiTapTienDoBySinhVien(
            @Param("sinhVienID") String sinhVienID,
            @Param("giangVienID") String giangVienID
    );

    @Query(value = """
        SELECT 
            bt.BaiTapID,                                                                      
            bt.TenBaiTap,                                                                     
            bt.MoTa,                                                                          
            CASE 
                WHEN dg.DanhGiaID IS NOT NULL THEN 'Da cham'
                WHEN bc.BaoCaoID IS NOT NULL THEN 'Da nop'
                ELSE 'Chua nop'
            END AS trangThaiNop,                                                              
            bc.FileBaoCao,                                                                    
            DATE_FORMAT(bc.NgayNop, '%H:%i - %d/%m/%Y') AS ngayNop,                           
            '' AS nhanXet,                                                                    
            dg.Diem                                                                           
        FROM BAI_TAP bt
        JOIN LOP_HOC_SINH_VIEN lhsv ON bt.LopID = lhsv.LopID
        LEFT JOIN NHOM_MEMBER nm ON nm.SinhVienID = lhsv.SinhVienID
        LEFT JOIN NHOM n ON n.NhomID = nm.NhomID AND n.BaiTapID = bt.BaiTapID
        LEFT JOIN BAO_CAO bc ON bt.BaiTapID = bc.BaiTapID 
            AND (
                (UPPER(bt.Loai) = 'CA NHAN' AND UPPER(bc.SinhVienID) = UPPER(lhsv.SinhVienID))
                OR (UPPER(bt.Loai) = 'NHOM' AND UPPER(bc.NhomID) = UPPER(n.NhomID))
            )
        LEFT JOIN DANH_GIA dg ON dg.BaoCaoID = bc.BaoCaoID
        WHERE lhsv.LopID = :lopID 
          AND lhsv.SinhVienID = :sinhVienID
        GROUP BY bt.BaiTapID, bt.TenBaiTap, bt.MoTa, bc.BaoCaoID, dg.DanhGiaID, bc.FileBaoCao, bc.NgayNop, dg.Diem
        ORDER BY bt.Deadline DESC
    """, nativeQuery = true)
    List<Object[]> findBaiTapByLopAndSinhVien(
            @Param("lopID") String lopID,
            @Param("sinhVienID") String sinhVienID
    );

    @Query(value = """
        SELECT 
            bt.BaiTapID, 
            bt.TenBaiTap, 
            bt.MoTa, 
            DATE_FORMAT(bt.Deadline, '%d/%m/%Y') AS deadline,          
            
            -- Đếm chuẩn số lượng đối tượng duy nhất (Mã SV / Mã Nhóm) đã nộp bài cuối cùng
            COALESCE(thong_ke.so_da_nop, 0) AS daNop,                      
            
            -- Đếm chuẩn số lượng đối tượng thực sự đang chờ chấm bài
            COALESCE(thong_ke.so_cho_cham, 0) AS choCham, 
            
            CASE 
                WHEN bt.Loai = 'Nhom' THEN (SELECT COUNT(*) FROM NHOM n WHERE n.BaiTapID = bt.BaiTapID)
                ELSE (SELECT COUNT(*) FROM LOP_HOC_SINH_VIEN lhsv WHERE lhsv.LopID = bt.LopID)
            END AS tongCanThu                                          
        FROM BAI_TAP bt
        
        -- Tích hợp Sub-query lọc sạch bản nộp lại ngay từ gốc
        LEFT JOIN (
            SELECT 
                b1.BaiTapID,
                COUNT(DISTINCT CASE WHEN b1.SinhVienID IS NOT NULL THEN b1.SinhVienID ELSE b1.NhomID END) AS so_da_nop,
                COUNT(DISTINCT CASE WHEN dg.DanhGiaID IS NULL THEN (CASE WHEN b1.SinhVienID IS NOT NULL THEN b1.SinhVienID ELSE b1.NhomID END) END) AS so_cho_cham
            FROM BAO_CAO b1
            LEFT JOIN DANH_GIA dg ON b1.BaoCaoID = dg.BaoCaoID
            WHERE b1.NgayNop = (
                SELECT MAX(b2.NgayNop) FROM BAO_CAO b2 
                WHERE b2.BaiTapID = b1.BaiTapID 
                  AND (COALESCE(b2.SinhVienID,'') = COALESCE(b1.SinhVienID,'') AND COALESCE(b2.NhomID,'') = COALESCE(b1.NhomID,''))
            )
            GROUP BY b1.BaiTapID
        ) thong_ke ON bt.BaiTapID = thong_ke.BaiTapID
        
        WHERE bt.LopID = :lopID
        GROUP BY bt.BaiTapID, bt.TenBaiTap, bt.MoTa, bt.Deadline, bt.Loai, bt.LopID, thong_ke.so_da_nop, thong_ke.so_cho_cham
        ORDER BY bt.Deadline DESC
    """, nativeQuery = true)
    List<Object[]> findAssignmentsByLop(@Param("lopID") String lopID);

    @Query(value = """
        SELECT 
            u.Fullname AS sinhVienTen, 
            bt.TenBaiTap, 
            DATE_FORMAT(bc.NgayNop, '%H:%i:%s %d/%m/%Y') AS thoiGian,  
            CASE 
                WHEN dg.DanhGiaID IS NOT NULL THEN 'graded'
                WHEN bt.Deadline < bc.NgayNop THEN 'late'
                ELSE 'pending'
            END AS trangThaiCode, 
            dg.Diem                                                    
        FROM BAO_CAO bc
        JOIN BAI_TAP bt ON bc.BaiTapID = bt.BaiTapID
        LEFT JOIN SINH_VIEN sv ON bc.SinhVienID = sv.SinhVienID
        LEFT JOIN USERS u ON sv.UserID = u.UserID
        LEFT JOIN DANH_GIA dg ON dg.BaoCaoID = bc.BaoCaoID
        
        WHERE bt.LopID = :lopID
          AND bc.NgayNop = (
              SELECT MAX(inner_bc.NgayNop) FROM BAO_CAO inner_bc
              WHERE inner_bc.BaiTapID = bc.BaiTapID
                AND (COALESCE(inner_bc.SinhVienID, '') = COALESCE(bc.SinhVienID, '') AND COALESCE(inner_bc.NhomID, '') = COALESCE(bc.NhomID, ''))
          )
        ORDER BY bc.NgayNop DESC
        LIMIT 5
    """, nativeQuery = true)
    List<Object[]> findRecentSubmissionsByLop(@Param("lopID") String lopID);

    @Query(value = """
        SELECT 
            bt.BaiTapID AS baiTapID, bt.TenBaiTap AS tenBaiTap, bt.MoTa AS moTa, bt.Loai AS loai,                                                   
            DATE_FORMAT(bt.Deadline, '%d/%m/%Y %H:%i') AS deadline,    
            CASE WHEN bt.Deadline >= NOW() THEN 'Dang hoat dong' ELSE 'Da qua han' END AS trangThai,
            bt.DiemToiDa AS diemToiDa,                                              
            COALESCE(thong_ke.so_da_nop, 0) AS daNop,                      
            COALESCE(thong_ke.so_cho_cham, 0) AS choCham,              
            COALESCE(thong_ke.diem_tb, 0.0) AS diemTrungBinh,                             
            CASE 
                WHEN bt.Loai = 'Nhom' THEN (SELECT COUNT(*) FROM NHOM n WHERE n.BaiTapID = bt.BaiTapID)
                ELSE (SELECT COUNT(*) FROM LOP_HOC_SINH_VIEN lhsv WHERE lhsv.LopID = bt.LopID)
            END AS tongMucCanThu                                       
        FROM BAI_TAP bt
        LEFT JOIN (
            SELECT 
                bc_outer.BaiTapID,
                COUNT(DISTINCT CASE WHEN bc_outer.SinhVienID IS NOT NULL THEN bc_outer.SinhVienID ELSE bc_outer.NhomID END) AS so_da_nop,
                COUNT(DISTINCT CASE 
                    WHEN dg.DanhGiaID IS NULL THEN (CASE WHEN bc_outer.SinhVienID IS NOT NULL THEN bc_outer.SinhVienID ELSE bc_outer.NhomID END)
                END) AS so_cho_cham,
                AVG(dg.Diem) AS diem_tb
            FROM BAO_CAO bc_outer
            LEFT JOIN DANH_GIA dg ON bc_outer.BaoCaoID = dg.BaoCaoID 
            WHERE bc_outer.NgayNop = (
                SELECT MAX(bc_inner.NgayNop)
                FROM BAO_CAO bc_inner
                WHERE bc_inner.BaiTapID = bc_outer.BaiTapID
                  AND (
                      (bc_inner.SinhVienID IS NOT NULL AND bc_inner.SinhVienID = bc_outer.SinhVienID)
                      OR
                      (bc_inner.NhomID IS NOT NULL AND bc_inner.NhomID = bc_outer.NhomID)
                  )
            )
            GROUP BY bc_outer.BaiTapID
        ) thong_ke ON bt.BaiTapID = thong_ke.BaiTapID
        WHERE bt.LopID = :lopID
        GROUP BY bt.BaiTapID, bt.TenBaiTap, bt.MoTa, bt.Loai, bt.Deadline, bt.DiemToiDa, bt.LopID,
                 thong_ke.so_da_nop, thong_ke.so_cho_cham, thong_ke.diem_tb
        ORDER BY bt.Deadline DESC
    """, nativeQuery = true)
    List<Object[]> findBaiTapByLopIDNative(@Param("lopID") String lopID);

    @Query(value = """
        SELECT 
            COALESCE(AVG(dg.Diem), 0) AS diemTBChung,
            (SELECT COUNT(*) FROM BAI_TAP WHERE LopID = :lopID) AS tongBaiTap,
            (SELECT COUNT(*) FROM LOP_HOC_SINH_VIEN WHERE LopID = :lopID) AS tongSinhVien,
            
            CASE 
                WHEN (SELECT COUNT(*) FROM BAI_TAP WHERE LopID = :lopID) = 0 THEN 0
                WHEN (SELECT COUNT(*) FROM LOP_HOC_SINH_VIEN WHERE LopID = :lopID) = 0 THEN 0
                ELSE ROUND((
                    SELECT COUNT(DISTINCT CASE WHEN b1.SinhVienID IS NOT NULL THEN b1.SinhVienID ELSE b1.NhomID END) 
                    FROM BAO_CAO b1 
                    JOIN BAI_TAP bt2 ON b1.BaiTapID = bt2.BaiTapID 
                    WHERE bt2.LopID = :lopID
                ) / ((SELECT COUNT(*) FROM BAI_TAP WHERE LopID = :lopID) * (SELECT COUNT(*) FROM LOP_HOC_SINH_VIEN WHERE LopID = :lopID)) * 100)
            END AS tyLeHoanThanh
        FROM LOP_HOC_SINH_VIEN lhsv
        LEFT JOIN BAI_TAP bt ON bt.LopID = lhsv.LopID
        
        -- Lọc sạch chỉ lấy bản nộp cuối cùng để tính điểm trung bình chung chính xác
        LEFT JOIN (
            SELECT outer_bc.* FROM BAO_CAO outer_bc
            WHERE outer_bc.NgayNop = (
                SELECT MAX(inner_bc.NgayNop) FROM BAO_CAO inner_bc
                WHERE inner_bc.BaiTapID = outer_bc.BaiTapID
                  AND (COALESCE(inner_bc.SinhVienID, '') = COALESCE(outer_bc.SinhVienID, '') AND COALESCE(inner_bc.NhomID, '') = COALESCE(outer_bc.NhomID, ''))
            )
        ) bc ON bc.BaiTapID = bt.BaiTapID
        LEFT JOIN DANH_GIA dg ON dg.BaoCaoID = bc.BaoCaoID
        WHERE lhsv.LopID = :lopID
    """, nativeQuery = true)
    List<Object[]> findReportStatsByLop(@Param("lopID") String lopID);

    @Query(value = """
        SELECT 
            sv.SinhVienID,
            u.Fullname,
            sv.Lop,
            COALESCE(AVG(diem_bai_tap.Diem), 0) AS diemTrungBinh,
            COUNT(DISTINCT CASE WHEN diem_bai_tap.Diem IS NOT NULL THEN diem_bai_tap.BaiTapID END) AS soBaiDaCham
        FROM LOP_HOC_SINH_VIEN lhsv
        JOIN SINH_VIEN sv ON lhsv.SinhVienID = sv.SinhVienID
        JOIN USERS u ON sv.UserID = u.UserID
        LEFT JOIN (
            SELECT 
                bt.BaiTapID,
                bt.Loai,
                CASE WHEN bt.Loai = 'Nhom' THEN nm.SinhVienID ELSE bc.SinhVienID END AS SinhVienID,
                dg.Diem
            FROM BAI_TAP bt
            JOIN BAO_CAO bc ON bc.BaiTapID = bt.BaiTapID
            JOIN DANH_GIA dg ON dg.BaoCaoID = bc.BaoCaoID
            LEFT JOIN NHOM_MEMBER nm ON nm.NhomID = bc.NhomID
            WHERE bt.LopID = :lopID
        ) AS diem_bai_tap ON diem_bai_tap.SinhVienID = sv.SinhVienID
        WHERE lhsv.LopID = :lopID
        GROUP BY sv.SinhVienID, u.Fullname, sv.Lop
        ORDER BY diemTrungBinh DESC, soBaiDaCham DESC
        LIMIT 3
    """, nativeQuery = true)
    List<Object[]> findTopStudentsByLop(@Param("lopID") String lopID);

    @Query(value = """
        SELECT bc.BaoCaoID 
        FROM BAO_CAO bc
        JOIN BAI_TAP bt ON bc.BaiTapID = bt.BaiTapID
        LEFT JOIN NHOM_MEMBER nm ON nm.NhomID = bc.NhomID
        WHERE bt.BaiTapID = :baiTapID 
          AND (UPPER(bc.SinhVienID) = UPPER(:sinhVienID) OR UPPER(nm.SinhVienID) = UPPER(:sinhVienID))
        ORDER BY bc.NgayNop DESC
        LIMIT 1
    """, nativeQuery = true)
    String findBaoCaoIdByTaiKhoan(@Param("baiTapID") String baiTapID, @Param("sinhVienID") String sinhVienID);

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO DANH_GIA (DanhGiaID, Diem, NhanXet, BaoCaoID, GiangVienID, NgayDanhGia)
        VALUES (
            COALESCE(
                (SELECT dg.DanhGiaID FROM (SELECT * FROM DANH_GIA) dg WHERE dg.BaoCaoID = :baoCaoID LIMIT 1), 
                CONCAT('DG', REPLACE(UUID(), '-', ''))
            ),
            :diem, 
            NULLIF(:nhanXet, ''), 
            :baoCaoID,
            (SELECT l.GiangVienID FROM BAO_CAO bc JOIN BAI_TAP bt ON bc.BaiTapID = bt.BaiTapID JOIN LOP_HOC l ON bt.LopID = l.LopID WHERE bc.BaoCaoID = :baoCaoID LIMIT 1),
            NOW()
        )
        ON DUPLICATE KEY UPDATE 
            Diem = :diem,
            NhanXet = COALESCE(NULLIF(:nhanXet, ''), NhanXet),
            NgayDanhGia = NOW()
    """, nativeQuery = true)
    void upsertDanhGiaDiem(
            @Param("baoCaoID") String baoCaoID,
            @Param("diem") Double diem,
            @Param("nhanXet") String nhanXet
    );

    @Query(value = """
        SELECT 
            lh.LopID,
            lh.TenLop,
            mh.TenMonHoc,   
            mh.SoTinChi     
        FROM LOP_HOC_SINH_VIEN lhsv
        JOIN LOP_HOC lh ON lhsv.LopID = lh.LopID
        JOIN MON_HOC mh ON lh.MonHocID = mh.MonHocID
        WHERE UPPER(lhsv.SinhVienID) = UPPER(:sinhVienID)
    """, nativeQuery = true)
    List<Object[]> findLopHocBySinhVienID(@Param("sinhVienID") String sinhVienID);

    
    @Query(value = """
        SELECT COALESCE(AVG(dg.Diem), 0.0)
        FROM BAI_TAP bt
        JOIN LOP_HOC_SINH_VIEN lhsv ON bt.LopID = lhsv.LopID
        LEFT JOIN NHOM_MEMBER nm ON nm.SinhVienID = lhsv.SinhVienID
        LEFT JOIN NHOM n ON n.NhomID = nm.NhomID AND n.BaiTapID = bt.BaiTapID
        
        -- Nhận diện chính xác bản nộp báo cáo (Cá nhân hoặc Nhóm của sinh viên này)
        JOIN BAO_CAO bc ON bt.BaiTapID = bc.BaiTapID
            AND (
                (UPPER(bt.Loai) = 'CA NHAN' AND UPPER(bc.SinhVienID) = UPPER(lhsv.SinhVienID))
                OR (UPPER(bt.Loai) = 'NHOM' AND UPPER(bc.NhomID) = UPPER(n.NhomID))
            )
        JOIN DANH_GIA dg ON dg.BaoCaoID = bc.BaoCaoID
        
        WHERE lhsv.LopID = :lopID 
          AND UPPER(lhsv.SinhVienID) = UPPER(:sinhVienID)
          AND bc.NgayNop = (
              SELECT MAX(bc_inner.NgayNop) 
              FROM BAO_CAO bc_inner 
              WHERE bc_inner.BaiTapID = bc.BaiTapID 
                AND (
                    (UPPER(bt.Loai) = 'CA NHAN' AND UPPER(bc_inner.SinhVienID) = UPPER(lhsv.SinhVienID))
                    OR (UPPER(bt.Loai) = 'NHOM' AND UPPER(bc_inner.NhomID) = UPPER(n.NhomID))
                )
          )
    """, nativeQuery = true)
    Double findDiemTrungBinhHocPhanCuaRiengSinhVien(
            @Param("lopID") String lopID,
            @Param("sinhVienID") String sinhVienID
    );

    @Query(value = """
        SELECT 
            lh.LopID,
            lh.TenLop,
            mh.TenMonHoc
        FROM LOP_HOC_SINH_VIEN lhsv
        JOIN LOP_HOC lh ON lhsv.LopID = lh.LopID
        JOIN MON_HOC mh ON lh.MonHocID = mh.MonHocID
        WHERE UPPER(lhsv.SinhVienID) = UPPER(:sinhVienID)
    """, nativeQuery = true)
    List<Object[]> findLopHocBySingleSV(@Param("sinhVienID") String sinhVienID);
}

