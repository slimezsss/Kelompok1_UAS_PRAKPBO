package view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.*;

public class AdminFrame extends JFrame {
    private DefaultTableModel modelBarang, modelHistory;
    private JLabel lOmzet, lCount, lFlash;
    private JTable tblBarang, tblHistory;

    // Folder gambar barang
    private static final String FOLDER_GAMBAR = "src/image/barang/";
    private static final String DEFAULT_IMAGE = "src/image/default-barang.png"; // Buat gambar ini ya!

    public AdminFrame() {
        // Pastikan folder gambar ada
        new File(FOLDER_GAMBAR).mkdirs();

        setTitle("Admin Dashboard - Daily Mart Professional");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.BOLD, 14));

        // DASHBOARD
        JPanel pDash = new JPanel(new GridLayout(3, 1, 20, 20));
        pDash.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));
        pDash.setBackground(Color.WHITE);

        lOmzet = createStatCard("Total Pendapatan (Omzet)", new Color(34, 139, 34));
        lCount = createStatCard("Total Transaksi Berhasil", new Color(70, 130, 180));

        JLabel lblTitle = new JLabel("OVERVIEW TOKO", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitle.setForeground(Color.DARK_GRAY);

        pDash.add(lblTitle);
        pDash.add(lOmzet);
        pDash.add(lCount);

        // INVENTORY & CONTROL
        JPanel pInv = new JPanel(new BorderLayout());
        String[] cols = {"ID Barang", "Nama Produk", "Kategori", "Detail Info", "Harga Normal", "Harga Promo", "Stok"};
        modelBarang = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblBarang = new JTable(modelBarang);
        styleTable(tblBarang);

        JPanel pControl = new JPanel(new GridLayout(2, 1, 10, 10));
        pControl.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel pCrud = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton bAdd = createButton("Tambah Barang", new Color(0, 102, 204));
        JButton bEdit = createButton("Edit Stok/Harga", new Color(255, 153, 0));
        JButton bDel = createButton("Hapus Barang", new Color(204, 0, 0));
        pCrud.add(bAdd); pCrud.add(bEdit); pCrud.add(bDel);

        JPanel pFlash = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        lFlash = new JLabel("Flash Sale: MATI ");
        lFlash.setFont(new Font("SansSerif", Font.BOLD, 16));
        JButton bFlash = createButton("Atur Flash Sale", new Color(128, 0, 128));
        pFlash.add(lFlash); pFlash.add(bFlash);

        pControl.add(pCrud); pControl.add(pFlash);
        pInv.add(new JScrollPane(tblBarang), BorderLayout.CENTER);
        pInv.add(pControl, BorderLayout.SOUTH);

        // RIWAYAT TRANSAKSI
        JPanel pHist = new JPanel(new BorderLayout());
        String[] colHist = {"ID Transaksi", "User Pembeli", "Tanggal", "Metode Bayar", "Total Belanja", "Status"};
        modelHistory = new DefaultTableModel(colHist, 0);
        tblHistory = new JTable(modelHistory);
        styleTable(tblHistory);
        pHist.add(new JScrollPane(tblHistory), BorderLayout.CENTER);

        tabs.addTab("  Dashboard  ", pDash);
        tabs.addTab("  Manajemen Stok  ", pInv);
        tabs.addTab("  Riwayat Transaksi  ", pHist);
        tabs.addChangeListener(e -> refreshAll());

        // Header Admin
        JPanel head = new JPanel(new BorderLayout());
        head.setBackground(new Color(30, 30, 30));
        head.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblAdmin = new JLabel("ADMINISTRATOR PANEL");
        lblAdmin.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblAdmin.setForeground(Color.WHITE);

        JButton out = new JButton("Logout");
        out.setBackground(Color.RED);
        out.setForeground(Color.WHITE);

        head.add(lblAdmin, BorderLayout.WEST);
        head.add(out, BorderLayout.EAST);

        add(head, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);

        refreshAll();

        // ACTION LISTENERS

        bAdd.addActionListener(e -> tambahBarangDenganGambar());

        bEdit.addActionListener(e -> {
            int r = tblBarang.getSelectedRow();
            if (r < 0) {
                JOptionPane.showMessageDialog(this, "Pilih barang di tabel dulu!");
                return;
            }
            Barang b = DataStore.listBarang.get(r);
            editBarang(b, r);
        });

        bDel.addActionListener(e -> {
            int r = tblBarang.getSelectedRow();
            if (r >= 0) {
                if (JOptionPane.showConfirmDialog(this, "Yakin hapus barang ini?") == 0) {
                    DataStore.listBarang.remove(r);
                    refreshAll();
                }
            }
        });

        bFlash.addActionListener(e -> {
            if (!Promo.isActive) {
                String p = JOptionPane.showInputDialog("Masukkan Persentase Diskon (1-100):");
                if (p != null) {
                    try {
                        Promo.persenDiskon = Integer.parseInt(p);
                        Promo.isActive = true;
                    } catch (Exception ex) {}
                }
            } else {
                Promo.isActive = false;
            }
            refreshAll();
        });

        out.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
    }

    // FITUR TAMBAH BARANG + UPLOAD GAMBAR
    private void tambahBarangDenganGambar() {
        String[] types = {"Makanan", "Minuman", "Kesehatan", "Perawatan", "RumahTangga"};
        String t = (String) JOptionPane.showInputDialog(this, "Pilih Kategori:", "Tambah Barang",
                JOptionPane.QUESTION_MESSAGE, null, types, types[0]);
        if (t == null) return;

        JTextField fNama = new JTextField(20);
        JTextField fHarga = new JTextField(10);
        JTextField fStok = new JTextField(10);
        JLabel lblPreview = new JLabel(new ImageIcon(DEFAULT_IMAGE));
        lblPreview.setPreferredSize(new Dimension(150, 150));
        Border border = BorderFactory.createLineBorder(Color.GRAY, 2);
        lblPreview.setBorder(border);

        JButton btnPilih = new JButton("Pilih Gambar (Opsional)");
        final String[] selectedPath = {null};

        btnPilih.addActionListener(ev -> {
            JFileChooser fc = new JFileChooser();
            fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Gambar JPG/PNG", "jpg", "jpeg", "png"));
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                try {
                    // Copy ke folder barang dengan nama unik
                    String ext = file.getName().substring(file.getName().lastIndexOf("."));
                    String namaBaru = "barang_" + System.currentTimeMillis() + ext;
                    Path tujuan = Path.of(FOLDER_GAMBAR + namaBaru);
                    Files.copy(file.toPath(), tujuan, StandardCopyOption.REPLACE_EXISTING);

                    selectedPath[0] = tujuan.toString();

                    ImageIcon icon = new ImageIcon(tujuan.toString());
                    Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                    lblPreview.setIcon(new ImageIcon(img));

                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Gagal upload gambar!");
                }
            }
        });

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0; c.gridy = 0; panel.add(new JLabel("Nama Barang:"), c);
        c.gridx = 1; panel.add(fNama, c);
        c.gridx = 0; c.gridy = 1; panel.add(new JLabel("Harga (Rp):"), c);
        c.gridx = 1; panel.add(fHarga, c);
        c.gridx = 0; c.gridy = 2; panel.add(new JLabel("Stok Awal:"), c);
        c.gridx = 1; panel.add(fStok, c);
        c.gridx = 0; c.gridy = 3; c.gridwidth = 2; panel.add(btnPilih, c);
        c.gridy = 4; panel.add(lblPreview, c);

        int result = JOptionPane.showConfirmDialog(this, panel, "Tambah Barang Baru",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String nama = fNama.getText().trim();
                double harga = Double.parseDouble(fHarga.getText());
                int stok = Integer.parseInt(fStok.getText());
                if (nama.isEmpty()) throw new Exception();

                String id = "B" + System.currentTimeMillis();
                Barang baru = null;

                switch (t) {
                    case "Makanan" -> baru = new Makanan(id, nama, harga, stok,
                            JOptionPane.showInputDialog("Tgl Kadaluarsa (Exp):"));
                    case "Minuman" -> baru = new Minuman(id, nama, harga, stok,
                            JOptionPane.showInputDialog("Ukuran (ml/liter):"));
                    case "Kesehatan" -> baru = new Kesehatan(id, nama, harga, stok,
                            JOptionPane.showConfirmDialog(this, "Butuh Resep Dokter?") == 0);
                    case "Perawatan" -> baru = new Perawatan(id, nama, harga, stok,
                            JOptionPane.showInputDialog("Merk Brand:"));
                    case "RumahTangga" -> baru = new RumahTangga(id, nama, harga, stok,
                            JOptionPane.showInputDialog("Fungsi Utama:"));
                }

                // SET GAMBAR (opsional)
                if (baru instanceof BarangWithImage withImage) {
                    withImage.setImagePath(selectedPath[0]);
                }

                DataStore.listBarang.add(baru);
                refreshAll();
                JOptionPane.showMessageDialog(this, "Barang berhasil ditambahkan dengan gambar!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Input salah! Pastikan semua field terisi dengan benar.");
            }
        }
    }

    // EDIT BARANG (termasuk ganti gambar)
    private void editBarang(Barang b, int index) {
        // Sama seperti tambah, tapi isi field dulu
        JTextField fNama = new JTextField(b.getNama(), 20);
        JTextField fHarga = new JTextField(String.valueOf((int)b.getHarga()), 10);
        JTextField fStok = new JTextField(String.valueOf(b.getStok()), 10);

        String currentImage = (b instanceof BarangWithImage withImage) ? withImage.getImagePath() : null;
        JLabel lblPreview = new JLabel();
        lblPreview.setPreferredSize(new Dimension(150, 150));
        lblPreview.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        updatePreview(lblPreview, currentImage);

        JButton btnPilih = new JButton("Ganti Gambar (Opsional)");
        final String[] selectedPath = {currentImage};

        btnPilih.addActionListener(ev -> {
            JFileChooser fc = new JFileChooser();
            fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Gambar JPG/PNG", "jpg", "jpeg", "png"));
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                try {
                    String ext = file.getName().substring(file.getName().lastIndexOf("."));
                    String namaBaru = "barang_" + System.currentTimeMillis() + ext;
                    Path tujuan = Path.of(FOLDER_GAMBAR + namaBaru);
                    Files.copy(file.toPath(), tujuan, StandardCopyOption.REPLACE_EXISTING);
                    selectedPath[0] = tujuan.toString();
                    updatePreview(lblPreview, selectedPath[0]);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Gagal ganti gambar!");
                }
            }
        });

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0; c.gridy = 0; panel.add(new JLabel("Nama:"), c);
        c.gridx = 1; panel.add(fNama, c);
        c.gridx = 0; c.gridy = 1; panel.add(new JLabel("Harga:"), c);
        c.gridx = 1; panel.add(fHarga, c);
        c.gridx = 0; c.gridy = 2; panel.add(new JLabel("Stok:"), c);
        c.gridx = 1; panel.add(fStok, c);
        c.gridx = 0; c.gridy = 3; c.gridwidth = 2; panel.add(btnPilih, c);
        c.gridy = 4; panel.add(lblPreview, c);

        int res = JOptionPane.showConfirmDialog(this, panel, "Edit Barang",
                JOptionPane.OK_CANCEL_OPTION);

        if (res == JOptionPane.OK_OPTION) {
            try {
                b.setNama(fNama.getText());
                b.setHarga(Double.parseDouble(fHarga.getText()));
                b.setStok(Integer.parseInt(fStok.getText()));

                if (b instanceof BarangWithImage withImage) {
                    withImage.setImagePath(selectedPath[0]);
                }

                refreshAll();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Input tidak valid!");
            }
        }
    }

    private void updatePreview(JLabel label, String path) {
        ImageIcon icon;
        if (path == null || path.isEmpty()) {
            icon = new ImageIcon(DEFAULT_IMAGE);
        } else {
            icon = new ImageIcon(path);
        }
        Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(img));
    }

    // ... helper method yang lama tetap sama (styleTable, createStatCard, createButton, refreshAll)
    // Cuma tambah satu baris di refreshAll kalau mau tampilkan gambar di tabel nanti

    private void styleTable(JTable table) {
        table.setRowHeight(35);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));

        table.getTableHeader().setBackground(new Color(34, 139, 34));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.getTableHeader().setPreferredSize(new Dimension(100, 40));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private JLabel createStatCard(String title, Color bgColor) {
        JLabel lbl = new JLabel("Rp 0", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 36));
        lbl.setForeground(bgColor);
        lbl.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(bgColor, 2), title,
                0, 0, new Font("SansSerif", Font.BOLD, 16), Color.DARK_GRAY));
        return lbl;
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setFocusPainted(false);
        return btn;
    }

    void refreshAll() {
        modelBarang.setRowCount(0);
        for (Barang b : DataStore.listBarang) {
            double d = Promo.hitungHarga(b.getHarga());
            modelBarang.addRow(new Object[]{
                b.getId(), b.getNama(), b.getKategori(), b.getDetailKhusus(),
                "Rp " + (int)b.getHarga(), "Rp " + (int)d, b.getStok()
            });
        }

        modelHistory.setRowCount(0);
        for (Transaksi t : DataStore.listTransaksi) {
            modelHistory.addRow(new Object[]{
                t.getId(), t.getUser(), t.getTanggalStr(), t.getMetode(),
                "Rp " + (int)t.getTotal(), t.getStatus()
            });
        }

        if (Promo.isActive) {
            lFlash.setText("Flash Sale: AKTIF (" + Promo.persenDiskon + "%)");
            lFlash.setForeground(new Color(220, 20, 60));
        } else {
            lFlash.setText("Flash Sale: NON-AKTIF");
            lFlash.setForeground(Color.DARK_GRAY);
        }

        double tot = 0;
        for (Transaksi t : DataStore.listTransaksi) tot += t.getTotal();
        lOmzet.setText("Rp " + (int)tot);
        lCount.setText(DataStore.listTransaksi.size() + " Transaksi");
    }
}