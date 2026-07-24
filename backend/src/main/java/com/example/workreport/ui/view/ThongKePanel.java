package com.example.workreport.ui.view;

import com.example.workreport.repository.BaiTapRepository;
import com.example.workreport.repository.GiangVienRepository;
import com.example.workreport.repository.LopHocRepository;
import com.example.workreport.repository.SinhVienRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class ThongKePanel extends JPanel {

    @Autowired
    private SinhVienRepository sinhVienRepository;

    @Autowired
    private GiangVienRepository giangVienRepository;

    @Autowired
    private BaiTapRepository baiTapRepository;

    @Autowired
    private LopHocRepository lopHocRepository;

    private JLabel lblTotalSV, lblTotalGV, lblTotalBT, lblTotalLop;
    private CustomBarChartPanel chartPanel;

    private long countSV = 0;
    private long countGV = 0;
    private long countLop = 0;
    private long countBT = 0;

    public ThongKePanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(245, 247, 250));
    }

    @PostConstruct
    public void initUI() {
        // 1. Top Summary Cards (GridLayout 1x4)
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        cardsPanel.setOpaque(false);

        lblTotalSV = createSummaryCard(cardsPanel, "Tổng Sinh Viên", new Color(52, 152, 219));
        lblTotalGV = createSummaryCard(cardsPanel, "Tổng Giảng Viên", new Color(46, 204, 113));
        lblTotalLop = createSummaryCard(cardsPanel, "Tổng Lớp Học Phần", new Color(155, 89, 182));
        lblTotalBT = createSummaryCard(cardsPanel, "Tổng Bài Tập", new Color(230, 126, 34));

        add(cardsPanel, BorderLayout.NORTH);

        // 2. Custom Painted OOP Chart (CENTER)
        chartPanel = new CustomBarChartPanel();
        chartPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                "  Biểu Đồ Thống Kê Số Lượng Thực Thể Trong Hệ Thống (Vẽ tự động bằng OOP Graphics2D)  "
        ));
        chartPanel.setBackground(Color.WHITE);

        add(chartPanel, BorderLayout.CENTER);

        // 3. Bottom Controls
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        JButton btnRefresh = new JButton("Làm mới thống kê & Biểu đồ");
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRefresh.addActionListener(e -> loadData());
        bottomPanel.add(btnRefresh);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JLabel createSummaryCard(JPanel parent, String title, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle.setForeground(color);

        JLabel lblValue = new JLabel("0");
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblValue.setForeground(new Color(40, 40, 40));

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        parent.add(card);
        return lblValue;
    }

    public void loadData() {
        try {
            countSV = sinhVienRepository.count();
            countGV = giangVienRepository.count();
            countLop = lopHocRepository.count();
            countBT = baiTapRepository.count();

            lblTotalSV.setText(String.valueOf(countSV));
            lblTotalGV.setText(String.valueOf(countGV));
            lblTotalLop.setText(String.valueOf(countLop));
            lblTotalBT.setText(String.valueOf(countBT));

            if (chartPanel != null) {
                chartPanel.setData(countSV, countGV, countLop, countBT);
                chartPanel.repaint();
            }
        } catch (Exception ex) {
            System.err.println("Lỗi loadData thống kê: " + ex.getMessage());
        }
    }

    /**
     * Lớp nội bộ kế thừa JPanel thể hiện kỹ thuật đồ họa custom (Polymorphism & Abstraction)
     */
    private class CustomBarChartPanel extends JPanel {
        private long svCount = 0, gvCount = 0, lopCount = 0, btCount = 0;

        public void setData(long sv, long gv, long lop, long bt) {
            this.svCount = sv;
            this.gvCount = gv;
            this.lopCount = lop;
            this.btCount = bt;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int padding = 60;

            // Draw axis
            g2.setColor(new Color(150, 150, 150));
            g2.setStroke(new BasicStroke(2f));
            g2.drawLine(padding, height - padding, width - padding, height - padding); // X axis
            g2.drawLine(padding, height - padding, padding, padding); // Y axis

            long maxVal = Math.max(Math.max(svCount, gvCount), Math.max(lopCount, btCount));
            if (maxVal < 10) maxVal = 10;

            String[] labels = {"Sinh Viên", "Giảng Viên", "Lớp Học", "Bài Tập"};
            long[] values = {svCount, gvCount, lopCount, btCount};
            Color[] colors = {new Color(52, 152, 219), new Color(46, 204, 113), new Color(155, 89, 182), new Color(230, 126, 34)};

            int barWidth = 70;
            int gap = (width - 2 * padding - 4 * barWidth) / 5;

            for (int i = 0; i < 4; i++) {
                int barHeight = (int) ((double) values[i] / maxVal * (height - 2 * padding - 30));
                int x = padding + gap + i * (barWidth + gap);
                int y = height - padding - barHeight;

                // Draw Bar
                g2.setColor(colors[i]);
                g2.fillRoundRect(x, y, barWidth, barHeight, 8, 8);

                // Draw Value Label
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
                String valStr = String.valueOf(values[i]);
                int strWidth = g2.getFontMetrics().stringWidth(valStr);
                g2.drawString(valStr, x + (barWidth - strWidth) / 2, y - 8);

                // Draw X Label
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                int labelWidth = g2.getFontMetrics().stringWidth(labels[i]);
                g2.drawString(labels[i], x + (barWidth - labelWidth) / 2, height - padding + 20);
            }
        }
    }
}
