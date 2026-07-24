package com.example.workreport.ui;

import com.example.workreport.ui.view.MainFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
public class SwingAppRunner implements CommandLineRunner {

    @Autowired
    private MainFrame mainFrame;

    @Override
    public void run(String... args) throws Exception {
        // Khởi chạy GUI trên Event Dispatch Thread (EDT) của Java Swing
        SwingUtilities.invokeLater(() -> {
            try {
                // Đặt LookAndFeel hiện đại cho ứng dụng Swing (Nimbus)
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("Sử dụng LookAndFeel mặc định của hệ thống.");
            }
            mainFrame.setVisible(true);
        });
    }
}
