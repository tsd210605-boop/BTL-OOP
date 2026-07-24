package com.example.workreport.ui.view;

import com.example.workreport.model.GiangVien;
import com.example.workreport.model.User;
import com.example.workreport.repository.GiangVienRepository;
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
public class GiangVienPanel extends JPanel {

    @Autowired
    private GiangVienRepository giangVienRepository;

    @Autowired
    private UserRepository userRepository;

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField txtID, txtName, txtEmail, txtPhone, txtDegree, txtSearch;
    private JComboBox<String> cbKhoa;

    public GiangVienPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    @PostConstruct
    public void initUI() {
        // 1. Form nhập liệu (NORTH)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("  Thông tin Giảng viên  "));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtID = new JTextField(12);
        txtName = new JTextField(18);
        txtEmail = new JTextField(18);
        txtPhone = new JTextField(12);
        txtDegree = new JTextField(15);
        cbKhoa = new JComboBox<>(new String[]{"Công nghệ thông tin", "Kinh tế - Quản lý", "Ngoại ngữ", "Điện tử Viễn thông", "Cơ khí - Tự động hóa"});

        // Row 0
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Mã GV (*) :"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; formPanel.add(txtID, gbc);
        gbc.gridx = 2; gbc.gridy = 0; formPanel.add(new JLabel("Họ và tên (*) :"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; formPanel.add(txtName, gbc);
        gbc.gridx = 4; gbc.gridy = 0; formPanel.add(new JLabel("Email :"), gbc);
        gbc.gridx = 5; gbc.gridy = 0; formPanel.add(txtEmail, gbc);

        // Row 1
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Số điện thoại :"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; formPanel.add(txtPhone, gbc);
        gbc.gridx = 2; gbc.gridy = 1; formPanel.add(new JLabel("Học hàm/Học vị :"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; formPanel.add(txtDegree, gbc);
        gbc.gridx = 4; gbc.gridy = 1; formPanel.add(new JLabel("Khoa :"), gbc);
        gbc.gridx = 5; gbc.gridy = 1; formPanel.add(cbKhoa, gbc);

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
        actionPanel.add(new JLabel("   |   Tìm Mã/Tên: "));
        actionPanel.add(txtSearch);
        actionPanel.add(btnSearch);
        actionPanel.add(btnReload);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 6;
        formPanel.add(actionPanel, gbc);

        add(formPanel, BorderLayout.NORTH);

        // 3. Bảng dữ liệu (CENTER)
        String[] columns = {"Mã GV", "Họ và Tên", "Email", "Số điện thoại", "Học vị", "Khoa"};
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
        btnAdd.addActionListener(e -> addGiangVien());
        btnUpdate.addActionListener(e -> updateGiangVien());
        btnDelete.addActionListener(e -> deleteGiangVien());
        btnClear.addActionListener(e -> clearForm());
        btnSearch.addActionListener(e -> searchGiangVien());
        btnReload.addActionListener(e -> loadData());
    }

    public void loadData() {
        tableModel.setRowCount(0);
        List<GiangVien> list = giangVienRepository.findAll();
        for (GiangVien gv : list) {
            String fullname = (gv.getUser() != null && gv.getUser().getFullname() != null) ? gv.getUser().getFullname() : "";
            String email = (gv.getUser() != null && gv.getUser().getEmail() != null) ? gv.getUser().getEmail() : "";
            tableModel.addRow(new Object[]{
                    gv.getGiangVienID(),
                    fullname,
                    email,
                    gv.getSoDienThoai() != null ? gv.getSoDienThoai() : "",
                    gv.getHocVi() != null ? gv.getHocVi() : "",
                    gv.getKhoa() != null ? gv.getKhoa() : ""
            });
        }
    }

    private void fillFormFromSelectedRow(int row) {
        txtID.setText(tableModel.getValueAt(row, 0).toString());
        txtID.setEditable(false);
        txtName.setText(tableModel.getValueAt(row, 1).toString());
        txtEmail.setText(tableModel.getValueAt(row, 2).toString());
        txtPhone.setText(tableModel.getValueAt(row, 3).toString());
        txtDegree.setText(tableModel.getValueAt(row, 4).toString());
        cbKhoa.setSelectedItem(tableModel.getValueAt(row, 5).toString());
    }

    private void clearForm() {
        txtID.setText("");
        txtID.setEditable(true);
        txtName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtDegree.setText("");
        table.clearSelection();
    }

    private void addGiangVien() {
        String id = txtID.getText().trim();
        String name = txtName.getText().trim();
        if (id.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ Mã Giảng Viên và Họ Tên!", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (giangVienRepository.existsById(id)) {
            JOptionPane.showMessageDialog(this, "Mã Giảng viên này đã tồn tại trong CSDL!", "Lỗi trùng lặp", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            User user = userRepository.findById(id).orElse(new User());
            user.setUserID(id);
            user.setUsername(id);
            user.setFullname(name);
            user.setEmail(txtEmail.getText().trim().isEmpty() ? id + "@gv.edu.vn" : txtEmail.getText().trim());
            user.setPhone(txtPhone.getText().trim());
            user.setRole("GIANG_VIEN");
            userRepository.save(user);

            GiangVien gv = new GiangVien();
            gv.setGiangVienID(id);
            gv.setUser(user);
            gv.setHocVi(txtDegree.getText().trim());
            gv.setKhoa(cbKhoa.getSelectedItem() != null ? cbKhoa.getSelectedItem().toString() : "");
            gv.setSoDienThoai(txtPhone.getText().trim());

            giangVienRepository.save(gv);
            JOptionPane.showMessageDialog(this, "Thêm mới giảng viên thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu dữ liệu: " + ex.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateGiangVien() {
        String id = txtID.getText().trim();
        if (id.isEmpty() || !giangVienRepository.existsById(id)) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một giảng viên từ bảng để cập nhật!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Optional<GiangVien> optGV = giangVienRepository.findById(id);
            if (optGV.isPresent()) {
                GiangVien gv = optGV.get();
                gv.setHocVi(txtDegree.getText().trim());
                gv.setKhoa(cbKhoa.getSelectedItem() != null ? cbKhoa.getSelectedItem().toString() : "");
                gv.setSoDienThoai(txtPhone.getText().trim());

                User user = gv.getUser();
                if (user != null) {
                    user.setFullname(txtName.getText().trim());
                    user.setEmail(txtEmail.getText().trim());
                    user.setPhone(txtPhone.getText().trim());
                    userRepository.save(user);
                }

                giangVienRepository.save(gv);
                JOptionPane.showMessageDialog(this, "Cập nhật thông tin giảng viên thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadData();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật: " + ex.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteGiangVien() {
        String id = txtID.getText().trim();
        if (id.isEmpty() || !giangVienRepository.existsById(id)) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một giảng viên để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa giảng viên [" + id + "]?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                giangVienRepository.deleteById(id);
                JOptionPane.showMessageDialog(this, "Đã xóa giảng viên thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Không thể xóa do ràng buộc dữ liệu (lớp học, bài tập...): " + ex.getMessage(), "Lỗi xóa", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void searchGiangVien() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            loadData();
            return;
        }
        tableModel.setRowCount(0);
        List<GiangVien> list = giangVienRepository.findAll().stream()
                .filter(gv -> gv.getGiangVienID().toLowerCase().contains(keyword) ||
                        (gv.getUser() != null && gv.getUser().getFullname() != null && gv.getUser().getFullname().toLowerCase().contains(keyword)) ||
                        (gv.getKhoa() != null && gv.getKhoa().toLowerCase().contains(keyword)))
                .collect(Collectors.toList());

        for (GiangVien gv : list) {
            String fullname = (gv.getUser() != null && gv.getUser().getFullname() != null) ? gv.getUser().getFullname() : "";
            String email = (gv.getUser() != null && gv.getUser().getEmail() != null) ? gv.getUser().getEmail() : "";
            tableModel.addRow(new Object[]{
                    gv.getGiangVienID(),
                    fullname,
                    email,
                    gv.getSoDienThoai() != null ? gv.getSoDienThoai() : "",
                    gv.getHocVi() != null ? gv.getHocVi() : "",
                    gv.getKhoa() != null ? gv.getKhoa() : ""
            });
        }
    }
}
