package view;

import model.*;
import util.StokHabisException;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CustomerFrame extends JFrame {
    private HashMap<Barang, Integer> keranjang = new HashMap<>();

    // Palet Warna Modern
    private final Color CLR_PRIMARY = new Color(3, 172, 14); // Hijau Tokopedia
    private final Color CLR_BG = new Color(243, 244, 245);   
    private final Color CLR_WHITE = Color.WHITE;
    private final Color CLR_PRICE = new Color(250, 89, 29);  
    private final Color CLR_TEXT = new Color(49, 53, 59);

    // Komponen UI
    private JTable tblShop;
    private JTextField txtSearch;
    private JLabel lblCartSubtotal, lblCartDisc, lblCartTotal;
    private JCheckBox chkPoin; // Dijadikan global agar bisa diupdate teksnya
    private DefaultTableModel modelBarang, modelCart, modelHist;

    public CustomerFrame() {
        setTitle("Daily Mart Official Store - " + DataStore.currentUser.getUsername());
        setSize(1280, 850);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(CLR_BG);

        // Navbar
        add(createNavbar(), BorderLayout.NORTH);

        // Tabs
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.BOLD, 14));
        tabs.setBackground(CLR_WHITE);

        tabs.addTab("  Daftar Barang  ", createHomeTab());
        tabs.addTab("  Keranjang Belanja  ", createCartTab());
        tabs.addTab("  Riwayat Transaksi  ", createHistoryTab());

        add(tabs, BorderLayout.CENTER);

        // Init Data
        refreshKatalog("");
        
        // Listeners
        txtSearch.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { refreshKatalog(txtSearch.getText().trim()); }
        });
        
        tabs.addChangeListener(e -> {
            if(tabs.getSelectedIndex() == 0) refreshKatalog("");
            if(tabs.getSelectedIndex() == 1) refreshCartTable();
            if(tabs.getSelectedIndex() == 2) refreshHistoryTable();
        });
    }

    // =======================================================
    // 1. PANEL BUILDERS
    // =======================================================

    private JPanel createNavbar() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(CLR_WHITE);
        p.setBorder(new CompoundBorder(new LineBorder(new Color(220,220,220)), new EmptyBorder(15, 30, 15, 30)));

        JLabel lLogo = new JLabel("Daily Mart");
        lLogo.setFont(new Font("SansSerif", Font.BOLD, 26));
        lLogo.setForeground(CLR_PRIMARY);

        JPanel pSearch = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pSearch.setBackground(CLR_WHITE);
        txtSearch = new JTextField(40);
        txtSearch.setPreferredSize(new Dimension(500, 40));
        txtSearch.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtSearch.setBorder(BorderFactory.createTitledBorder("Cari barang..."));
        pSearch.add(txtSearch);

        JPanel pUser = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pUser.setBackground(CLR_WHITE);
        JLabel lUser = new JLabel("Halo, " + DataStore.currentUser.getUsername());
        lUser.setFont(new Font("SansSerif", Font.BOLD, 14));
        JButton btnOut = new JButton("Keluar");
        styleOutlinedButton(btnOut);
        btnOut.addActionListener(e -> { new LoginFrame().setVisible(true); dispose(); });
        
        pUser.add(lUser); pUser.add(Box.createHorizontalStrut(10)); pUser.add(btnOut);

        p.add(lLogo, BorderLayout.WEST);
        p.add(pSearch, BorderLayout.CENTER);
        p.add(pUser, BorderLayout.EAST);
        return p;
    }

    private JPanel createHomeTab() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(CLR_BG);
        p.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        String[] cols = {"ID", "Nama Barang", "Kategori", "Harga Normal", "Harga Promo", "Stok"};
        modelBarang = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblShop = new JTable(modelBarang);
        styleTable(tblShop);
        
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelBarang);
        tblShop.setRowSorter(sorter);
        
        tblShop.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) { 
                    int row = tblShop.getSelectedRow();
                    if (row != -1) {
                        int modelRow = tblShop.convertRowIndexToModel(row);
                        String id = (String) modelBarang.getValueAt(modelRow, 0);
                        Barang selected = null;
                        for(Barang b : DataStore.listBarang) {
                            if(b.getId().equals(id)) selected = b;
                        }
                        if(selected != null) showDetailPopup(selected);
                    }
                }
            }
        });

        JLabel lblInfo = new JLabel("Belanja Kebutuhan Harian? Di Daily Mart Ajaa!");
        lblInfo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblInfo.setBorder(new EmptyBorder(0, 0, 10, 0));

        p.add(lblInfo, BorderLayout.NORTH);
        p.add(new JScrollPane(tblShop), BorderLayout.CENTER);
        return p;
    }

    private JPanel createCartTab() {
        JPanel p = new JPanel(new BorderLayout(20, 20));
        p.setBackground(CLR_BG);
        p.setBorder(new EmptyBorder(20, 40, 20, 40));

        // Table
        String[] h = {"Produk", "Harga Satuan", "Qty", "Total"};
        modelCart = new DefaultTableModel(h, 0);
        JTable t = new JTable(modelCart);
        styleTable(t);
        
        // Summary Panel
        JPanel pSum = new JPanel();
        pSum.setLayout(new BoxLayout(pSum, BoxLayout.Y_AXIS));
        pSum.setBackground(CLR_WHITE);
        pSum.setBorder(new CompoundBorder(new LineBorder(new Color(220,220,220)), new EmptyBorder(20, 20, 20, 20)));
        pSum.setPreferredSize(new Dimension(350, 280));

        JLabel lTitle = new JLabel("Ringkasan Belanja");
        lTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        lTitle.setForeground(CLR_TEXT);
        lTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        lblCartSubtotal = new JLabel("Subtotal: Rp 0");
        lblCartSubtotal.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblCartSubtotal.setForeground(CLR_TEXT);
        lblCartSubtotal.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        lblCartDisc = new JLabel("Diskon Poin: -Rp 0");
        lblCartDisc.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblCartDisc.setForeground(CLR_PRIMARY); 
        lblCartDisc.setAlignmentX(Component.LEFT_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(350, 10));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        lblCartTotal = new JLabel("Total Tagihan: Rp 0");
        lblCartTotal.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblCartTotal.setForeground(CLR_PRICE);
        lblCartTotal.setAlignmentX(Component.LEFT_ALIGNMENT);

        chkPoin = new JCheckBox("Gunakan Poin Member");
        chkPoin.setBackground(CLR_WHITE);
        chkPoin.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Cek Role untuk Checkbox
        if(DataStore.currentUser instanceof Member) {
            Member m = (Member) DataStore.currentUser;
            chkPoin.setText("Gunakan Poin (Saldo: " + m.getPoin() + ")");
        } else {
            chkPoin.setText("Poin (Khusus Member)");
            chkPoin.setEnabled(false);
        }

        JButton btnPay = new JButton("Bayar Sekarang");
        styleButton(btnPay, CLR_PRIMARY, CLR_WHITE);
        btnPay.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnPay.setMaximumSize(new Dimension(350, 45));

        pSum.add(lTitle); pSum.add(Box.createVerticalStrut(15));
        pSum.add(lblCartSubtotal); pSum.add(Box.createVerticalStrut(8));
        pSum.add(chkPoin); pSum.add(Box.createVerticalStrut(5));
        pSum.add(lblCartDisc); pSum.add(Box.createVerticalStrut(10));
        pSum.add(sep); pSum.add(Box.createVerticalStrut(10));
        pSum.add(lblCartTotal); pSum.add(Box.createVerticalStrut(20));
        pSum.add(btnPay);

        JPanel pRight = new JPanel(new BorderLayout());
        pRight.setBackground(CLR_BG);
        pRight.add(pSum, BorderLayout.NORTH);

        p.add(new JScrollPane(t), BorderLayout.CENTER);
        p.add(pRight, BorderLayout.EAST);

        chkPoin.addActionListener(e -> updateCartSummary(chkPoin.isSelected()));
        btnPay.addActionListener(e -> processPayment(chkPoin.isSelected()));

        return p;
    }

    private JPanel createHistoryTab() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(CLR_BG);
        p.setBorder(new EmptyBorder(20, 40, 20, 40));

        String[] h = {"No. Transaksi", "Tanggal", "Metode Bayar", "Total Belanja", "Status"};
        modelHist = new DefaultTableModel(h, 0);
        JTable t = new JTable(modelHist);
        styleTable(t);
        
        p.add(new JScrollPane(t), BorderLayout.CENTER);
        return p;
    }

    // =======================================================
    // 2. LOGIC & DATA
    // =======================================================

    private void refreshKatalog(String keyword) {
        modelBarang.setRowCount(0);
        for(Barang b : DataStore.listBarang) {
            if (b.getNama().toLowerCase().contains(keyword.toLowerCase()) || 
                b.getKategori().toLowerCase().contains(keyword.toLowerCase())) {
                
                double promo = Promo.hitungHarga(b.getHarga());
                modelBarang.addRow(new Object[]{
                    b.getId(), b.getNama(), b.getKategori(), 
                    (int)b.getHarga(), (int)promo, b.getStok()
                });
            }
        }
    }

    private void showDetailPopup(Barang b) {
        JDialog d = new JDialog(this, "Detail: " + b.getNama(), true);
        d.setSize(500, 450);
        d.setLocationRelativeTo(this);
        d.setLayout(new BorderLayout());
        d.getContentPane().setBackground(CLR_WHITE);
        
        JPanel pContent = new JPanel();
        pContent.setLayout(new BoxLayout(pContent, BoxLayout.Y_AXIS));
        pContent.setBorder(new EmptyBorder(20, 30, 20, 30));
        pContent.setBackground(CLR_WHITE);
        
        JLabel lNama = new JLabel("<html>" + b.getNama() + "</html>");
        lNama.setFont(new Font("SansSerif", Font.BOLD, 22));
        lNama.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lKat = new JLabel("Kategori: " + b.getKategori());
        lKat.setFont(new Font("SansSerif", Font.BOLD, 14));
        lKat.setForeground(CLR_PRIMARY);
        lKat.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        double promo = Promo.hitungHarga(b.getHarga());
        JLabel lHarga = new JLabel("Harga: Rp " + (int)promo);
        lHarga.setFont(new Font("SansSerif", Font.BOLD, 20));
        lHarga.setForeground(CLR_PRICE);
        lHarga.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Panel Info (Polymorphism InfoLengkap)
        JPanel pSpec = new JPanel(new BorderLayout());
        pSpec.setBackground(new Color(240, 255, 240));
        pSpec.setBorder(BorderFactory.createTitledBorder(new LineBorder(CLR_PRIMARY), "Info"));
        pSpec.setAlignmentX(Component.LEFT_ALIGNMENT);
        pSpec.setMaximumSize(new Dimension(500, 150));
        
        String info = "<html>" + b.getDetailKhusus() + "<br><br><b>Stok Tersedia:</b> " + b.getStok() + "</html>";
        JLabel lInfo = new JLabel(info);
        lInfo.setBorder(new EmptyBorder(10, 10, 10, 10));
        lInfo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        pSpec.add(lInfo, BorderLayout.CENTER);
        
        JButton btnBeli = new JButton("Masukkan ke Keranjang");
        styleButton(btnBeli, CLR_PRIMARY, CLR_WHITE);
        btnBeli.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        btnBeli.addActionListener(ev -> {
            String in = JOptionPane.showInputDialog(d, "Beli berapa?");
            if(in != null) {
                try {
                    int q = Integer.parseInt(in);
                    if(q <= 0) throw new Exception("Minimal beli 1");
                    if(q > b.getStok()) throw new StokHabisException("Stok Kurang! Sisa: " + b.getStok());
                    
                    keranjang.put(b, keranjang.getOrDefault(b, 0) + q);
                    JOptionPane.showMessageDialog(d, "Berhasil ditambahkan!");
                    d.dispose();
                } catch(Exception ex) {
                    JOptionPane.showMessageDialog(d, "Error: " + ex.getMessage());
                }
            }
        });
        
        pContent.add(lNama);
        pContent.add(Box.createVerticalStrut(5));
        pContent.add(lKat);
        pContent.add(Box.createVerticalStrut(10));
        pContent.add(lHarga);
        pContent.add(Box.createVerticalStrut(15));
        pContent.add(pSpec);
        pContent.add(Box.createVerticalStrut(20));
        pContent.add(btnBeli);
        
        d.add(pContent, BorderLayout.CENTER);
        d.setVisible(true);
    }

    private void refreshCartTable() {
        modelCart.setRowCount(0);
        for(Map.Entry<Barang, Integer> entry : keranjang.entrySet()) {
            Barang b = entry.getKey();
            int qty = entry.getValue();
            double p = Promo.hitungHarga(b.getHarga());
            modelCart.addRow(new Object[]{b.getNama(), (int)p, qty, (int)(p*qty)});
        }
        // Update Text Saldo Poin
        if (DataStore.currentUser instanceof Member) {
            Member m = (Member) DataStore.currentUser;
            chkPoin.setText("Gunakan Poin (Saldo: " + m.getPoin() + ")");
        }
        updateCartSummary(chkPoin.isSelected());
    }

    private void updateCartSummary(boolean usePoin) {
        double sub = 0;
        for(Map.Entry<Barang, Integer> entry : keranjang.entrySet()) {
            sub += Promo.hitungHarga(entry.getKey().getHarga()) * entry.getValue();
        }
        
        int pot = 0;
        if(usePoin && DataStore.currentUser instanceof Member) {
            // Logika: Potongan maksimal tidak boleh melebihi harga belanja
            pot = Math.min((int)sub, ((Member)DataStore.currentUser).getPoin());
        }
        
        lblCartSubtotal.setText("Subtotal: Rp " + (int)sub);
        lblCartDisc.setText("Diskon Poin: -Rp " + pot);
        lblCartTotal.setText("Total Tagihan: Rp " + (int)(sub - pot));
    }

    private void processPayment(boolean usePoin) {
        if(keranjang.isEmpty()) { JOptionPane.showMessageDialog(this, "Keranjang Kosong!"); return; }

        // Hitung Ulang
        double sub = 0;
        for(Map.Entry<Barang, Integer> entry : keranjang.entrySet()) {
            sub += Promo.hitungHarga(entry.getKey().getHarga()) * entry.getValue();
        }
        
        int pot = 0;
        if(usePoin && DataStore.currentUser instanceof Member) {
            pot = Math.min((int)sub, ((Member)DataStore.currentUser).getPoin());
        }
        double finalTot = sub - pot;

        String[] ops = {"QRIS", "Transfer Bank", "COD"};
        int c = JOptionPane.showOptionDialog(this, "Total: Rp " + (int)finalTot, "Bayar", 0, 1, null, ops, ops[0]);
        
        if(c != -1) {
            String method = ops[c];
            
            // Pesan Umum (Permintaan: Hapus detail menunggu, langsung loading saja)
            // Tapi agar loading ada gunanya, kita tulis "Memproses Transaksi..."
            String msg = "Memproses Transaksi dengan " + method + "...";

            // Capture variables
            final double fSub = sub;
            final int fPot = pot;
            final double fTot = finalTot;
            final String fMet = method;

            JDialog d = new JDialog(this, "Processing", true);
            d.setSize(300, 120); d.setLocationRelativeTo(this);
            d.add(new JLabel(msg, SwingConstants.CENTER));
            
            Timer t = new Timer(2000, x -> {
                d.dispose();
                
                // 1. Logic Core (Kurangi Stok & Poin)
                keranjang.forEach((b, q) -> b.setStok(b.getStok() - q));
                
                if(DataStore.currentUser instanceof Member) {
                    Member m = (Member)DataStore.currentUser;
                    m.pakaiPoin(fPot);        // Kurangi poin yg dipakai
                    m.tambahPoin(fTot);       // Tambah poin dari transaksi ini
                }
                
                // 2. Simpan TRX
                String idTrx = "TRX-" + System.currentTimeMillis();
                DataStore.listTransaksi.add(new Transaksi(idTrx, DataStore.currentUser.getUsername(), fTot, fMet));
                
                // 3. POP-UP SUKSES DULU (Sesuai Request)
                JOptionPane.showMessageDialog(this, "Transaksi Berhasil!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                
                // 4. BARU MUNCUL STRUK
                showInvoice(idTrx, fMet, fSub, fPot, fTot);

                keranjang.clear();
                chkPoin.setSelected(false); // Reset Checkbox
                refreshKatalog("");
                refreshCartTable();
                refreshHistoryTable();
            });
            t.setRepeats(false); t.start(); d.setVisible(true);
        }
    }

    private void showInvoice(String id, String met, double sub, int disc, double tot) {
        JDialog d = new JDialog(this, "Struk Belanja", true);
        d.setSize(400, 600);
        d.setLocationRelativeTo(this);
        d.getContentPane().setBackground(CLR_WHITE);
        d.setLayout(new BorderLayout());

        JPanel pContent = new JPanel();
        pContent.setLayout(new BoxLayout(pContent, BoxLayout.Y_AXIS));
        pContent.setBackground(CLR_WHITE);
        pContent.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lHead = new JLabel("DAILY MART");
        lHead.setFont(new Font("SansSerif", Font.BOLD, 24));
        lHead.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lDate = new JLabel(new SimpleDateFormat("dd MMM yyyy HH:mm").format(new Date()));
        lDate.setAlignmentX(Component.CENTER_ALIGNMENT);

        pContent.add(lHead);
        pContent.add(lDate);
        pContent.add(Box.createVerticalStrut(10));
        pContent.add(new JSeparator());
        pContent.add(Box.createVerticalStrut(10));

        // List Barang
        for(Map.Entry<Barang, Integer> entry : keranjang.entrySet()) {
            JPanel pRow = new JPanel(new BorderLayout());
            pRow.setBackground(CLR_WHITE);
            pRow.add(new JLabel(entry.getKey().getNama() + " x" + entry.getValue()), BorderLayout.WEST);
            int pr = (int)(Promo.hitungHarga(entry.getKey().getHarga()) * entry.getValue());
            pRow.add(new JLabel("Rp " + pr), BorderLayout.EAST);
            pRow.setMaximumSize(new Dimension(350, 25));
            pContent.add(pRow);
        }

        pContent.add(Box.createVerticalStrut(10));
        pContent.add(new JSeparator());
        pContent.add(Box.createVerticalStrut(10));

        addSummaryRow(pContent, "Subtotal", sub);
        addSummaryRow(pContent, "Diskon Poin", -disc);
        pContent.add(Box.createVerticalStrut(10));
        
        JPanel pTot = new JPanel(new BorderLayout());
        pTot.setBackground(CLR_WHITE);
        JLabel lT = new JLabel("TOTAL BAYAR"); lT.setFont(new Font("SansSerif", Font.BOLD, 16));
        JLabel lV = new JLabel("Rp " + (int)tot); lV.setFont(new Font("SansSerif", Font.BOLD, 16));
        pTot.add(lT, BorderLayout.WEST); pTot.add(lV, BorderLayout.EAST);
        pTot.setMaximumSize(new Dimension(350, 30));
        pContent.add(pTot);

        pContent.add(Box.createVerticalStrut(20));
        JLabel lMet = new JLabel("Metode: " + met);
        lMet.setAlignmentX(Component.CENTER_ALIGNMENT);
        pContent.add(lMet);
        
        JLabel lThanks = new JLabel("Terima Kasih!");
        lThanks.setFont(new Font("SansSerif", Font.ITALIC, 14));
        lThanks.setAlignmentX(Component.CENTER_ALIGNMENT);
        pContent.add(Box.createVerticalStrut(20));
        pContent.add(lThanks);

        JButton btnOk = new JButton("Tutup Struk");
        btnOk.addActionListener(e -> d.dispose());
        
        d.add(pContent, BorderLayout.CENTER);
        d.add(btnOk, BorderLayout.SOUTH);
        d.setVisible(true);
    }

    private void addSummaryRow(JPanel p, String label, double val) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.add(new JLabel(label), BorderLayout.WEST);
        row.add(new JLabel("Rp " + (int)val), BorderLayout.EAST);
        row.setMaximumSize(new Dimension(350, 25));
        p.add(row);
    }

    private void refreshHistoryTable() {
        modelHist.setRowCount(0);
        for(Transaksi t : DataStore.listTransaksi) {
            if(t.getUser().equals(DataStore.currentUser.getUsername()))
                modelHist.addRow(new Object[]{t.getId(), t.getTanggalStr(), t.getMetode(), (int)t.getTotal(), t.getStatus()});
        }
    }

    // --- STYLING METHODS ---
    
    private void styleButton(JButton b, Color bg, Color fg) {
        b.setBackground(bg); b.setForeground(fg); b.setFont(new Font("SansSerif", Font.BOLD, 14));
        b.setFocusPainted(false);
    }
    private void styleOutlinedButton(JButton b) {
        b.setBackground(Color.WHITE); b.setForeground(CLR_PRIMARY);
        b.setBorder(new LineBorder(CLR_PRIMARY, 1)); b.setFocusPainted(false);
    }
    private void styleTable(JTable t) {
        t.setRowHeight(35);
        t.setShowVerticalLines(false);
        t.setGridColor(new Color(230,230,230));
        t.setFont(new Font("SansSerif", Font.PLAIN, 14));
        t.getTableHeader().setBackground(CLR_PRIMARY); 
        t.getTableHeader().setForeground(Color.WHITE);
        t.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        t.getTableHeader().setPreferredSize(new Dimension(100, 40));
        DefaultTableCellRenderer c = new DefaultTableCellRenderer(); c.setHorizontalAlignment(JLabel.CENTER);
        for(int i=0; i<t.getColumnCount(); i++) t.getColumnModel().getColumn(i).setCellRenderer(c);
    }
}
