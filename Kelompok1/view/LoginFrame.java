package view;
import java.awt.*;
import javax.swing.*;
import model.*;

public class LoginFrame extends JFrame {
    public LoginFrame() {
        setTitle("Login System");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel lbl = new JLabel("DAILY MART LOGIN", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 18));
        
        JTextField txtUser = new JTextField();
        txtUser.setBorder(BorderFactory.createTitledBorder("Username"));
        JPasswordField txtPass = new JPasswordField();
        txtPass.setBorder(BorderFactory.createTitledBorder("Password"));
        
        JButton btnLogin = new JButton("MASUK");
        btnLogin.setBackground(new Color(34, 139, 34));
        btnLogin.setForeground(Color.WHITE);

        JButton btnRegister = new JButton("Register");
        btnRegister.addActionListener(e -> {
            new RegisterFrame().setVisible(true);
            dispose();
        });
        panel.add(btnRegister);
        
        panel.add(lbl); panel.add(txtUser); panel.add(txtPass); panel.add(btnLogin);
        add(panel);

        btnLogin.addActionListener(e -> {
            String u = txtUser.getText();
            String p = new String(txtPass.getPassword());
            boolean ok = false;
            
            for(Akun a : DataStore.listAkun) {
                if(a.login(u, p)) {
                    DataStore.currentUser = a;
                    ok = true;
                    dispose();
                    if(a instanceof Admin) new AdminFrame().setVisible(true);
                    else new CustomerFrame().setVisible(true);
                    break;
                }
            }
            if(!ok) JOptionPane.showMessageDialog(this, "Gagal Login!");
        });
    }
}