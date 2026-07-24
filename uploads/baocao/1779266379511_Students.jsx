import React, { useState, useEffect } from "react";
import Header from "../components/Header";
import NavMenu from "../components/NavMenu";
import {
    FaUserGraduate,
    FaSearch,
    FaChartLine,
    FaUsers,
    FaChevronRight,
    FaArrowLeft,
    FaBook
} from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import api from "../../api/api";
import "../style/Students.css";

export default function Students() {
    const navigate = useNavigate();
    const [studentList, setStudentList] = useState([]);
    const [loading, setLoading] = useState(true);
    const [searchTerm, setSearchTerm] = useState("");

    // 🔥 State quản lý việc chọn Lớp Học Phần (Ví dụ: "L001", "L002")
    const [selectedLHP, setSelectedLHP] = useState(null);

    const loadStudents = async () => {
        try {
            setLoading(true);
            const giangVienID = localStorage.getItem("giangVienID") || "GV001";
            const response = await api.get(`/api/sinhvien/giangvien/${giangVienID}`);
            if (response && response.success) {
                setStudentList(response.data);
            }
        } catch (error) {
            console.error("Lỗi đồng bộ danh sách sinh viên:", error);
        } finally {
            setLoading(false);
        }
    };

    // 🌟 ĐÃ CẬP NHẬT: Hàm điều hướng bốc trúng mã lớp đang chọn và mã sinh viên lên thanh URL
    const handleViewDetail = (sinhVienID) => {
        if (selectedLHP) {
            navigate(`/lop-hoc/${selectedLHP}/sinh-vien/${sinhVienID}`);
        }
    };

    useEffect(() => {
        loadStudents();
    }, []);

    // 📊 THUẬT TOÁN GOM CỤM ĐỂ TRÍCH XUẤT DANH SÁCH CÁC LỚP HỌC PHẦN (LHP) DUY NHẤT
    const lhpGroups = studentList.reduce((acc, current) => {
        const lhpID = current.lopHocPhanID || "Lớp học phần";
        if (!acc[lhpID]) {
            acc[lhpID] = {
                lhpID: lhpID,
                studentCount: 0,
                totalPointsSum: 0,
                gradedStudentsCount: 0
            };
        }
        acc[lhpID].studentCount += 1;
        if (current.diemTrungBinh > 0) {
            acc[lhpID].totalPointsSum += current.diemTrungBinh;
            acc[lhpID].gradedStudentsCount += 1;
        }
        return acc;
    }, {});

    const uniqueLHPs = Object.values(lhpGroups);

    // 🔍 LỌC SINH VIÊN: Chỉ lấy sinh viên thuộc Lớp Học Phần đang chọn + khớp từ khóa tìm kiếm
    const displayedStudents = studentList.filter(student => {
        if (student.lopHocPhanID !== selectedLHP) return false;

        const keyword = searchTerm.toLowerCase();
        return (
            student.fullName.toLowerCase().includes(keyword) ||
            student.sinhVienID.toLowerCase().includes(keyword) ||
            student.lop.toLowerCase().includes(keyword)
        );
    });

    if (loading) {
        return (
            <><Header /><NavMenu /><div className="profile-loading">Đang tổng hợp dữ liệu học phần...</div></>
        );
    }

    return (
        <>
            <Header />
            <NavMenu />

            <div className="students-page">
                <div className="students-container">

                    {/* ================================================================= */}
                    {/* KHỐI 1: GIAO DIỆN CHƯA CHỌN LỚP HỌC PHẦN */}
                    {/* ================================================================= */}
                    {selectedLHP === null ? (
                        <>
                            <div className="students-header">
                                <div>
                                    <h1>Danh sách lớp học phần</h1>
                                    <p>Quản lý tiến độ học tập và điểm số theo từng mã lớp học phần giảng dạy</p>
                                </div>
                                <div className="students-total">
                                    <FaBook />
                                    <span>{uniqueLHPs.length} lớp học phần</span>
                                </div>
                            </div>

                            <div className="class-grid" style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: '20px', marginTop: '25px' }}>
                                {uniqueLHPs.map((lhp, index) => {
                                    const avgPoint = lhp.gradedStudentsCount > 0
                                        ? (lhp.totalPointsSum / lhp.gradedStudentsCount).toFixed(1)
                                        : "---";

                                    return (
                                        <div
                                            key={index}
                                            className="class-select-card"
                                            onClick={() => {
                                                setSelectedLHP(lhp.lhpID);
                                                setSearchTerm("");
                                            }}
                                            style={{
                                                background: '#ffffff',
                                                border: '1px solid #e2e8f0',
                                                borderRadius: '12px',
                                                padding: '24px',
                                                cursor: 'pointer',
                                                transition: 'all 0.2s ease',
                                                boxShadow: '0 1px 3px rgba(0,0,0,0.05)'
                                            }}
                                            onMouseEnter={(e) => {
                                                e.currentTarget.style.transform = 'translateY(-2px)';
                                                e.currentTarget.style.boxShadow = '0 10px 15px -3px rgba(0, 0, 0, 0.1)';
                                                e.currentTarget.style.borderColor = '#2563eb';
                                            }}
                                            onMouseLeave={(e) => {
                                                e.currentTarget.style.transform = 'translateY(0)';
                                                e.currentTarget.style.boxShadow = '0 1px 3px rgba(0,0,0,0.05)';
                                                e.currentTarget.style.borderColor = '#e2e8f0';
                                            }}
                                        >
                                            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
                                                <div style={{ background: '#f0fdf4', padding: '12px', borderRadius: '8px', color: '#16a34a' }}>
                                                    <FaBook style={{ fontSize: '20px' }} />
                                                </div>
                                                <span style={{ fontSize: '14px', color: '#2563eb', fontWeight: '600', display: 'flex', alignItems: 'center', gap: '4px' }}>
                                                    Xem danh sách <FaChevronRight style={{ fontSize: '10px' }} />
                                                </span>
                                            </div>

                                            <h3 style={{ margin: '0 0 6px 0', fontSize: '22px', color: '#1e293b', fontWeight: 'bold' }}>
                                                Mã LHP: {lhp.lhpID}
                                            </h3>
                                            <p style={{ margin: '0 0 16px 0', fontSize: '14px', color: '#64748b' }}>
                                                {lhp.lhpID === "L001" ? "Môn học: Cơ sở dữ liệu" : "Môn học: Lập trình Java"}
                                            </p>

                                            <div style={{ display: 'flex', gap: '20px', borderTop: '1px dashed #e2e8f0', paddingTop: '14px', fontSize: '14px', color: '#475569' }}>
                                                <div>Sĩ số: <strong style={{ color: '#0f172a' }}>{lhp.studentCount} sinh viên</strong></div>
                                                <div>Điểm TB lớp: <strong style={{ color: '#2563eb' }}>{avgPoint}đ</strong></div>
                                            </div>
                                        </div>
                                    );
                                })}
                            </div>
                        </>
                    ) : (
                        // =================================================================
                        // KHỐI 2: GIAO DIỆN ĐÃ CHỌN LỚP HỌC PHẦN - HIỂN THỊ SINH VIÊN CHI TIẾT
                        // =================================================================
                        <>
                            <div className="students-header">
                                <div>
                                    <button
                                        onClick={() => setSelectedLHP(null)}
                                        style={{ display: 'flex', alignItems: 'center', gap: '8px', background: 'none', border: 'none', color: '#64748b', cursor: 'pointer', fontSize: '14px', fontWeight: '500', marginBottom: '10px', padding: 0 }}
                                    >
                                        <FaArrowLeft /> Quay lại danh sách lớp học phần
                                    </button>
                                    <h1>Danh sách lớp học phần: {selectedLHP}</h1>
                                    <p>Quản lý chi tiết tiến độ, điểm trung bình tích lũy bài tập của sinh viên trong học phần</p>
                                </div>

                                <div className="students-total">
                                    <FaUserGraduate />
                                    <span>{studentList.filter(s => s.lopHocPhanID === selectedLHP).length} thành viên</span>
                                </div>
                            </div>

                            <div className="students-search-box">
                                <FaSearch className="students-search-icon" />
                                <input
                                    type="text"
                                    placeholder="Tìm kiếm theo tên, mã SV, hoặc lớp hành chính..."
                                    value={searchTerm}
                                    onChange={(e) => setSearchTerm(e.target.value)}
                                />
                            </div>

                            {displayedStudents.length === 0 ? (
                                <div className="empty-state" style={{ marginTop: '20px' }}>Không tìm thấy sinh viên nào trong lớp học phần này.</div>
                            ) : (
                                <div className="students-grid">
                                    {displayedStudents.map((student) => {
                                        const progressPercent = student.tongBaiTap > 0
                                            ? Math.round((student.hoanThanh / student.tongBaiTap) * 100)
                                            : 0;

                                        return (
                                            <div
                                                key={student.sinhVienID}
                                                className="students-card"
                                                /* 🌟 ĐÃ SỬA: Thay thế hàm navigate cũ bằng cách gọi handleViewDetail chuẩn cấu trúc mới */
                                                onClick={() => handleViewDetail(student.sinhVienID)}
                                                style={{ cursor: 'pointer' }}
                                            >
                                                <div className="students-card-top">
                                                    <div className="students-avatar">
                                                        <FaUserGraduate />
                                                    </div>

                                                    <div className="students-info">
                                                        <h3>{student.fullName}</h3>
                                                        <p>{student.sinhVienID}</p>
                                                        <span className="students-class-badge">
                                                            Lớp quản lý: {student.lop}
                                                        </span>
                                                    </div>
                                                </div>

                                                <div className="students-stats-grid">
                                                    <div className="students-mini-box">
                                                        <span>Tổng bài</span>
                                                        <strong>{student.tongBaiTap}</strong>
                                                    </div>

                                                    <div className="students-mini-box green">
                                                        <span>Đã nộp</span>
                                                        <strong>{student.hoanThanh}</strong>
                                                    </div>

                                                    <div className="students-mini-box blue">
                                                        <span>Đang làm</span>
                                                        <strong>{student.dangLam}</strong>
                                                    </div>

                                                    <div className="students-mini-box orange">
                                                        <span>Quá hạn</span>
                                                        <strong>{student.quaHan}</strong>
                                                    </div>
                                                </div>

                                                <div className="students-progress-section">
                                                    <div className="students-progress-top">
                                                        <span>Tiến độ hoàn thành</span>
                                                        <strong>{progressPercent}%</strong>
                                                    </div>

                                                    <div className="students-progress-bar">
                                                        <div
                                                            className="students-progress-fill"
                                                            style={{ width: `${progressPercent}%` }}
                                                        ></div>
                                                    </div>
                                                </div>

                                                <div className="students-card-footer">
                                                    <div className="students-average">
                                                        <FaChartLine />
                                                        <span>
                                                            Điểm TB:
                                                            <strong> {student.diemTrungBinh > 0 ? student.diemTrungBinh.toFixed(1) : "---"}đ</strong>
                                                        </span>
                                                    </div>

                                                    <div className="students-groups">
                                                        <FaUsers />
                                                        <span>{student.soNhomThamGia} nhóm</span>
                                                    </div>
                                                </div>
                                            </div>
                                        );
                                    })}
                                </div>
                            )}
                        </>
                    )}
                </div>
            </div>
        </>
    );
}