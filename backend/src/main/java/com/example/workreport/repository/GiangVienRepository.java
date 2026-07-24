package com.example.workreport.repository;

import com.example.workreport.model.GiangVien;
import com.example.workreport.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GiangVienRepository extends JpaRepository<GiangVien, String> {
    Optional<GiangVien> findByUser(User user);
    Optional<GiangVien> findByUser_UserID(String userID);
    Optional<GiangVien> findByUser_Username(String username);
}
