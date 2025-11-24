package view;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.util.HashMap;
import model.*;
import util.StokHabisException;

public class CustomerFrame extends JFrame {
    private DefaultTableModel modelBarang, modelCart, modelHist;
    private HashMap<Barang, Integer> keranjang = new HashMap<Barang, Integer>();    
    private JLabel lblSubtotal, lblDiskon, lblGrandTotal;
    private JCheckBox chkPoin;
    private JTable tblShop;
    private JTextField txtSearch;

    public CustomerFrame() {
        setTitle("Daily Mart Store - " + DataStore.currentUser.getUsername());
        setSize(1100, 750); 
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.BOLD, 14));

        // TAB 1: KATALOG
        JPanel pShop = new JPanel(new BorderLayout(10, 10));
        pShop.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel pSearch = new JPanel(new BorderLayout(10, 10));
        txtSearch = new JTextField();
        txtSearch.putClientProperty("JTextField.placeholderText", "Cari barang di sini...");
        txtSearch.setBorder(BorderFactory.createTitledBorder("Cari Nama Barang / Kategori"));
        pSearch.add(txtSearch, BorderLayout.CENTER);

        // TAMBAH KOLOM GAMBAR DI DEPAN!
        String[] colShop = {"", "ID", "Nama Produk", "Kategori", "Spesifikasi", "Harga Normal", "Harga Promo", "Sisa Stok"};
        modelBarang = new DefaultTableModel(colShop, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblShop = new JTable(modelBarang);
        tblShop.setRowHeight(70); // Biar gambar keliatan
        tblShop.getColumnModel().getColumn(0).setPreferredWidth(80);
        tblShop.getColumnModel().getColumn(0).setMaxWidth(80);
        tblShop.getColumnModel().getColumn(0).setCellRenderer(new ImageRenderer()); // GAMBAR MUNCUL!

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelBarang);
        tblShop.setRowSorter(sorter);
        txtSearch.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String text = txtSearch.getText();
                if (text.trim().isEmpty()) sorter.setRowFilter(null);
                else sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }
        });

        JButton btnAdd = createButton("TAMBAH KE KERANJANG (+)", new Color(34, 139, 34));
        
        pShop.add(pSearch, BorderLayout.NORTH);
        pShop.add(new JScrollPane(tblShop), BorderLayout.CENTER);
        pShop.add(btnAdd, BorderLayout.SOUTH);

        // TAB 2: KERANJANG
        JPanel pCart = new JPanel(new BorderLayout(20, 20));
        pCart.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        String[] colCart = {"", "Nama Barang", "Qty", "Harga Satuan", "Total"};
        modelCart = new DefaultTableModel(colCart, 0) { 
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tblCart = new JTable(modelCart);
        tblCart.setRowHeight(70);
        tblCart.getColumnModel().getColumn(0).setPreferredWidth(80);
        tblCart.getColumnModel().getColumn(0).setMaxWidth(80);
        tblCart.getColumnModel().getColumn(0).setCellRenderer(new ImageRenderer());

        JPanel pSummary = new JPanel(new GridLayout(6, 1, 5, 5));
        pSummary.setPreferredSize(new Dimension(300, 0));
        pSummary.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Ringkasan Belanja"));
        pSummary.setBackground(Color.WHITE);

        lblSubtotal = new JLabel("Subtotal: Rp 0");
        lblDiskon = new JLabel("Diskon Poin: -Rp 0");
        lblDiskon.setForeground(new Color(220, 20, 60));
        lblGrandTotal = new JLabel("TOTAL BAYAR: Rp 0");
        lblGrandTotal.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblGrandTotal.setForeground(new Color(34, 139, 34));
        
        chkPoin = new JCheckBox("Gunakan Poin");
        if(DataStore.currentUser instanceof Member m) {
            chkPoin.setText("<html>Gunakan Poin<br>(Saldo: " + m.getPoin() + " Poin)</html>");
        } else {
            chkPoin.setEnabled(false);
        }

        JButton btnPay = createButton("BAYAR SEKARANG", new Color(0, 102, 204));

        pSummary.add(lblSubtotal);
        pSummary.add(chkPoin);
        pSummary.add(new JSeparator());
        pSummary.add(lblDiskon);
        pSummary.add(lblGrandTotal);
        pSummary.add(btnPay);

        pCart.add(new JScrollPane(tblCart), BorderLayout.CENTER);
        pCart.add(pSummary, BorderLayout.EAST);

        // TAB 3: RIWAYAT
        JPanel pHist = new JPanel(new BorderLayout());
        String[] colHist = {"ID Trx", "Tanggal", "Metode", "Total Bayar", "Status"};
        modelHist = new DefaultTableModel(colHist, 0);
        JTable tblHist = new JTable(modelHist);
        styleTable(tblHist);
        pHist.add(new JScrollPane(tblHist), BorderLayout.CENTER);

        tabs.addTab("  Katalog  ", pShop);
        tabs.addTab("  Keranjang  ", pCart);
        tabs.addTab("  Riwayat  ", pHist);

        // Header
        JPanel head = new JPanel(new BorderLayout());
        head.setBackground(new Color(66, 181, 73));
        head.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel lblUser = new JLabel("Halo, " + DataStore.currentUser.getUsername());
        lblUser.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblUser.setForeground(Color.WHITE);
        JButton btnOut = new JButton("Logout");
        btnOut.setBackground(Color.RED);
        btnOut.setForeground(Color.WHITE);

        head.add(lblUser, BorderLayout.WEST);
        head.add(btnOut, BorderLayout.EAST);

        add(head, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);

        refreshAll();

        chkPoin.addActionListener(e -> updateSummaryUI());

        btnAdd.addActionListener(e -> {
            int row = tblShop.getSelectedRow();
            if(row < 0) { JOptionPane.showMessageDialog(this, "Pilih barang dulu!"); return; }
            
            String id = (String) tblShop.getValueAt(row, 1); // kolom ID sekarang index 1
            Barang selected = DataStore.listBarang.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst().orElse(null);
            
            String in = JOptionPane.showInputDialog("Beli berapa " + selected.getNama() + "?");
            if(in != null && !in.trim().isEmpty()) {
                try {
                    int qty = Integer.parseInt(in);
                    if(qty <= 0) throw new Exception("Jumlah harus lebih dari 0!");
                    if(qty > selected.getStok()) throw new StokHabisException("Stok tidak cukup!");
                    keranjang.put(selected, keranjang.getOrDefault(selected, 0) + qty);
                    refreshAll();
                } catch(Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                }
            }
        });

        btnPay.addActionListener(e -> {
            if (keranjang.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Keranjang kosong!");
                return;
            }

            double subtotal = hitungSubtotal();
            int poinUsed = 0;
            Member member = DataStore.currentUser instanceof Member ? (Member) DataStore.currentUser : null;

            // PAKAI getPoin() — JANGAN PERNAH PAKAI member.poin lagi!
            if (member != null && chkPoin.isSelected()) {
                poinUsed = Math.min((int)subtotal, member.getPoin());
            }
            double grandTotal = subtotal - poinUsed;

            String[] opts = {"QRIS", "Transfer Bank", "COD (Bayar di Tempat)", "Batal"};
            int pilihan = JOptionPane.showOptionDialog(this,
                "<html><h3>Pembayaran</h3>Total: <b>Rp " + String.format("%,.0f", grandTotal) + "</b></html>",
                "Pilih Metode", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, opts, opts[0]);

            if (pilihan < 0 || pilihan == 3) return;

            // Kurangi stok
            keranjang.forEach((b, q) -> b.setStok(b.getStok() - q));

            // UPDATE POIN PAKAI setPoin() — INI YANG BENAR!
            if (member != null && chkPoin.isSelected()) {
                int poinSekarang = member.getPoin();
                int poinBaru = poinSekarang - poinUsed + (int)(subtotal / 10000);
                member.setPoin(poinBaru);  // PAKAI setPoin() — BUKAN member.poin = 
            }

            // Simpan transaksi
            DataStore.listTransaksi.add(new Transaksi(
                "TRX" + System.currentTimeMillis(),
                DataStore.currentUser.getUsername(),
                grandTotal,
                opts[pilihan]
            ));

            JOptionPane.showMessageDialog(this, 
                "Pembayaran Berhasil!\nTerima kasih telah berbelanja!", 
                "Sukses", JOptionPane.INFORMATION_MESSAGE);

            keranjang.clear();
            chkPoin.setSelected(false);
            refreshAll();
        });

        btnOut.addActionListener(e -> { new LoginFrame().setVisible(true); dispose(); });
        tabs.addChangeListener(e -> refreshAll());
    }

    private double hitungSubtotal() {
        return keranjang.entrySet().stream()
            .mapToDouble(e -> Promo.hitungHarga(e.getKey().getHarga()) * e.getValue())
            .sum();
    }

    private void updateSummaryUI() {
        double subtotal = hitungSubtotal();
        int poin = 0;
        
        if (DataStore.currentUser instanceof Member m && chkPoin.isSelected()) {
            poin = Math.min((int)subtotal, m.getPoin()); // PAKAI getPoin()
            chkPoin.setText("<html>Gunakan Poin<br>(Saldo: " + m.getPoin() + " Poin)</html>");
        }

        double total = subtotal - poin;
        lblSubtotal.setText("Subtotal: Rp " + String.format("%,.0f", subtotal));
        lblDiskon.setText("Diskon Poin: -Rp " + String.format("%,.0f", (double)poin));
        lblGrandTotal.setText("<html>TOTAL BAYAR:<br>Rp " + String.format("%,.0f", total) + "</html>");
    }

    // GAMBAR MUNCUL CANTIK!
    class ImageRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            l.setText("");
            l.setHorizontalAlignment(JLabel.CENTER);
            if (value instanceof Barang b) {
                l.setIcon(b.getImageIcon(60, 60));
            } else {
                l.setIcon(null);
            }
            return l;
        }
    }

    private void refreshAll() {
        modelBarang.setRowCount(0);
        for(Barang b : DataStore.listBarang) {
            double p = Promo.hitungHarga(b.getHarga());
            modelBarang.addRow(new Object[]{
                b, // gambar
                b.getId(), b.getNama(), b.getKategori(), b.getDetailKhusus(),
                String.format("%,.0f", b.getHarga()), String.format("%,.0f", p), b.getStok()
            });
        }
        
        modelCart.setRowCount(0);
        for(Barang b : keranjang.keySet()) {
            double p = Promo.hitungHarga(b.getHarga());
            int qty = keranjang.get(b);
            modelCart.addRow(new Object[]{
                b, b.getNama(), qty, String.format("%,.0f", p), String.format("%,.0f", p * qty)
            });
        }
        
        updateSummaryUI();

        modelHist.setRowCount(0);
        for(Transaksi t : DataStore.listTransaksi) {
            if(t.getUser().equals(DataStore.currentUser.getUsername()))
                modelHist.addRow(new Object[]{t.getId(), t.getTanggalStr(), t.getMetode(), 
                    String.format("%,.0f", t.getTotal()), t.getStatus()});
        }
    }

    private void styleTable(JTable table) {
        table.setRowHeight(70);
        table.getTableHeader().setBackground(new Color(34, 139, 34));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        DefaultTableCellRenderer c = new DefaultTableCellRenderer();
        c.setHorizontalAlignment(JLabel.CENTER);
        for(int i = 1; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(c);
        }
    }
    
    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setFocusPainted(false);
        return btn;
    }
}