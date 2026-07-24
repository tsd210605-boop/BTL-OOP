package com.example.workreport.repository;

import com.example.workreport.model.SinhVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SinhVienRepository extends JpaRepository<SinhVien, String> {

    Optional<SinhVien> findByUser_UserID(String userID);

    Optional<SinhVien> findByUser_Username(String username);

    @Query(value = """
        SELECT
            sv.SinhVienID AS sinhVienID,
            u.Fullname AS fullName,
            sv.Lop AS lop,
            sv.Khoa AS khoa,
            u.Email AS email,
            u.Role AS role
        FROM SINH_VIEN sv
        JOIN USERS u ON sv.UserID = u.UserID
        WHERE sv.UserID = :userID
    """, nativeQuery = true)
    Optional<Object[]> findProfileByUserIDNative(@Param("userID") String userID);

    @Query(value = """
        SELECT sv.Lop, COUNT(sv2.SinhVienID) AS tongSV
        FROM SINH_VIEN sv
        JOIN SINH_VIEN sv2 ON sv2.Lop = sv.Lop
        WHERE sv.SinhVienID = :sinhVienID
        GROUP BY sv.Lop
    """, nativeQuery = true)
    List<Object[]> findLopInfoBySinhVienID(@Param("sinhVienID") String sinhVienID);

    @Query(value = """
        SELECT 
            sv.SinhVienID,                                                                    
            u.Fullname,                                                                       
            sv.Lop,                                                                           
            (SELECT COUNT(*) FROM BAI_TAP bt2 WHERE bt2.LopID = lhsv.LopID) AS tongBaiTap,    
            COUNT(DISTINCT CASE WHEN bc.BaoCaoID IS NOT NULL AND bc.TrangThai = 'Da nop' THEN bt.BaiTapID END) AS hoanThanh, 
            COUNT(DISTINCT CASE WHEN bc.BaoCaoID IS NULL AND bt.Deadline >= NOW() THEN bt.BaiTapID END) AS dangLam,         
            COUNT(DISTINCT CASE WHEN bc.BaoCaoID IS NULL AND bt.Deadline < NOW() THEN bt.BaiTapID END) AS quaHan,           
            COALESCE(AVG(dg.Diem), 0.0) AS diemTrungBinh,                                     
            (SELECT COUNT(*) FROM NHOM_MEMBER nm WHERE nm.SinhVienID = sv.SinhVienID) AS soNhom, 
            lhsv.LopID AS lopHocPhanID                                                        
        FROM SINH_VIEN sv
        JOIN USERS u ON sv.UserID = u.UserID
        JOIN LOP_HOC_SINH_VIEN lhsv ON sv.SinhVienID = lhsv.SinhVienID
        JOIN LOP_HOC lh ON lhsv.LopID = lh.LopID
        LEFT JOIN NHOM_MEMBER nm_main ON sv.SinhVienID = nm_main.SinhVienID
        LEFT JOIN BAI_TAP bt ON bt.LopID = lhsv.LopID                                         
        LEFT JOIN NHOM n ON n.NhomID = nm_main.NhomID AND n.BaiTapID = bt.BaiTapID            
        LEFT JOIN BAO_CAO bc ON bt.BaiTapID = bc.BaiTapID 
            AND (bc.SinhVienID = sv.SinhVienID OR bc.NhomID = n.NhomID)
        LEFT JOIN DANH_GIA dg ON bc.BaoCaoID = dg.BaoCaoID
        WHERE lh.GiangVienID = :giangVienID
        GROUP BY sv.SinhVienID, u.Fullname, sv.Lop, lhsv.LopID
        ORDER BY u.Fullname ASC
    """, nativeQuery = true)
    List<Object[]> findTienDoSinhVienByGiangVien(@Param("giangVienID") String giangVienID);
}