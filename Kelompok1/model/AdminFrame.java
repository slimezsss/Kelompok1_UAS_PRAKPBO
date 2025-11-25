package view;
import model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminFrame extends JFrame {
    private DefaultTableModel modelBarang, modelHistory;
    private JLabel lOmzet, lCount, lFlash;
    private JTable tblBarang, tblHistory;

    public AdminFrame() {
        setTitle("Admin Dashboard");
        setSize(1000, 700); 
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Styling TabbedPane
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.BOLD, 14));

        // DASHBOARD
        JPanel pDash = new JPanel(new GridLayout(3, 1, 20, 20));
        pDash.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));
        pDash.setBackground(Color.WHITE);
        
        lOmzet = createStatCard("Total Pendapatan (Omzet)", new Color(34, 139, 34)); 
        lCount = createStatCard("Total Transaksi Berhasil", new Color(70, 130, 180));
        
        JLabel lblTitle = new JLabel("Ringkasan Penjualan", SwingConstants.CENTER);
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

        // Control Panel (Bawah)
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

        // RIWAYAT TRANSAKSI (FITUR BARU)
        JPanel pHist = new JPanel(new BorderLayout());
        String[] colHist = {"ID Transaksi", "User Pembeli", "Tanggal", "Metode Bayar", "Total Belanja", "Status"};
        modelHistory = new DefaultTableModel(colHist, 0);
        tblHistory = new JTable(modelHistory);
        styleTable(tblHistory); // Styling tabel history
        pHist.add(new JScrollPane(tblHistory), BorderLayout.CENTER);

        // Menambahkan Tab
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

        // --- ACTION LISTENERS ---
        
        // 1. Tambah Barang
        bAdd.addActionListener(e -> {
            String[] types = {"Makanan", "Minuman", "Kesehatan", "Perawatan", "RumahTangga"};
            String t = (String) JOptionPane.showInputDialog(this, "Pilih Kategori:", "Tambah Barang", 1, null, types, types[0]);
            if(t == null) return;
            
            String n = JOptionPane.showInputDialog("Nama Barang:");
            if(n == null || n.isEmpty()) return;
            
            try {
                double h = Double.parseDouble(JOptionPane.showInputDialog("Harga Satuan (Rp):"));
                int s = Integer.parseInt(JOptionPane.showInputDialog("Jumlah Stok Awal:"));
                String id = "B" + System.currentTimeMillis();
                
                Barang baru = null;
                if(t.equals("Makanan")) baru = new Makanan(id, n, h, s, JOptionPane.showInputDialog("Tgl Kadaluarsa (Exp):"));
                else if(t.equals("Minuman")) baru = new Minuman(id, n, h, s, JOptionPane.showInputDialog("Ukuran (ml/liter):"));
                else if(t.equals("Kesehatan")) baru = new Kesehatan(id, n, h, s, JOptionPane.showConfirmDialog(this, "Butuh Resep Dokter?") == 0);
                else if(t.equals("Perawatan")) baru = new Perawatan(id, n, h, s, JOptionPane.showInputDialog("Merk Brand:"));
                else baru = new RumahTangga(id, n, h, s, JOptionPane.showInputDialog("Fungsi Utama:"));
                
                DataStore.listBarang.add(baru);
                refreshAll();
                JOptionPane.showMessageDialog(this, "Barang berhasil ditambahkan!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Input salah! Pastikan harga/stok berupa angka.");
            }
        });

        // 2. Edit Barang
        bEdit.addActionListener(e -> {
            int r = tblBarang.getSelectedRow();
            if(r < 0) { JOptionPane.showMessageDialog(this, "Pilih barang di tabel dulu!"); return; }
            Barang b = DataStore.listBarang.get(r);
            
            String nn = JOptionPane.showInputDialog("Edit Nama:", b.getNama());
            if(nn != null) b.setNama(nn);
            String hh = JOptionPane.showInputDialog("Edit Harga:", b.getHarga());
            if(hh != null) b.setHarga(Double.parseDouble(hh));
            String ss = JOptionPane.showInputDialog("Edit Stok:", b.getStok());
            if(ss != null) b.setStok(Integer.parseInt(ss));
            
            refreshAll();
        });

        // 3. Hapus Barang
        bDel.addActionListener(e -> {
            int r = tblBarang.getSelectedRow();
            if(r >= 0) { 
                if(JOptionPane.showConfirmDialog(this, "Yakin hapus barang ini?") == 0) {
                    DataStore.listBarang.remove(r); 
                    refreshAll(); 
                }
            }
        });

        // 4. Atur Flash Sale
        bFlash.addActionListener(e -> {
            if(!Promo.isActive) {
                String p = JOptionPane.showInputDialog("Masukkan Persentase Diskon (1-100):");
                if(p != null) {
                    try {
                        Promo.persenDiskon = Integer.parseInt(p);
                        Promo.isActive = true;
                    } catch(Exception ex) {}
                }
            } else {
                Promo.isActive = false;
            }
            refreshAll();
        });

        out.addActionListener(e -> { new LoginFrame().setVisible(true); dispose(); });
    }

    // HELPER METHODS

    private void styleTable(JTable table) {
        table.setRowHeight(35); 
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        // Header Styling
        table.getTableHeader().setBackground(new Color(34, 139, 34)); 
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.getTableHeader().setPreferredSize(new Dimension(100, 40));
        
        // Center Alignment Cell
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
        // Refresh Tabel Barang
        modelBarang.setRowCount(0);
        for(Barang b : DataStore.listBarang) {
            double d = Promo.hitungHarga(b.getHarga());
            modelBarang.addRow(new Object[]{
                b.getId(), b.getNama(), b.getKategori(), b.getDetailKhusus(),
                "Rp " + (int)b.getHarga(), "Rp " + (int)d, b.getStok()
            });
        }
        
        // Refresh Tabel History (SEMUA TRANSAKSI)
        modelHistory.setRowCount(0);
        for(Transaksi t : DataStore.listTransaksi) {
            modelHistory.addRow(new Object[]{
                t.getId(), t.getUser(), t.getTanggalStr(), t.getMetode(), "Rp " + (int)t.getTotal(), t.getStatus()
            });
        }
        
        // Refresh Status Dashboard
        if(Promo.isActive) {
            lFlash.setText("Flash Sale: AKTIF (" + Promo.persenDiskon + "%)");
            lFlash.setForeground(new Color(220, 20, 60));
        } else {
            lFlash.setText("Flash Sale: NON-AKTIF");
            lFlash.setForeground(Color.DARK_GRAY);
        }
        
        double tot = 0;
        for(Transaksi t : DataStore.listTransaksi) tot += t.getTotal();
        lOmzet.setText("Rp " + (int)tot);
        lCount.setText(DataStore.listTransaksi.size() + " Transaksi");
    }
}