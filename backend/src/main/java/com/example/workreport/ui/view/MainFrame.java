package com.example.workreport.ui.view;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class MainFrame extends JFrame {

    @Autowired
    private SinhVienPanel sinhVienPanel;

    @Autowired
    private GiangVienPanel giangVienPanel;

    @Autowired
    private BaiTapPanel baiTapPanel;

    @Autowired
    private ThongKePanel thongKePanel;

    private JTabbedPane tabbedPane;

    public MainFrame() {
        super("Hệ Thống Quản Lý Công Việc & Bài Tập - Java Desktop Swing (OOP)");
    }

    @PostConstruct
    public void initUI() {
        setSize(1150, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1. Setup MenuBar
        JMenuBar menuBar = new JMenuBar();
        
        JMenu menuSys = new JMenu("Hệ thống");
        JMenuItem itemRefreshAll = new JMenuItem("Làm mới toàn bộ dữ liệu");
        itemRefreshAll.addActionListener(e -> refreshAllTabs());
        JMenuItem itemExit = new JMenuItem("Thoát chương trình");
        itemExit.addActionListener(e -> System.exit(0));
        menuSys.add(itemRefreshAll);
        menuSys.addSeparator();
        menuSys.add(itemExit);

        JMenu menuManage = new JMenu("Quản lý");
        JMenuItem itemTabSV = new JMenuItem("Quản lý Sinh viên");
        itemTabSV.addActionListener(e -> tabbedPane.setSelectedIndex(0));
        JMenuItem itemTabGV = new JMenuItem("Quản lý Giảng viên");
        itemTabGV.addActionListener(e -> tabbedPane.setSelectedIndex(1));
        JMenuItem itemTabBT = new JMenuItem("Quản lý Bài tập");
        itemTabBT.addActionListener(e -> tabbedPane.setSelectedIndex(2));
        JMenuItem itemTabTK = new JMenuItem("Thống kê & Biểu đồ");
        itemTabTK.addActionListener(e -> tabbedPane.setSelectedIndex(3));
        menuManage.add(itemTabSV);
        menuManage.add(itemTabGV);
        menuManage.add(itemTabBT);
        menuManage.add(itemTabTK);

        JMenu menuHelp = new JMenu("Trợ giúp");
        JMenuItem itemAbout = new JMenuItem("Giới thiệu Đề tài");
        itemAbout.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Bài Tập Lớn Môn Lập Trình Hướng Đối Tượng (Java)\n\n" +
                "• Giao diện: Java Swing Desktop (JFrame, JTable, JTabbedPane,...)\n" +
                "• Kiến trúc: OOP MVC + Layered Architecture (Model - View - Controller - Service - Repository)\n" +
                "• Cơ sở dữ liệu: MySQL (ql_cv) via JDBC / JPA Hibernate\n" +
                "• Tính năng OOP: Đóng gói (Encapsulation), Kế thừa (Inheritance), Đa hình (Polymorphism),\n" +
                "  Trừu tượng (Abstraction), Biểu đồ custom Graphics2D.\n\n" +
                "Phiên bản: 1.0.0 Desktop Swing Edition",
                "Giới Thiệu Đề Tài", JOptionPane.INFORMATION_MESSAGE));
        menuHelp.add(itemAbout);

        menuBar.add(menuSys);
        menuBar.add(menuManage);
        menuBar.add(menuHelp);
        setJMenuBar(menuBar);

        // 2. Setup TabbedPane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));

        tabbedPane.addTab("  Quản lý Sinh viên  ", sinhVienPanel);
        tabbedPane.addTab("  Quản lý Giảng viên  ", giangVienPanel);
        tabbedPane.addTab("  Quản lý Bài tập  ", baiTapPanel);
        tabbedPane.addTab("  Thống kê & Biểu đồ  ", thongKePanel);

        // Tự động làm mới dữ liệu khi chuyển Tab
        tabbedPane.addChangeListener(e -> {
            int idx = tabbedPane.getSelectedIndex();
            if (idx == 0 && sinhVienPanel != null) sinhVienPanel.loadData();
            else if (idx == 1 && giangVienPanel != null) giangVienPanel.loadData();
            else if (idx == 2 && baiTapPanel != null) baiTapPanel.loadData();
            else if (idx == 3 && thongKePanel != null) thongKePanel.loadData();
        });

        add(tabbedPane, BorderLayout.CENTER);

        // 3. Setup Status Bar
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusBar.setBackground(new Color(240, 240, 240));
        JLabel lblStatus = new JLabel("  ● Trạng thái: Kết nối MySQL (ql_cv) thành công | Giao diện: Java Swing Desktop | Kiến trúc: OOP MVC + Repository Pattern");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setForeground(new Color(40, 120, 40));
        statusBar.add(lblStatus, BorderLayout.WEST);
        add(statusBar, BorderLayout.SOUTH);
    }

    public void refreshAllTabs() {
        if (sinhVienPanel != null) sinhVienPanel.loadData();
        if (giangVienPanel != null) giangVienPanel.loadData();
        if (baiTapPanel != null) baiTapPanel.loadData();
        if (thongKePanel != null) thongKePanel.loadData();
        JOptionPane.showMessageDialog(this, "Đã làm mới toàn bộ dữ liệu từ MySQL!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
}
