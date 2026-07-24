package com.example.workreport.service;

import com.example.workreport.dto.CapNhatProfileRequest;
import com.example.workreport.dto.GiangVienProfileDTO;
import com.example.workreport.model.GiangVien;
import com.example.workreport.model.User;
import com.example.workreport.repository.GiangVienRepository;
import com.example.workreport.repository.UserRepository;

import org.springframework.stereotype.Service;

@Service
public class GiangVienServiceImpl implements GiangVienService {

    private final GiangVienRepository repository;
    private final UserRepository userRepository;

    public GiangVienServiceImpl(
            GiangVienRepository repository,
            UserRepository userRepository
    ) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Override
    public GiangVienProfileDTO getProfile(String userID) {

        GiangVien gv = repository.findByUser_UserID(userID)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Không tìm thấy giảng viên"
                        )
                );

        GiangVienProfileDTO dto =
                new GiangVienProfileDTO();

        dto.setFullName(
                gv.getUser() != null
                        ? gv.getUser().getFullname()
                        : ""
        );

        dto.setEmail(
                gv.getUser() != null
                        ? gv.getUser().getEmail()
                        : ""
        );

        dto.setTitle(
                gv.getHocVi() != null
                        ? gv.getHocVi()
                        : ""
        );

        dto.setFaculty(
                gv.getKhoa() != null
                        ? gv.getKhoa()
                        : ""
        );

        dto.setPhone(
                gv.getSoDienThoai() != null
                        ? gv.getSoDienThoai()
                        : ""
        );

        dto.setOffice(
                gv.getPhongLamViec() != null
                        ? gv.getPhongLamViec()
                        : ""
        );

        dto.setOfficeHours(
                gv.getGioTiepSinhVien() != null
                        ? gv.getGioTiepSinhVien()
                        : ""
        );

        dto.setBio(
                gv.getGioiThieu() != null
                        ? gv.getGioiThieu()
                        : ""
        );

        dto.setSpecialties(
                gv.getChuyenMon() != null
                        ? gv.getChuyenMon()
                        : ""
        );

        return dto;
    }

    @Override
    public String updateProfile(
            String userID,
            CapNhatProfileRequest request
    ) {

        GiangVien gv = repository.findByUser_UserID(userID)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Không tìm thấy giảng viên"
                        )
                );

        User user = gv.getUser();

        if (user != null) {

            user.setFullname(
                    request.getFullName()
            );

            user.setEmail(
                    request.getEmail()
            );

            userRepository.save(user);
        }

        gv.setHocVi(request.getTitle());
        gv.setKhoa(request.getFaculty());
        gv.setSoDienThoai(request.getPhone());
        gv.setPhongLamViec(request.getOffice());
        gv.setGioTiepSinhVien(request.getOfficeHours());
        gv.setGioiThieu(request.getBio());
        gv.setChuyenMon(request.getSpecialties());

        repository.save(gv);

        return "Cập nhật profile thành công";
    }
}