package com.example.workreport.service;

import com.example.workreport.dto.CapNhatProfileRequest;
import com.example.workreport.dto.GiangVienProfileDTO;

public interface GiangVienService {
    GiangVienProfileDTO getProfile(String userID);
    String updateProfile(String userID, CapNhatProfileRequest request);
}