
-- =====================================================
-- USERS
-- =====================================================

INSERT INTO USERS VALUES
('U000', 'admin', '123456', 'Quản Trị Viên Hệ Thống', 'admin@ptit.vn', 'ADMIN', 'admin_avatar.png'),

('U001', 'teacher', '123456', 'PGS.TS Nguyễn Văn A', 'teacher@ptit.vn', 'GIANG_VIEN', 'avatar1.png'),

('U002', 'student01', '123456', 'Nguyễn Văn An', 'an@ptit.vn', 'SINH_VIEN', 'avatar2.png'),

('U003', 'student02', '123456', 'Trần Thị Bình', 'binh@ptit.vn', 'SINH_VIEN', 'avatar3.png'),

('U004', 'student03', '123456', 'Lê Văn Cường', 'cuong@ptit.vn', 'SINH_VIEN', 'avatar4.png');


-- =====================================================
-- GIANG_VIEN
-- =====================================================

INSERT INTO GIANG_VIEN (GiangVienID,UserID,HocVi,BoMon,SoDienThoai,Khoa,PhongLamViec,GioTiepSinhVien,GioiThieu,ChuyenMon)
VALUES (
    'GV001','U001','Tiến sĩ','CNTT','0123456789','Công nghệ thông tin','A3-201','Thứ 2 - Thứ 6, 9:00 - 11:00','Giảng viên khoa Công nghệ thông tin với nhiều năm kinh nghiệm giảng dạy và nghiên cứu.','Java, Web, Cấu trúc dữ liệu, Cơ sở dữ liệu'
);

-- =====================================================
-- SINH_VIEN
-- =====================================================

INSERT INTO SINH_VIEN VALUES
('SV001', 'U002', 'CNTT', 'D23CQCN01', 3.6, '0123456789', '', ''),

('SV002', 'U003', 'CNTT', 'D23CQCN01', 3.2, '0123456789', '', ''),

('SV003', 'U004', 'CNTT', 'D23CQCN02', 3.8, '0123456789', '', '');

-- =====================================================
-- NAM_HOC
-- =====================================================

INSERT INTO NAM_HOC VALUES
('NH2025', '2025-2026');

-- =====================================================
-- HOC_KY
-- =====================================================

INSERT INTO HOC_KY VALUES
('HK1', 'Học kỳ 1', 'NH2025');

-- =====================================================
-- MON_HOC
-- =====================================================

INSERT INTO MON_HOC VALUES
('MH001', 'Cơ sở dữ liệu', 'Thiết kế và quản lý CSDL', 3),

('MH002', 'Lập trình Java', 'Lập trình hướng đối tượng Java', 3);

-- =====================================================
-- LOP_HOC
-- =====================================================

INSERT INTO LOP_HOC VALUES
('L001', 'DB01', 'MH001', 'GV001', 'HK1'),

('L002', 'JAVA01', 'MH002', 'GV001', 'HK1');

-- =====================================================
-- LOP_HOC_SINH_VIEN
-- =====================================================

INSERT INTO LOP_HOC_SINH_VIEN VALUES
('L001', 'SV001'),
('L001', 'SV002'),
('L001', 'SV003'), 
('L002', 'SV001'),
('L002', 'SV003');

-- =====================================================
-- BAI_TAP
-- =====================================================

INSERT INTO BAI_TAP VALUES
(
    'BT001',
    'Thiết kế ERD',
    'Thiết kế sơ đồ thực thể liên kết',
    '2026-04-10 23:59:00',
    'Nhom',
    100,
    'Dang hoat dong',
    'L001'
),

(
    'BT002',
    'Xây dựng CRUD Java',
    'Thiết kế ứng dụng Java cơ bản',
    '2026-04-15 23:59:00',
    'Ca nhan',
    100,
    'Dang hoat dong',
    'L002'
);

-- =====================================================
-- NHOM
-- =====================================================

INSERT INTO NHOM VALUES
('N001', 'Nhóm 1', 'BT001', 'SV001'),

('N002', 'Nhóm 2', 'BT001', 'SV003');

-- =====================================================
-- NHOM_MEMBER
-- =====================================================

INSERT INTO NHOM_MEMBER VALUES
('N001', 'SV001'),
('N001', 'SV002'),
('N002', 'SV003');

-- =====================================================
-- BAO_CAO
-- =====================================================

-- Bài tập nhóm
INSERT INTO BAO_CAO VALUES
(
    'BC001',
    '2026-04-09 20:00:00',
    'erd_report.pdf',
    'Da nop',
    'BT001',
    NULL,
    'N001'
);

-- Bài tập cá nhân
INSERT INTO BAO_CAO VALUES
(
    'BC002',
    '2026-04-14 21:00:00',
    'java_crud.zip',
    'Da nop',
    'BT002',
    'SV001',
    NULL
);

-- =====================================================
-- DANH_GIA
-- =====================================================

INSERT INTO DANH_GIA VALUES
(
    'DG001',
    9.5,
    'Bài làm tốt',
    'BC001',
    'GV001',
    now()
),

(
    'DG002',
    8.5,
    'Cần tối ưu code',
    'BC002',
    'GV001', 
    now()
);

-- =====================================================
-- PHAN_HOI
-- =====================================================

INSERT INTO PHAN_HOI VALUES
(
    'PH001',
    'BC001',
    'GV001',
    'ERD khá đầy đủ',
    '2026-04-11'
);

-- =====================================================
-- TAI_LIEU
-- =====================================================

INSERT INTO TAI_LIEU VALUES
(
    'TL001',
    'Huong_dan_ERD.pdf',
    '/uploads/erd.pdf',
    'PDF',
    '2026-04-01',
    'BT001'
);

-- 1. Thêm một môn học mẫu
INSERT INTO MON_HOC (MonHocID, TenMonHoc, MoTa, SoTinChi) 
VALUES ('MH_CSDL', 'Cơ sở dữ liệu', 'Học về SQL', 3);

-- 2. Thêm một lớp học phần mẫu
INSERT INTO LOP_HOC (LopID, TenLop, MonHocID, GiangVienID, HocKyID) 
VALUES ('Lop_D23_CSDL', 'D23CQCN01', 'MH_CSDL', 'GV001', 'HK1');

-- 3. Đăng ký cho sinh viên SV001 vào lớp này
INSERT INTO LOP_HOC_SINH_VIEN (LopID, SinhVienID) 
VALUES ('Lop_D23_CSDL', 'SV001');

-- 4. Thêm lịch học cho môn này vào Thứ 5 (DayIndex = 3), Ca học từ 9:00 (StartSlot = 2)
INSERT INTO LICH_HOC (LichHocID, LopID, DayIndex, StartSlot, SpanSlots, PhongHoc, LoaiBuoi, GhiChu) VALUES 
('LH_001', 'Lop_D23_CSDL', 3, 2, 2, 'A2-302', 'Lý thuyết', 'Mang theo laptop'),
('LH_002', 'L002', 1, 6, 2, 'A3-205', 'Thực hành', 'Mang theo laptop'),
('LH_003', 'L001', 4, 9, 2, 'B1-102', 'Bài tập', 'Review SQL và chữa bài'),
('LH_004','L002', 5, 4, 3, 'LAB-JAVA-01', 'Thực hành', 'Code Spring Boot');

-- 5. THÊM DEADLINE BÀI TẬP: Đặt hạn nộp chính xác vào ngày 23/05/2026 (Nằm trong tuần 18/05 - 23/05)
INSERT INTO BAI_TAP (BaiTapID, TenBaiTap, MoTa, Deadline, Loai, DiemToiDa, TrangThai, LopID)
VALUES ('BT_001', 'Triển khai CSDL', 'Nộp báo cáo SQL', '2026-05-23 23:59:00', 'Cá nhân', 10.0, 'Dang hoat dong', 'Lop_D23_CSDL');
