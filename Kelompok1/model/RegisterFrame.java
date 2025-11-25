package view;

import model.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RegisterFrame extends JFrame {
    
    public RegisterFrame() {
        setTitle("Registrasi Akun Baru");
        setSize(400, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // Header
        JLabel lblTitle = new JLabel("Buat Akun Baru", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitle.setForeground(new Color(3, 172, 14)); // Hijau
        lblTitle.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Form Input
        JPanel pForm = new JPanel(new GridLayout(5, 1, 10, 10));
        pForm.setBackground(Color.WHITE);
        pForm.setBorder(new EmptyBorder(10, 40, 10, 40));

        JTextField txtUser = new JTextField();
        txtUser.setBorder(BorderFactory.createTitledBorder("Username"));
        
        JPasswordField txtPass = new JPasswordField();
        txtPass.setBorder(BorderFactory.createTitledBorder("Password"));
        
        // Pilihan Role
        String[] roles = {"User Biasa", "Member"};
        JComboBox<String> comboRole = new JComboBox<>(roles);
        comboRole.setBorder(BorderFactory.createTitledBorder("Tipe Akun"));
        comboRole.setBackground(Color.WHITE);

        JButton btnDaftar = new JButton("Daftar Sekarang");
        btnDaftar.setBackground(new Color(3, 172, 14));
        btnDaftar.setForeground(Color.WHITE);
        btnDaftar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnDaftar.setFocusPainted(false);

        JButton btnLogin = new JButton("Sudah punya akun? Login");
        btnLogin.setContentAreaFilled(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setForeground(Color.BLUE);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        pForm.add(txtUser);
        pForm.add(txtPass);
        pForm.add(comboRole);
        pForm.add(btnDaftar);
        pForm.add(btnLogin);

        add(pForm, BorderLayout.CENTER);

        // --- LOGIC ---
        
        // Tombol Daftar
        btnDaftar.addActionListener(e -> {
            String u = txtUser.getText().trim();
            String p = new String(txtPass.getPassword()).trim();
            
            if(u.isEmpty() || p.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username dan Password wajib diisi!");
                return;
            }

            // Cek Duplikasi Username
            for(Akun a : DataStore.listAkun) {
                if(a.getUsername().equalsIgnoreCase(u)) {
                    JOptionPane.showMessageDialog(this, "Username sudah terpakai! Pilih yang lain.");
                    return;
                }
            }

            // Simpan Akun Baru
            if(comboRole.getSelectedIndex() == 0) {
                DataStore.listAkun.add(new Customer(u, p));
            } else {
                DataStore.listAkun.add(new Member(u, p));
            }

            JOptionPane.showMessageDialog(this, "Registrasi Berhasil! Silakan Login.");
            new LoginFrame().setVisible(true);
            dispose();
        });

        // Tombol Kembali ke Login
        btnLogin.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
    }
}