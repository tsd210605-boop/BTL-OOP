package com.example.workreport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OverviewDTO {
    // 4 số thống kê ở Card Header
    private long activeAssignmentsCount;
    private long totalSubmissionsCount;
    private long gradedSubmissionsCount;
    private long pendingSubmissionsCount;

    // 2 danh sách list bên dưới
    private List<OverviewAssignment> activeAssignments;
    private List<OverviewSubmission> recentSubmissions;

    @Data
    @AllArgsConstructor
    public static class OverviewAssignment {
        private String id;
        private String title;
        private String desc;
        private String due;
        private String submitted; // Chuỗi định dạng "đã nộp / tổng sĩ số" (Ví dụ: 4/8)
        private int progressPercent;
        private String pending;   // Chuỗi thông báo "X bài chờ chấm"
    }

    @Data
    @AllArgsConstructor
    public static class OverviewSubmission {
        private String student;
        private String assignment;
        private String time;
        private String status; // "late", "pending", "graded"
        private Double score;
    }
}