package com.example.workreport.controller;

import com.example.workreport.dto.ApiResponse;
import com.example.workreport.dto.ThemGiangVienRequest;
import com.example.workreport.dto.ThemMonHocRequest;
import com.example.workreport.dto.ThemSinhVienRequest;
import com.example.workreport.exception.AppException;
import com.example.workreport.model.GiangVien;
import com.example.workreport.model.MonHoc;
import com.example.workreport.model.SinhVien;
import com.example.workreport.model.User;
import com.example.workreport.repository.GiangVienRepository;
import com.example.workreport.repository.MonHocRepository;
import com.example.workreport.repository.SinhVienRepository;
import com.example.workreport.repository.UserRepository;
import com.example.workreport.repository.NhomRepository;
import com.example.workreport.repository.NhomMemberRepository;
import com.example.workreport.repository.BaiTapRepository;
import com.example.workreport.model.Nhom;
import com.example.workreport.model.NhomMember;
import com.example.workreport.model.NhomMemberKey;
import com.example.workreport.model.BaiTap;
import com.example.workreport.dto.TaoNhomRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final UserRepository userRepository;
    private final SinhVienRepository sinhVienRepository;
    private final GiangVienRepository giangVienRepository;
    private final MonHocRepository monHocRepository;
    private final NhomRepository nhomRepository;
    private final NhomMemberRepository nhomMemberRepository;
    private final BaiTapRepository baiTapRepository;

    public AdminController(
            UserRepository userRepository,
            SinhVienRepository sinhVienRepository,
            GiangVienRepository giangVienRepository,
            MonHocRepository monHocRepository,
            NhomRepository nhomRepository,
            NhomMemberRepository nhomMemberRepository,
            BaiTapRepository baiTapRepository
    ) {
        this.userRepository = userRepository;
        this.sinhVienRepository = sinhVienRepository;
        this.giangVienRepository = giangVienRepository;
        this.monHocRepository = monHocRepository;
        this.nhomRepository = nhomRepository;
        this.nhomMemberRepository = nhomMemberRepository;
        this.baiTapRepository = baiTapRepository;
    }

    @GetMapping("/sinhvien")
    public ResponseEntity<ApiResponse<List<SinhVien>>>
    getAllSinhVien() {
        return ResponseEntity.ok(ApiResponse.ok(sinhVienRepository.findAll()));
    }

    @PostMapping("/sinhvien")
    public ResponseEntity<ApiResponse<Map<String, String>>>
    themSinhVien(@RequestBody ThemSinhVienRequest req) {
        long soSV = sinhVienRepository.count() + 1;
        long soUser = userRepository.count() + 1;
        String sinhVienID = String.format("SV%03d", soSV);
        String userID = String.format("U%03d", soUser);
        while (sinhVienRepository.existsById(sinhVienID)) {
            soSV++;
            sinhVienID = String.format("SV%03d", soSV);
        }
        while (userRepository.existsById(userID)) {
            soUser++;
            userID = String.format("U%03d", soUser);
        }
        User user = new User();
        user.setUserID(userID);
        user.setUsername(req.getUsername());
        user.setPassword(req.getPassword());
        user.setFullname(req.getFullName());
        user.setEmail(req.getEmail());

        user.setRole("SINH_VIEN");
        userRepository.save(user);
        SinhVien sv = new SinhVien();
        sv.setSinhVienID(sinhVienID);
        sv.setUser(user);
        sv.setKhoa(req.getKhoa());
        sv.setLop(req.getLop());
        sinhVienRepository.save(sv);
        Map<String, String> response = new HashMap<>();
        response.put("sinhVienID", sinhVienID);
        response.put("userID", userID);
        return ResponseEntity.ok(ApiResponse.ok(response, "Thêm sinh viên thành công"));
    }

    @DeleteMapping("/sinhvien/{sinhVienID}")
    public ResponseEntity<ApiResponse<Map<String, String>>>
    xoaSinhVien(
            @PathVariable String sinhVienID
    ) {
        SinhVien sv = sinhVienRepository.findById(sinhVienID).orElseThrow(() -> new AppException("Sinh viên không tồn tại", "404", 404));

        String userID = sv.getUser().getUserID();
        sinhVienRepository.delete(sv);
        userRepository.deleteById(userID);
        return ResponseEntity.ok(ApiResponse.ok(Map.of("message", "Xóa sinh viên thành công"), "Xóa thành công"));
    }

    @GetMapping("/giangvien")
    public ResponseEntity<ApiResponse<List<GiangVien>>>
    getAllGiangVien() {
        return ResponseEntity.ok(ApiResponse.ok(giangVienRepository.findAll()));
    }

    @PostMapping("/giangvien")
    public ResponseEntity<ApiResponse<Map<String, String>>>
    themGiangVien(
            @RequestBody ThemGiangVienRequest req
    ) {
        long soGV = giangVienRepository.count() + 1;
        long soUser = userRepository.count() + 1;
        String giangVienID = String.format("GV%03d", soGV);
        String userID = String.format("U%03d", soUser);
        while (giangVienRepository.existsById(giangVienID)) {
            soGV++;
            giangVienID = String.format("GV%03d", soGV);
        }
        while (userRepository.existsById(userID)) {
            soUser++;
            userID = String.format("U%03d", soUser);
        }

        User user = new User();
        user.setUserID(userID);
        user.setUsername(req.getUsername());
        user.setPassword(req.getPassword());
        user.setFullname(req.getFullName());
        user.setEmail(req.getEmail());

        user.setRole("GIANG_VIEN");
        userRepository.save(user);
        GiangVien gv = new GiangVien();
        gv.setGiangVienID(giangVienID);
        gv.setUser(user);
        gv.setHocVi(req.getHocVi());
        gv.setBoMon(req.getBoMon());
        gv.setSoDienThoai(req.getSoDienThoai());
        gv.setKhoa(req.getKhoa());
        gv.setPhongLamViec(req.getPhongLamViec());
        gv.setGioTiepSinhVien(req.getGioTiepSinhVien());
        gv.setGioiThieu(req.getGioiThieu());
        gv.setChuyenMon(req.getChuyenMon());
        giangVienRepository.save(gv);
        Map<String, String> response = new HashMap<>();
        response.put("giangVienID", giangVienID);
        response.put("userID", userID);
        return ResponseEntity.ok(ApiResponse.ok(response, "Thêm giảng viên thành công"));
    }

    @DeleteMapping("/giangvien/{giangVienID}")
    public ResponseEntity<ApiResponse<Map<String, String>>>
    xoaGiangVien(
            @PathVariable String giangVienID
    ) {
        GiangVien gv = giangVienRepository.findById(giangVienID).orElseThrow(() -> new AppException("Giảng viên không tồn tại", "404", 404));
        String userID = gv.getUser().getUserID();
        giangVienRepository.delete(gv);
        userRepository.deleteById(userID);
        return ResponseEntity.ok(ApiResponse.ok(Map.of("message", "Xóa giảng viên thành công"), "Xóa thành công"));
    }

    @GetMapping("/monhoc")
    public ResponseEntity<ApiResponse<List<MonHoc>>>
    getAllMonHoc() {
        return ResponseEntity.ok(ApiResponse.ok(monHocRepository.findAll()));
    }

    @PostMapping("/monhoc")
    public ResponseEntity<ApiResponse<Map<String, String>>>
    themMonHoc(
            @RequestBody ThemMonHocRequest req
    ) {
        long soMH = monHocRepository.count() + 1;
        String monHocID = String.format("MH%03d", soMH);
        while (monHocRepository.existsById(monHocID)) {
            soMH++;
            monHocID = String.format("MH%03d", soMH);
        }
        MonHoc mh = new MonHoc();
        mh.setMonHocID(monHocID);
        mh.setTenMonHoc(req.getTenMonHoc());
        mh.setMoTa(req.getMoTa());
        mh.setSoTinChi(req.getSoTinChi());
        monHocRepository.save(mh);
        Map<String, String> response = new HashMap<>();
        response.put("monHocID", monHocID);
        return ResponseEntity.ok(ApiResponse.ok(response, "Thêm môn học thành công"));
    }

    @DeleteMapping("/monhoc/{monHocID}")
    public ResponseEntity<ApiResponse<Map<String, String>>>
    xoaMonHoc(
            @PathVariable String monHocID
    ) {
        MonHoc mh = monHocRepository.findById(monHocID).orElseThrow(() -> new AppException("Môn học không tồn tại", "404", 404));
        monHocRepository.delete(mh);
        return ResponseEntity.ok(ApiResponse.ok(Map.of("message", "Xóa môn học thành công"), "Xóa thành công"));
    }

    @GetMapping("/nhom")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getAllNhom() {
        List<Nhom> nhoms = nhomRepository.findAll();
        List<Map<String, Object>> response = new ArrayList<>();
        for (Nhom n : nhoms) {
            Map<String, Object> map = new HashMap<>();
            map.put("nhomID", n.getNhomID());
            map.put("tenNhom", n.getTenNhom());
            map.put("baiTapID", n.getBaiTap() != null ? n.getBaiTap().getBaiTapID() : null);
            map.put("tenBaiTap", n.getBaiTap() != null ? n.getBaiTap().getTenBaiTap() : null);
            map.put("truongNhom", n.getTruongNhom() != null ? n.getTruongNhom().getSinhVienID() : null);
            map.put("truongNhomTen", n.getTruongNhom() != null ? n.getTruongNhom().getUser().getFullname() : null);
            
            List<String> thanhVienList = new ArrayList<>();
            List<NhomMember> members = nhomMemberRepository.findByNhom_NhomID(n.getNhomID());
            for (NhomMember member : members) {
                thanhVienList.add(member.getSinhVien().getSinhVienID() + " - " + member.getSinhVien().getUser().getFullname());
            }
            map.put("thanhVien", thanhVienList);
            response.add(map);
        }
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PostMapping("/nhom")
    @Transactional
    public ResponseEntity<ApiResponse<Map<String, String>>> themNhom(@RequestBody TaoNhomRequest req) {
        long soNhom = nhomRepository.count() + 1;
        String nhomID = String.format("N%03d", soNhom);
        while (nhomRepository.existsById(nhomID)) {
            soNhom++;
            nhomID = String.format("N%03d", soNhom);
        }

        BaiTap baiTap = baiTapRepository.findById(req.getBaiTapID())
                .orElseThrow(() -> new AppException("Bài tập không tồn tại", "404", 404));
        SinhVien truongNhom = sinhVienRepository.findById(req.getTruongNhom())
                .orElseThrow(() -> new AppException("Trưởng nhóm không tồn tại", "404", 404));

        Nhom nhom = new Nhom();
        nhom.setNhomID(nhomID);
        nhom.setTenNhom(req.getTenNhom());
        nhom.setBaiTap(baiTap);
        nhom.setTruongNhom(truongNhom);
        nhomRepository.save(nhom);

        if (req.getThanhVien() != null && !req.getThanhVien().isEmpty()) {
            for (String svID : req.getThanhVien()) {
                SinhVien sv = sinhVienRepository.findById(svID)
                        .orElseThrow(() -> new AppException("Sinh viên " + svID + " không tồn tại", "404", 404));
                NhomMember member = new NhomMember();
                NhomMemberKey key = new NhomMemberKey(nhomID, svID);
                member.setId(key);
                member.setNhom(nhom);
                member.setSinhVien(sv);
                nhomMemberRepository.save(member);
            }
        }

        Map<String, String> response = new HashMap<>();
        response.put("nhomID", nhomID);
        return ResponseEntity.ok(ApiResponse.ok(response, "Tạo nhóm thành công"));
    }

    @DeleteMapping("/nhom/{nhomID}")
    @Transactional
    public ResponseEntity<ApiResponse<Map<String, String>>> xoaNhom(@PathVariable String nhomID) {
        Nhom nhom = nhomRepository.findById(nhomID)
                .orElseThrow(() -> new AppException("Nhóm không tồn tại", "404", 404));
        try {
            nhomMemberRepository.deleteByNhom_NhomID(nhomID);
            nhomRepository.delete(nhom);
            return ResponseEntity.ok(ApiResponse.ok(Map.of("message", "Xóa nhóm thành công"), "Xóa thành công"));
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            return ResponseEntity.status(400).body(ApiResponse.badRequest("Không thể xóa nhóm này vì nhóm đã nộp bài. Vui lòng xóa các bài nộp của nhóm trước."));
        }
    }

    @GetMapping("/baitap")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getAllBaiTap() {
        List<BaiTap> baiTaps = baiTapRepository.findAll();
        List<Map<String, Object>> response = new ArrayList<>();
        for (BaiTap bt : baiTaps) {
            Map<String, Object> map = new HashMap<>();
            map.put("baiTapID", bt.getBaiTapID());
            map.put("tenBaiTap", bt.getTenBaiTap());
            map.put("lopID", bt.getLopHoc() != null ? bt.getLopHoc().getLopID() : null);
            response.add(map);
        }
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}