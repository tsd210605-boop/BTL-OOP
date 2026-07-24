-- DROP DATABASE IF EXISTS ql_cv;
CREATE DATABASE ql_cv;
USE ql_cv;

-- =====================================================
-- USERS
-- =====================================================

CREATE TABLE USERS (
    UserID VARCHAR(50) PRIMARY KEY,
    Username VARCHAR(50) UNIQUE,
    Password VARCHAR(100),
    Fullname VARCHAR(255),
    Email VARCHAR(255) UNIQUE,
    Role VARCHAR(50),
    Avatar VARCHAR(255)
);

-- =====================================================
-- GIANG_VIEN
-- =====================================================

CREATE TABLE GIANG_VIEN (
    GiangVienID VARCHAR(50) PRIMARY KEY,
    UserID VARCHAR(50) UNIQUE,

    HocVi VARCHAR(100),
    BoMon VARCHAR(100),
    SoDienThoai VARCHAR(20),

    Khoa VARCHAR(100),
    PhongLamViec VARCHAR(100),
    GioTiepSinhVien VARCHAR(255),

    GioiThieu TEXT,
    ChuyenMon TEXT,

    FOREIGN KEY (UserID)
    REFERENCES USERS(UserID)
);

-- =====================================================
-- SINH_VIEN
-- =====================================================

CREATE TABLE SINH_VIEN (
    SinhVienID VARCHAR(50) PRIMARY KEY,
    UserID VARCHAR(50) UNIQUE,
    Khoa VARCHAR(100),
    Lop VARCHAR(50),
    GPA FLOAT,
    SoDienThoai VARCHAR(20),
    GioiThieu TEXT,
    KyNang TEXT,

    FOREIGN KEY (UserID)
    REFERENCES USERS(UserID)
);

-- =====================================================
-- NAM_HOC
-- =====================================================

CREATE TABLE NAM_HOC (
    NamHocID VARCHAR(50) PRIMARY KEY,
    TenNamHoc VARCHAR(100)
);

-- =====================================================
-- HOC_KY
-- =====================================================

CREATE TABLE HOC_KY (
    HocKyID VARCHAR(50) PRIMARY KEY,
    TenHocKy VARCHAR(50),

    NamHocID VARCHAR(50),

    FOREIGN KEY (NamHocID)
    REFERENCES NAM_HOC(NamHocID)
);

-- =====================================================
-- MON_HOC
-- =====================================================

CREATE TABLE MON_HOC (
    MonHocID VARCHAR(50) PRIMARY KEY,
    TenMonHoc VARCHAR(255),
    MoTa TEXT,
    SoTinChi INT
);

-- =====================================================
-- LOP_HOC
-- =====================================================

CREATE TABLE LOP_HOC (
    LopID VARCHAR(50) PRIMARY KEY,
    TenLop VARCHAR(100),

    MonHocID VARCHAR(50),
    GiangVienID VARCHAR(50),
    HocKyID VARCHAR(50),

    FOREIGN KEY (MonHocID)
    REFERENCES MON_HOC(MonHocID),

    FOREIGN KEY (GiangVienID)
    REFERENCES GIANG_VIEN(GiangVienID),

    FOREIGN KEY (HocKyID)
    REFERENCES HOC_KY(HocKyID)
);

-- =====================================================
-- LOP_HOC_SINH_VIEN
-- =====================================================

CREATE TABLE LOP_HOC_SINH_VIEN (
    LopID VARCHAR(50),
    SinhVienID VARCHAR(50),

    PRIMARY KEY (LopID, SinhVienID),

    FOREIGN KEY (LopID)
    REFERENCES LOP_HOC(LopID),

    FOREIGN KEY (SinhVienID)
    REFERENCES SINH_VIEN(SinhVienID)
);

-- =====================================================
-- BAI_TAP
-- =====================================================

CREATE TABLE BAI_TAP (
    BaiTapID VARCHAR(50) PRIMARY KEY,

    TenBaiTap VARCHAR(255),
    MoTa TEXT,

    Deadline DATETIME,

    Loai VARCHAR(50),

    DiemToiDa FLOAT,

    TrangThai VARCHAR(100),

    LopID VARCHAR(50),

    FOREIGN KEY (LopID)
    REFERENCES LOP_HOC(LopID)
);

-- =====================================================
-- NHOM
-- =====================================================

CREATE TABLE NHOM (
    NhomID VARCHAR(50) PRIMARY KEY,

    TenNhom VARCHAR(100),

    BaiTapID VARCHAR(50),

    TruongNhom VARCHAR(50),

    FOREIGN KEY (BaiTapID)
    REFERENCES BAI_TAP(BaiTapID),

    FOREIGN KEY (TruongNhom)
    REFERENCES SINH_VIEN(SinhVienID)
);

-- =====================================================
-- NHOM_MEMBER
-- =====================================================

CREATE TABLE NHOM_MEMBER (
    NhomID VARCHAR(50),
    SinhVienID VARCHAR(50),

    PRIMARY KEY (NhomID, SinhVienID),

    FOREIGN KEY (NhomID)
    REFERENCES NHOM(NhomID),

    FOREIGN KEY (SinhVienID)
    REFERENCES SINH_VIEN(SinhVienID)
);

-- =====================================================
-- TIEN_DO
-- =====================================================

CREATE TABLE TIEN_DO (
    TienDoID VARCHAR(50) PRIMARY KEY,

    NgayCapNhat DATE,

    PhanTramHoanThanh INT,

    TrangThai VARCHAR(100),

    NoiDung TEXT,

    BaiTapID VARCHAR(50),

    NhomID VARCHAR(50),

    FOREIGN KEY (BaiTapID)
    REFERENCES BAI_TAP(BaiTapID),

    FOREIGN KEY (NhomID)
    REFERENCES NHOM(NhomID)
);

-- =====================================================
-- BAO_CAO
-- =====================================================

CREATE TABLE BAO_CAO (
    BaoCaoID VARCHAR(50) PRIMARY KEY,

    NgayNop DATETIME,

    FileBaoCao VARCHAR(255),

    TrangThai VARCHAR(100),

    BaiTapID VARCHAR(50),

    SinhVienID VARCHAR(50),

    NhomID VARCHAR(50),

    FOREIGN KEY (BaiTapID)
    REFERENCES BAI_TAP(BaiTapID),

    FOREIGN KEY (SinhVienID)
    REFERENCES SINH_VIEN(SinhVienID),

    FOREIGN KEY (NhomID)
    REFERENCES NHOM(NhomID)
);

-- =====================================================
-- DANH_GIA
-- =====================================================

CREATE TABLE DANH_GIA (
    DanhGiaID VARCHAR(50) PRIMARY KEY,

    Diem FLOAT,

    NhanXet TEXT,

    BaoCaoID VARCHAR(50),

    GiangVienID VARCHAR(50),
	
    NgayDanhGia DATETIME NULL,
    
    FOREIGN KEY (BaoCaoID)
    REFERENCES BAO_CAO(BaoCaoID),

    FOREIGN KEY (GiangVienID)
    REFERENCES GIANG_VIEN(GiangVienID)
);

-- =====================================================
-- TAI_LIEU
-- =====================================================

CREATE TABLE TAI_LIEU (
    TaiLieuID VARCHAR(50) PRIMARY KEY,

    TenFile VARCHAR(255),

    DuongDanFile VARCHAR(255),

    LoaiFile VARCHAR(50),

    NgayCapNhat DATE,

    BaiTapID VARCHAR(50),

    FOREIGN KEY (BaiTapID)
    REFERENCES BAI_TAP(BaiTapID)
);

-- =====================================================
-- PHAN_HOI
-- =====================================================

CREATE TABLE PHAN_HOI (
    PhanHoiID VARCHAR(50) PRIMARY KEY,

    BaoCaoID VARCHAR(50),

    GiangVienID VARCHAR(50),

    NoiDung TEXT,

    NgayPhanHoi DATE,

    FOREIGN KEY (BaoCaoID)
    REFERENCES BAO_CAO(BaoCaoID),

    FOREIGN KEY (GiangVienID)
    REFERENCES GIANG_VIEN(GiangVienID)
);

CREATE TABLE LICH_HOC (
    LichHocID VARCHAR(50) PRIMARY KEY,
    LopID VARCHAR(50),
    DayIndex INT,       -- 0: Thứ 2, 1: Thứ 3, 2: Thứ 4, 3: Thứ 5, 4: Thứ 6, 5: Thứ 7
    StartSlot INT,      -- Index kíp học bắt đầu từ mảng TIMES (0 -> 15)
    SpanSlots INT,      -- Số tiết diễn ra (ví dụ: 2 tiết, 3 tiết)
    PhongHoc VARCHAR(100),
    LoaiBuoi VARCHAR(50), -- Lý thuyết / Thực hành
    GhiChu TEXT,
    FOREIGN KEY (LopID) REFERENCES LOP_HOC(LopID)
);

