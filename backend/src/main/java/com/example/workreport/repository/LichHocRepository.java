package com.example.workreport.repository;

import com.example.workreport.model.BaiTap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LichHocRepository extends JpaRepository<BaiTap, String> {

    @Query(value = """
        SELECT 
            lh.LichHocID,
            lh.LopID,
            l.TenLop,
            mh.MonHocID,
            mh.TenMonHoc,
            u.Fullname AS tenGiangVien,
            lh.DayIndex,
            lh.StartSlot,
            lh.SpanSlots,
            lh.PhongHoc,
            lh.LoaiBuoi,
            lh.GhiChu
        FROM LICH_HOC lh
        JOIN LOP_HOC l ON lh.LopID = l.LopID
        JOIN MON_HOC mh ON l.MonHocID = mh.MonHocID
        JOIN LOP_HOC_SINH_VIEN lhsv ON l.LopID = lhsv.LopID
        JOIN GIANG_VIEN gv ON l.GiangVienID = gv.GiangVienID
        JOIN USERS u ON gv.UserID = u.UserID
        WHERE UPPER(lhsv.SinhVienID) = UPPER(:sinhVienID)
    """, nativeQuery = true)
    List<Object[]> findLichHocTuongThichSinhVien(@Param("sinhVienID") String sinhVienID);

    @Query(value = """
        SELECT 
            bt.BaiTapID,
            bt.TenBaiTap,
            bt.Deadline,
            bt.Loai,
            mh.TenMonHoc,
            l.LopID,
            l.TenLop,
            bt.TrangThai
        FROM BAI_TAP bt
        JOIN LOP_HOC l ON bt.LopID = l.LopID
        JOIN MON_HOC mh ON l.MonHocID = mh.MonHocID
        JOIN LOP_HOC_SINH_VIEN lhsv ON l.LopID = lhsv.LopID
        WHERE UPPER(lhsv.SinhVienID) = UPPER(:sinhVienID)
        ORDER BY bt.Deadline ASC
    """, nativeQuery = true)
    List<Object[]> findToanBoDeadlineCuaSinhVien(@Param("sinhVienID") String sinhVienID);
}