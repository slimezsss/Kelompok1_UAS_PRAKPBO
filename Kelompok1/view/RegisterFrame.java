package view;

import model.*;
import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    public RegisterFrame() {
        setTitle("Registrasi Akun");
        setSize(350, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel p = new JPanel(new GridLayout(4, 2, 10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField tfUser = new JTextField();
        JPasswordField tfPass = new JPasswordField();

        p.add(new JLabel("Username:"));
        p.add(tfUser);
        p.add(new JLabel("Password:"));
        p.add(tfPass);

        JButton btnSubmit = new JButton("Daftar");

        btnSubmit.addActionListener(e -> {
            String u = tfUser.getText().trim();
            String pw = new String(tfPass.getPassword());

            if(u.isEmpty() || pw.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tidak boleh kosong!");
                return;
            }

            DataStore.listAkun.add(new Customer(u, pw));
            JOptionPane.showMessageDialog(this, "Registrasi Berhasil!");
            new LoginFrame().setVisible(true);
            dispose();
        });

        p.add(btnSubmit);
        add(p);
    }
}
