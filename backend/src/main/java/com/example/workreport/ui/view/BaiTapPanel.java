package com.example.workreport.ui.view;

import com.example.workreport.model.BaiTap;
import com.example.workreport.model.LopHoc;
import com.example.workreport.repository.BaiTapRepository;
import com.example.workreport.repository.LopHocRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BaiTapPanel extends JPanel {

    @Autowired
    private BaiTapRepository baiTapRepository;

    @Autowired
    private LopHocRepository lopHocRepository;

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField txtID, txtName, txtMoTa, txtDeadline, txtLopID, txtSearch;
    private JComboBox<String> cbLoai, cbTrangThai;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public BaiTapPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    @PostConstruct
    public void initUI() {
        // 1. Form nhập liệu (NORTH)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("  Thông tin Bài tập / Công việc  "));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtID = new JTextField(12);
        txtName = new JTextField(18);
        txtMoTa = new JTextField(18);
        txtDeadline = new JTextField("2026-08-30 23:59", 14);
        txtLopID = new JTextField(10);
        cbLoai = new JComboBox<>(new String[]{"Ca nhan", "Nhom"});
        cbTrangThai = new JComboBox<>(new String[]{"Dang hoat dong", "Da dong"});

        // Row 0
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Mã Bài tập (*) :"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; formPanel.add(txtID, gbc);
        gbc.gridx = 2; gbc.gridy = 0; formPanel.add(new JLabel("Tiêu đề (*) :"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; formPanel.add(txtName, gbc);
        gbc.gridx = 4; gbc.gridy = 0; formPanel.add(new JLabel("Loại bài tập :"), gbc);
        gbc.gridx = 5; gbc.gridy = 0; formPanel.add(cbLoai, gbc);

        // Row 1
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Hạn nộp (yyyy-MM-dd HH:mm) :"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; formPanel.add(txtDeadline, gbc);
        gbc.gridx = 2; gbc.gridy = 1; formPanel.add(new JLabel("Trạng thái :"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; formPanel.add(cbTrangThai, gbc);
        gbc.gridx = 4; gbc.gridy = 1; formPanel.add(new JLabel("Lớp học phần ID :"), gbc);
        gbc.gridx = 5; gbc.gridy = 1; formPanel.add(txtLopID, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Mô tả / Yêu cầu :"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 5; formPanel.add(txtMoTa, gbc);
        gbc.gridwidth = 1;

        // 2. Buttons & Search Panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton btnAdd = new JButton("Thêm mới");
        JButton btnUpdate = new JButton("Cập nhật");
        JButton btnDelete = new JButton("Xóa");
        JButton btnClear = new JButton("Làm mới form");

        txtSearch = new JTextField(15);
        JButton btnSearch = new JButton("Tìm kiếm");
        JButton btnReload = new JButton("Tải lại DB");

        actionPanel.add(btnAdd);
        actionPanel.add(btnUpdate);
        actionPanel.add(btnDelete);
        actionPanel.add(btnClear);
        actionPanel.add(new JLabel("   |   Tìm Mã/Tiêu đề: "));
        actionPanel.add(txtSearch);
        actionPanel.add(btnSearch);
        actionPanel.add(btnReload);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 6;
        formPanel.add(actionPanel, gbc);

        add(formPanel, BorderLayout.NORTH);

        // 3. Bảng dữ liệu (CENTER)
        String[] columns = {"Mã BT", "Tiêu đề", "Mô tả", "Hạn nộp (Deadline)", "Loại", "Trạng thái", "Lớp học phần ID"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(24);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                fillFormFromSelectedRow(table.getSelectedRow());
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);

        // 4. Xử lý sự kiện các nút
        btnAdd.addActionListener(e -> addBaiTap());
        btnUpdate.addActionListener(e -> updateBaiTap());
        btnDelete.addActionListener(e -> deleteBaiTap());
        btnClear.addActionListener(e -> clearForm());
        btnSearch.addActionListener(e -> searchBaiTap());
        btnReload.addActionListener(e -> loadData());
    }

    public void loadData() {
        tableModel.setRowCount(0);
        List<BaiTap> list = baiTapRepository.findAll();
        for (BaiTap bt : list) {
            String dl = bt.getDeadline() != null ? bt.getDeadline().format(formatter) : "";
            String lop = (bt.getLopHoc() != null && bt.getLopHoc().getLopID() != null) ? bt.getLopHoc().getLopID() : "";
            tableModel.addRow(new Object[]{
                    bt.getBaiTapID(),
                    bt.getTenBaiTap(),
                    bt.getMoTa() != null ? bt.getMoTa() : "",
                    dl,
                    bt.getLoai() != null ? bt.getLoai() : "",
                    bt.getTrangThai() != null ? bt.getTrangThai() : "Dang hoat dong",
                    lop
            });
        }
    }

    private void fillFormFromSelectedRow(int row) {
        txtID.setText(tableModel.getValueAt(row, 0).toString());
        txtID.setEditable(false);
        txtName.setText(tableModel.getValueAt(row, 1).toString());
        txtMoTa.setText(tableModel.getValueAt(row, 2).toString());
        txtDeadline.setText(tableModel.getValueAt(row, 3).toString());
        cbLoai.setSelectedItem(tableModel.getValueAt(row, 4).toString());
        cbTrangThai.setSelectedItem(tableModel.getValueAt(row, 5).toString());
        txtLopID.setText(tableModel.getValueAt(row, 6).toString());
    }

    private void clearForm() {
        txtID.setText("");
        txtID.setEditable(true);
        txtName.setText("");
        txtMoTa.setText("");
        txtDeadline.setText("2026-08-30 23:59");
        txtLopID.setText("");
        table.clearSelection();
    }

    private void addBaiTap() {
        String id = txtID.getText().trim();
        String name = txtName.getText().trim();
        if (id.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã Bài Tập và Tiêu đề!", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (baiTapRepository.existsById(id)) {
            JOptionPane.showMessageDialog(this, "Mã bài tập đã tồn tại!", "Lỗi trùng lặp", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            BaiTap bt = new BaiTap();
            bt.setBaiTapID(id);
            bt.setTenBaiTap(name);
            bt.setMoTa(txtMoTa.getText().trim());
            bt.setLoai(cbLoai.getSelectedItem() != null ? cbLoai.getSelectedItem().toString() : "Ca nhan");
            bt.setTrangThai(cbTrangThai.getSelectedItem() != null ? cbTrangThai.getSelectedItem().toString() : "Dang hoat dong");
            bt.setDiemToiDa(10.0f);

            try {
                bt.setDeadline(LocalDateTime.parse(txtDeadline.getText().trim(), formatter));
            } catch (Exception ex) {
                bt.setDeadline(LocalDateTime.now().plusDays(7));
            }

            String lopId = txtLopID.getText().trim();
            if (!lopId.isEmpty()) {
                Optional<LopHoc> optLop = lopHocRepository.findById(lopId);
                optLop.ifPresent(bt::setLopHoc);
            }

            baiTapRepository.save(bt);
            JOptionPane.showMessageDialog(this, "Thêm bài tập mới thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu dữ liệu: " + ex.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateBaiTap() {
        String id = txtID.getText().trim();
        if (id.isEmpty() || !baiTapRepository.existsById(id)) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một bài tập từ bảng để cập nhật!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Optional<BaiTap> optBT = baiTapRepository.findById(id);
            if (optBT.isPresent()) {
                BaiTap bt = optBT.get();
                bt.setTenBaiTap(txtName.getText().trim());
                bt.setMoTa(txtMoTa.getText().trim());
                bt.setLoai(cbLoai.getSelectedItem() != null ? cbLoai.getSelectedItem().toString() : "Ca nhan");
                bt.setTrangThai(cbTrangThai.getSelectedItem() != null ? cbTrangThai.getSelectedItem().toString() : "Dang hoat dong");

                try {
                    bt.setDeadline(LocalDateTime.parse(txtDeadline.getText().trim(), formatter));
                } catch (Exception ex) {}

                String lopId = txtLopID.getText().trim();
                if (!lopId.isEmpty()) {
                    Optional<LopHoc> optLop = lopHocRepository.findById(lopId);
                    optLop.ifPresent(bt::setLopHoc);
                } else {
                    bt.setLopHoc(null);
                }

                baiTapRepository.save(bt);
                JOptionPane.showMessageDialog(this, "Cập nhật bài tập thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadData();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật: " + ex.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteBaiTap() {
        String id = txtID.getText().trim();
        if (id.isEmpty() || !baiTapRepository.existsById(id)) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một bài tập để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa bài tập [" + id + "]?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                baiTapRepository.deleteById(id);
                JOptionPane.showMessageDialog(this, "Đã xóa bài tập thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Không thể xóa do ràng buộc báo cáo/nhóm: " + ex.getMessage(), "Lỗi xóa", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void searchBaiTap() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            loadData();
            return;
        }
        tableModel.setRowCount(0);
        List<BaiTap> list = baiTapRepository.findAll().stream()
                .filter(bt -> bt.getBaiTapID().toLowerCase().contains(keyword) ||
                        (bt.getTenBaiTap() != null && bt.getTenBaiTap().toLowerCase().contains(keyword)) ||
                        (bt.getLoai() != null && bt.getLoai().toLowerCase().contains(keyword)))
                .collect(Collectors.toList());

        for (BaiTap bt : list) {
            String dl = bt.getDeadline() != null ? bt.getDeadline().format(formatter) : "";
            String lop = (bt.getLopHoc() != null && bt.getLopHoc().getLopID() != null) ? bt.getLopHoc().getLopID() : "";
            tableModel.addRow(new Object[]{
                    bt.getBaiTapID(),
                    bt.getTenBaiTap(),
                    bt.getMoTa() != null ? bt.getMoTa() : "",
                    dl,
                    bt.getLoai() != null ? bt.getLoai() : "",
                    bt.getTrangThai() != null ? bt.getTrangThai() : "Dang hoat dong",
                    lop
            });
        }
    }
}
