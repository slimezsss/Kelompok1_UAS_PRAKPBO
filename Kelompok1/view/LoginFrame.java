package view;
import model.*;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    public LoginFrame() {
        setTitle("Login Daily Mart");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setBackground(Color.WHITE);
        
        JLabel lbl = new JLabel("LOGIN", SwingConstants.CENTER);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 20));
        lbl.setForeground(new Color(3, 172, 14));
        
        JTextField txtUser = new JTextField();
        txtUser.setBorder(BorderFactory.createTitledBorder("Username"));
        
        JPasswordField txtPass = new JPasswordField();
        txtPass.setBorder(BorderFactory.createTitledBorder("Password"));
        
        JButton btnLogin = new JButton("MASUK");
        btnLogin.setBackground(new Color(3, 172, 14));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnLogin.setFocusPainted(false);
        
        // Tombol Menuju Register
        JButton btnReg = new JButton("Belum punya akun? Daftar");
        btnReg.setContentAreaFilled(false);
        btnReg.setBorderPainted(false);
        btnReg.setForeground(Color.BLUE);
        btnReg.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        panel.add(lbl); panel.add(txtUser); panel.add(txtPass); panel.add(btnLogin); panel.add(btnReg);
        add(panel);

        // Logic Login
        btnLogin.addActionListener(e -> {
            String u = txtUser.getText();
            String p = new String(txtPass.getPassword());
            boolean found = false;
            
            for(Akun a : DataStore.listAkun) {
                if(a.login(u, p)) {
                    DataStore.currentUser = a;
                    found = true;
                    dispose();
                    if(a instanceof Admin) new AdminFrame().setVisible(true);
                    else new CustomerFrame().setVisible(true);
                    break;
                }
            }
            if(!found) JOptionPane.showMessageDialog(this, "Gagal Login! Akun tidak ditemukan.");
        });

        // Logic Pindah ke Register
        btnReg.addActionListener(e -> {
            new RegisterFrame().setVisible(true);
            dispose();
        });
    }
}
