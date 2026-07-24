package com.example.workreport.repository;

import com.example.workreport.model.NhomMember;
import com.example.workreport.model.NhomMemberKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import jakarta.transaction.Transactional;

@Repository
public interface NhomMemberRepository extends JpaRepository<NhomMember, NhomMemberKey> {
    List<NhomMember> findByNhom_NhomID(String nhomID);

    @Transactional
    void deleteByNhom_NhomID(String nhomID);
}
