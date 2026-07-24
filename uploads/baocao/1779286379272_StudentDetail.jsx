import React, { useState, useEffect } from "react";
import Header from "../components/Header";
import NavMenu from "../components/NavMenu";
import "../style/StudentDetail.css";

import {
    FaArrowLeft,
    FaUserGraduate,
    FaFileDownload,
    FaClock,
    FaCheckCircle,
    FaTimesCircle,
    FaChartLine
} from "react-icons/fa";

import { useNavigate, useParams } from "react-router-dom";
import api from "../../api/api"; // Đảm bảo đúng đường dẫn tới file cấu hình axios của bạn

export default function StudentDetail() {
    const navigate = useNavigate();

    // 🌟 1. SỬA TẠI ĐÂY: Lấy cả lopID và id (sinhVienID) từ thanh URL bốc xuống
    const { lopID, id } = useParams();

    const [studentProfile, setStudentProfile] = useState(null);
    const [dbAssignments, setDbAssignments] = useState([]);
    const [loading, setLoading] = useState(true);

    // 🌟 2. SỬA TẠI ĐÂY: Gọi API đồng bộ dữ liệu theo cặp (Lớp học phần + Sinh viên)
    useEffect(() => {
        const fetchStudentData = async () => {
            try {
                setLoading(true);

                // API 1: Lấy thông tin cá nhân sinh viên (@GetMapping("/{id}"))
                const profileRes = await api.get(`/api/sinhvien/${id}`);

                // API 2: Gọi chính xác endpoint mới chứa cả id và lopID (Bẻ gãy việc lôi bài tập lớp khác vào)
                const assignmentsRes = await api.get(`/api/sinhvien/${id}/lop/${lopID}/tien-do-bai-tap`);

                if (profileRes && profileRes.success) {
                    setStudentProfile(profileRes.data);
                }

                if (assignmentsRes && assignmentsRes.success) {
                    setDbAssignments(assignmentsRes.data);
                }
            } catch (error) {
                console.error("Lỗi tải tiến độ chi tiết sinh viên:", error);
            } finally {
                setLoading(false);
            }
        };

        // Chỉ kích hoạt khi thanh URL cung cấp đầy đủ mã lớp và mã sinh viên
        if (id && lopID) {
            fetchStudentData();
        }
    }, [id, lopID]);

    if (loading) {
        return (
            <>
                <Header />
                <NavMenu />
                <div className="profile-loading" style={{ textAlign: 'center', marginTop: '100px', fontSize: '16px', color: '#64748b' }}>
                    Đang tổng hợp tiến độ chi tiết của sinh viên từ hệ thống...
                </div>
            </>
        );
    }

    // 🌟 3. THUẬT TOÁN TỰ ĐỘNG TÍNH TOÁN CÁC CHỈ SỐ PROFILE (Chỉ tính dựa trên các bài tập của lớp hiện tại)
    const totalCount = dbAssignments.length;
    const completedCount = dbAssignments.filter(item => item.trangThaiNop !== "Chua nop").length;
    const progressPercent = totalCount > 0 ? Math.round((completedCount / totalCount) * 100) : 0;

    const gradedAssignments = dbAssignments.filter(item => item.diem !== null);
    const avgScore = gradedAssignments.length > 0
        ? (gradedAssignments.reduce((sum, item) => sum + item.diem, 0) / gradedAssignments.length).toFixed(1)
        : "---";

    // 🌟 4. ÁNH XẠ DỮ LIỆU (MAPPING): Chuyển đổi DTO sang cấu trúc mảng hiển thị cũ của bạn
    const assignments = dbAssignments.map((item, idx) => {
        let statusString = "Chưa nộp";
        if (item.trangThaiNop === "Da cham") statusString = "Đã chấm";
        if (item.trangThaiNop === "Da nop") statusString = "Đã nộp";

        return {
            id: idx + 1,
            title: item.tenBaiTap,
            status: statusString,
            score: item.diem,
            desc: item.moTa,
            file: item.fileBaoCao,
            time: item.ngayNop,
            feedback: item.nhanXet
        };
    });

    return (
        <>
            <Header />
            <NavMenu />

            <div className="student-detail-page">
                <div className="student-detail-container">

                    <div className="student-detail-back">
                        <button onClick={() => navigate(-1)}>
                            <FaArrowLeft />
                            Quay lại danh sách sinh viên
                        </button>
                    </div>

                    {/* ===== PROFILE CARD ===== */}
                    {studentProfile && (
                        <div className="student-profile-card">
                            <div className="student-profile-left">
                                <div className="student-profile-avatar">
                                    <FaUserGraduate />
                                </div>

                                <div className="student-profile-info">
                                    {/* 🌟 Đã sửa: Gọi trực tiếp fullName và email viết hoa viết thường chuẩn đét theo Alias SQL */}
                                    <h1>{studentProfile.fullName || "Sinh viên PTIT"}</h1>
                                    <p>Mã sinh viên: {studentProfile.sinhVienID}</p>

                                    <div className="student-profile-meta">
                                        <span className="student-class-tag">
                                            {studentProfile.lop}
                                        </span>
                                        <div className="student-email">
                                            ✉ {studentProfile.user?.email || "student@ptit.edu.vn"}
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div className="student-profile-stats">
                                <div className="student-profile-stat">
                                    <span>Điểm TB</span>
                                    <strong>{avgScore}</strong>
                                </div>

                                <div className="student-profile-stat">
                                    <span>Hoàn thành</span>
                                    <strong>{completedCount} / {totalCount}</strong>
                                </div>

                                <div className="student-profile-stat">
                                    <span>Tiến độ</span>
                                    <strong>{progressPercent}%</strong>
                                </div>
                            </div>
                        </div>
                    )}

                    {/* ===== SECTION DANH SÁCH BÀI TẬP ĐỘNG THEO LỚP ===== */}
                    <div className="student-detail-section">
                        <div className="student-detail-title-row">
                            <h2>Danh sách bài tập học phần: {lopID}</h2>
                            <span>{totalCount} bài tập</span>
                        </div>

                        {assignments.map((item) => (
                            <div
                                key={item.id}
                                className="student-assignment-card"
                            >
                                <div className="student-assignment-top">
                                    <div>
                                        <h3>{item.title}</h3>
                                        <p>{item.desc || "Chưa có bài nộp"}</p>
                                    </div>

                                    {item.status === "Đã chấm" && (
                                        <div className="student-status green">
                                            <FaCheckCircle />
                                            Đã chấm
                                        </div>
                                    )}

                                    {item.status === "Đã nộp" && (
                                        <div className="student-status blue">
                                            <FaClock />
                                            Đã nộp
                                        </div>
                                    )}

                                    {item.status === "Chưa nộp" && (
                                        <div className="student-status red">
                                            <FaTimesCircle />
                                            Chưa nộp
                                        </div>
                                    )}
                                </div>

                                {(item.status !== "Chưa nộp") && (
                                    <>
                                        <div className="student-file-row">
                                            <FaFileDownload />
                                            <a
                                                href={`http://localhost:8081/files/${item.file}`}
                                                target="_blank"
                                                rel="noreferrer"
                                                style={{ textDecoration: 'none', color: 'inherit', marginLeft: '5px', fontWeight: '500' }}
                                            >
                                                {item.file}
                                            </a>
                                        </div>

                                        <div className="student-time-row">
                                            <FaClock />
                                            <span>Nộp lúc: {item.time}</span>
                                        </div>
                                    </>
                                )}

                                {item.feedback && (
                                    <div className="student-feedback-box">
                                        <strong>Nhận xét</strong>
                                        <p>{item.feedback}</p>
                                    </div>
                                )}

                                <div className="student-assignment-footer">
                                    <div className="student-score-row">
                                        <FaChartLine />
                                        <span>
                                            Điểm:
                                            <strong>
                                                {" "}
                                                {item.score ?? "--"}
                                            </strong>
                                        </span>
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>

                </div>
            </div>
        </>
    );
}