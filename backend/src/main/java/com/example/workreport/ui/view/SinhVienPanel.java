package com.example.workreport.ui.view;

import com.example.workreport.model.SinhVien;
import com.example.workreport.model.User;
import com.example.workreport.repository.SinhVienRepository;
import com.example.workreport.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SinhVienPanel extends JPanel {

    @Autowired
    private SinhVienRepository sinhVienRepository;

    @Autowired
    private UserRepository userRepository;

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField txtID, txtName, txtEmail, txtPhone, txtLop, txtGpa, txtSearch;
    private JComboBox<String> cbKhoa;

    public SinhVienPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    @PostConstruct
    public void initUI() {
        // 1. Form nhập liệu (NORTH)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("  Thông tin Sinh viên  "));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtID = new JTextField(12);
        txtName = new JTextField(18);
        txtEmail = new JTextField(18);
        txtPhone = new JTextField(12);
        txtLop = new JTextField(12);
        cbKhoa = new JComboBox<>(new String[]{"Công nghệ thông tin", "Kinh tế - Quản lý", "Ngoại ngữ", "Điện tử Viễn thông", "Cơ khí - Tự động hóa"});
        txtGpa = new JTextField(6);

        // Row 0
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Mã SV (*) :"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; formPanel.add(txtID, gbc);
        gbc.gridx = 2; gbc.gridy = 0; formPanel.add(new JLabel("Họ và tên (*) :"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; formPanel.add(txtName, gbc);
        gbc.gridx = 4; gbc.gridy = 0; formPanel.add(new JLabel("Email :"), gbc);
        gbc.gridx = 5; gbc.gridy = 0; formPanel.add(txtEmail, gbc);

        // Row 1
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Số điện thoại :"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; formPanel.add(txtPhone, gbc);
        gbc.gridx = 2; gbc.gridy = 1; formPanel.add(new JLabel("Lớp :"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; formPanel.add(txtLop, gbc);
        gbc.gridx = 4; gbc.gridy = 1; formPanel.add(new JLabel("Khoa :"), gbc);
        gbc.gridx = 5; gbc.gridy = 1; formPanel.add(cbKhoa, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Điểm GPA :"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; formPanel.add(txtGpa, gbc);

        // 2. Buttons & Search Panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton btnAdd = new JButton("Thêm mới");
        JButton btnUpdate = new JButton("Cập nhật");
        JButton btnDelete = new JButton("Xóa");
        JButton btnClear = new JButton("Làm mới form");

        btnAdd.setIcon(UIManager.getIcon("Tree.closedIcon"));
        btnUpdate.setIcon(UIManager.getIcon("Tree.leafIcon"));
        btnDelete.setIcon(UIManager.getIcon("OptionPane.errorIcon"));

        txtSearch = new JTextField(15);
        JButton btnSearch = new JButton("Tìm kiếm");
        JButton btnReload = new JButton("Tải lại DB");

        actionPanel.add(btnAdd);
        actionPanel.add(btnUpdate);
        actionPanel.add(btnDelete);
        actionPanel.add(btnClear);
        actionPanel.add(new JLabel("   |   Tìm Mã/Tên: "));
        actionPanel.add(txtSearch);
        actionPanel.add(btnSearch);
        actionPanel.add(btnReload);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 6;
        formPanel.add(actionPanel, gbc);

        add(formPanel, BorderLayout.NORTH);

        // 3. Bảng dữ liệu (CENTER)
        String[] columns = {"Mã SV", "Họ và Tên", "Email", "Số điện thoại", "Lớp", "Khoa", "GPA"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép sửa trực tiếp trên ô
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(24);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Bắt sự kiện chọn dòng trên bảng
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                fillFormFromSelectedRow(table.getSelectedRow());
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);

        // 4. Xử lý sự kiện các nút
        btnAdd.addActionListener(e -> addSinhVien());
        btnUpdate.addActionListener(e -> updateSinhVien());
        btnDelete.addActionListener(e -> deleteSinhVien());
        btnClear.addActionListener(e -> clearForm());
        btnSearch.addActionListener(e -> searchSinhVien());
        btnReload.addActionListener(e -> loadData());
    }

    public void loadData() {
        tableModel.setRowCount(0);
        List<SinhVien> list = sinhVienRepository.findAll();
        for (SinhVien sv : list) {
            String fullname = (sv.getUser() != null && sv.getUser().getFullname() != null) ? sv.getUser().getFullname() : "";
            String email = (sv.getUser() != null && sv.getUser().getEmail() != null) ? sv.getUser().getEmail() : "";
            tableModel.addRow(new Object[]{
                    sv.getSinhVienID(),
                    fullname,
                    email,
                    sv.getPhone() != null ? sv.getPhone() : "",
                    sv.getLop() != null ? sv.getLop() : "",
                    sv.getKhoa() != null ? sv.getKhoa() : "",
                    sv.getGpa() != null ? sv.getGpa() : 0.0f
            });
        }
    }

    private void fillFormFromSelectedRow(int row) {
        txtID.setText(tableModel.getValueAt(row, 0).toString());
        txtID.setEditable(false); // Mã SV là khóa chính không cho sửa khi chọn
        txtName.setText(tableModel.getValueAt(row, 1).toString());
        txtEmail.setText(tableModel.getValueAt(row, 2).toString());
        txtPhone.setText(tableModel.getValueAt(row, 3).toString());
        txtLop.setText(tableModel.getValueAt(row, 4).toString());
        cbKhoa.setSelectedItem(tableModel.getValueAt(row, 5).toString());
        txtGpa.setText(tableModel.getValueAt(row, 6).toString());
    }

    private void clearForm() {
        txtID.setText("");
        txtID.setEditable(true);
        txtName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtLop.setText("");
        txtGpa.setText("");
        table.clearSelection();
    }

    private void addSinhVien() {
        String id = txtID.getText().trim();
        String name = txtName.getText().trim();
        if (id.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ Mã Sinh Viên và Họ Tên!", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (sinhVienRepository.existsById(id)) {
            JOptionPane.showMessageDialog(this, "Mã Sinh viên này đã tồn tại trong CSDL!", "Lỗi trùng lặp", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Tạo hoặc cập nhật User tương ứng
            User user = userRepository.findById(id).orElse(new User());
            user.setUserID(id);
            user.setUsername(id);
            user.setFullname(name);
            user.setEmail(txtEmail.getText().trim().isEmpty() ? id + "@sv.edu.vn" : txtEmail.getText().trim());
            user.setPhone(txtPhone.getText().trim());
            user.setRole("SINH_VIEN");
            userRepository.save(user);

            // Tạo SinhVien
            SinhVien sv = new SinhVien();
            sv.setSinhVienID(id);
            sv.setUser(user);
            sv.setLop(txtLop.getText().trim());
            sv.setKhoa(cbKhoa.getSelectedItem() != null ? cbKhoa.getSelectedItem().toString() : "");
            sv.setPhone(txtPhone.getText().trim());
            try {
                sv.setGpa(Float.parseFloat(txtGpa.getText().trim()));
            } catch (Exception ex) {
                sv.setGpa(0.0f);
            }

            sinhVienRepository.save(sv);
            JOptionPane.showMessageDialog(this, "Thêm mới sinh viên thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu dữ liệu: " + ex.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateSinhVien() {
        String id = txtID.getText().trim();
        if (id.isEmpty() || !sinhVienRepository.existsById(id)) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sinh viên từ bảng để cập nhật!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Optional<SinhVien> optSV = sinhVienRepository.findById(id);
            if (optSV.isPresent()) {
                SinhVien sv = optSV.get();
                sv.setLop(txtLop.getText().trim());
                sv.setKhoa(cbKhoa.getSelectedItem() != null ? cbKhoa.getSelectedItem().toString() : "");
                sv.setPhone(txtPhone.getText().trim());
                try {
                    sv.setGpa(Float.parseFloat(txtGpa.getText().trim()));
                } catch (Exception ex) {}

                User user = sv.getUser();
                if (user != null) {
                    user.setFullname(txtName.getText().trim());
                    user.setEmail(txtEmail.getText().trim());
                    user.setPhone(txtPhone.getText().trim());
                    userRepository.save(user);
                }

                sinhVienRepository.save(sv);
                JOptionPane.showMessageDialog(this, "Cập nhật thông tin sinh viên thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadData();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật: " + ex.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSinhVien() {
        String id = txtID.getText().trim();
        if (id.isEmpty() || !sinhVienRepository.existsById(id)) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sinh viên để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa sinh viên [" + id + "]?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                sinhVienRepository.deleteById(id);
                JOptionPane.showMessageDialog(this, "Đã xóa sinh viên thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Không thể xóa do ràng buộc dữ liệu (bài tập, nhóm...): " + ex.getMessage(), "Lỗi xóa", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void searchSinhVien() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            loadData();
            return;
        }
        tableModel.setRowCount(0);
        List<SinhVien> list = sinhVienRepository.findAll().stream()
                .filter(sv -> sv.getSinhVienID().toLowerCase().contains(keyword) ||
                        (sv.getUser() != null && sv.getUser().getFullname() != null && sv.getUser().getFullname().toLowerCase().contains(keyword)) ||
                        (sv.getLop() != null && sv.getLop().toLowerCase().contains(keyword)))
                .collect(Collectors.toList());

        for (SinhVien sv : list) {
            String fullname = (sv.getUser() != null && sv.getUser().getFullname() != null) ? sv.getUser().getFullname() : "";
            String email = (sv.getUser() != null && sv.getUser().getEmail() != null) ? sv.getUser().getEmail() : "";
            tableModel.addRow(new Object[]{
                    sv.getSinhVienID(),
                    fullname,
                    email,
                    sv.getPhone() != null ? sv.getPhone() : "",
                    sv.getLop() != null ? sv.getLop() : "",
                    sv.getKhoa() != null ? sv.getKhoa() : "",
                    sv.getGpa() != null ? sv.getGpa() : 0.0f
            });
        }
    }
}
