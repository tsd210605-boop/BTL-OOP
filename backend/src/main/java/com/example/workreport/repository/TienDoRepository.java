package com.example.workreport.repository;

import com.example.workreport.model.TienDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TienDoRepository extends JpaRepository<TienDo, String> {
    List<TienDo> findByBaiTap_BaiTapID(String baiTapID);
    List<TienDo> findByNhom_NhomID(String nhomID);
    @Query("""
        SELECT COUNT(t)
        FROM TienDo t
        WHERE t.baiTap.baiTapID = :baiTapID
          AND t.trangThai = :trangThai
    """)
    int countByBaiTapIdAndTrangThai(
            @Param("baiTapID") String baiTapID,
            @Param("trangThai") String trangThai
    );

    @Query("""
        SELECT COUNT(t)
        FROM TienDo t
        WHERE t.nhom.nhomID IN :nhomIDs
          AND t.trangThai = :trangThai
    """)
    long countByNhomIdsAndTrangThai(
            @Param("nhomIDs") List<String> nhomIDs,
            @Param("trangThai") String trangThai
    );

    @Query("""
        SELECT t
        FROM TienDo t
        WHERE t.nhom.nhomID IN :nhomIDs
          AND t.baiTap.baiTapID = :baiTapID
    """)
    Optional<TienDo> findByNhomIdsAndBaiTapId(
            @Param("nhomIDs") List<String> nhomIDs,
            @Param("baiTapID") String baiTapID
    );

    @Query("""
        SELECT t
        FROM TienDo t
        WHERE t.nhom.nhomID = :nhomID
        ORDER BY t.ngayCapNhat DESC
    """)
    List<TienDo> findLatestByNhomID(
            @Param("nhomID") String nhomID
    );

    @Query("""
        SELECT t
        FROM TienDo t
        WHERE t.baiTap.baiTapID = :baiTapID
        ORDER BY t.ngayCapNhat DESC
    """)
    List<TienDo> findByBaiTapOrderByNgayCapNhat(
            @Param("baiTapID") String baiTapID
    );
}
