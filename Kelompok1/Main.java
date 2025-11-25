package main;
import model.*;
import view.LoginFrame;

public class Main {
    public static void main(String[] args) {
        // SEEDING DATA DUMMY
        
        // User
        DataStore.listAkun.add(new Admin("admin_hazai", "admin_hazai"));
        DataStore.listAkun.add(new Admin("admin_rijal", "admin_rijal"));
        DataStore.listAkun.add(new Admin("admin_abdi", "admin_abdi"));
        DataStore.listAkun.add(new Member("member_rijal", "member_rijal"));
        DataStore.listAkun.add(new Member("member_hazai", "member_hazai"));
        DataStore.listAkun.add(new Member("member_abdi", "member_abdi"));
        DataStore.listAkun.add(new Customer("user_rijal", "user_rijal"));
        DataStore.listAkun.add(new Customer("user_hazai", "user_hazai"));
        DataStore.listAkun.add(new Customer("user_abdi", "user_abdi"));

        // Barang
        // === Kategori: MAKANAN (10 Item) ===
        // Format: ID, Nama, Harga, Stok, Expired Date
        DataStore.listBarang.add(new Makanan("F01", "Indomie Goreng", 3500, 200, "10/12/2025"));
        DataStore.listBarang.add(new Makanan("F02", "Roti Tawar Sari Roti", 18000, 20, "05/01/2024"));
        DataStore.listBarang.add(new Makanan("F03", "Beras Pandan Wangi 5kg", 75000, 15, "20/12/2026"));
        DataStore.listBarang.add(new Makanan("F04", "Minyak Goreng Bimoli 2L", 42000, 30, "15/08/2025"));
        DataStore.listBarang.add(new Makanan("F05", "Telur Ayam (Tray 10 Btr)", 22000, 25, "10/01/2024"));
        DataStore.listBarang.add(new Makanan("F06", "Kecap Bango Manis 550ml", 24000, 40, "12/12/2025"));
        DataStore.listBarang.add(new Makanan("F07", "Saus Sambal ABC", 12000, 50, "30/06/2025"));
        DataStore.listBarang.add(new Makanan("F08", "Chitato Sapi Panggang", 11000, 60, "20/05/2025"));
        DataStore.listBarang.add(new Makanan("F09", "Silverqueen Cashew", 16000, 45, "14/02/2025"));
        DataStore.listBarang.add(new Makanan("F10", "Tepung Segitiga Biru 1kg", 14500, 30, "01/01/2026"));

        // === Kategori: MINUMAN (10 Item) ===
        // Format: ID, Nama, Harga, Stok, Ukuran/Volume
        DataStore.listBarang.add(new Minuman("D01", "Aqua Botol", 4000, 100, "600ml"));
        DataStore.listBarang.add(new Minuman("D02", "Coca Cola", 7500, 50, "390ml"));
        DataStore.listBarang.add(new Minuman("D03", "Teh Botol Sosro", 5000, 80, "450ml"));
        DataStore.listBarang.add(new Minuman("D04", "Bear Brand (Susu Beruang)", 10000, 60, "189ml"));
        DataStore.listBarang.add(new Minuman("D05", "Ultra Milk Coklat", 7500, 40, "250ml"));
        DataStore.listBarang.add(new Minuman("D06", "Kopi Kapal Api (1 Pack)", 15000, 30, "10 Sachet"));
        DataStore.listBarang.add(new Minuman("D07", "Pocari Sweat", 8000, 45, "500ml"));
        DataStore.listBarang.add(new Minuman("D08", "Yakult (1 Pack)", 11000, 20, "5 x 65ml"));
        DataStore.listBarang.add(new Minuman("D09", "Floridina Orange", 3500, 70, "350ml"));
        DataStore.listBarang.add(new Minuman("D10", "Good Day Cappuccino", 9000, 50, "5 Sachet"));

        // === Kategori: KESEHATAN (10 Item) ===
        // Format: ID, Nama, Harga, Stok, Butuh Resep (Boolean)
        DataStore.listBarang.add(new Kesehatan("K01", "Panadol Merah (Sakit Kepala)", 12500, 50, false));
        DataStore.listBarang.add(new Kesehatan("K02", "Bodrex Tablet", 5000, 100, false));
        DataStore.listBarang.add(new Kesehatan("K03", "Betadine Antiseptik", 18000, 20, false));
        DataStore.listBarang.add(new Kesehatan("K04", "Hansaplast Kain (10s)", 6000, 80, false));
        DataStore.listBarang.add(new Kesehatan("K05", "Minyak Kayu Putih Cap Lang", 28000, 30, false));
        DataStore.listBarang.add(new Kesehatan("K06", "Promag (Obat Maag)", 9000, 40, false));
        DataStore.listBarang.add(new Kesehatan("K07", "Tolak Angin Cair (1 Box)", 42000, 25, false));
        DataStore.listBarang.add(new Kesehatan("K08", "Masker Medis (Box Isi 50)", 35000, 15, false));
        DataStore.listBarang.add(new Kesehatan("K09", "Vitamin C IPI", 7000, 60, false));
        DataStore.listBarang.add(new Kesehatan("K10", "Sanmol Sirup Anak", 22000, 20, false));

        // === Kategori: PERAWATAN DIRI (10 Item) ===
        // Format: ID, Nama, Harga, Stok, Merk
        DataStore.listBarang.add(new Perawatan("P01", "Lifebuoy Sabun Cair", 26000, 30, "Lifebuoy"));
        DataStore.listBarang.add(new Perawatan("P02", "Shampoo Pantene Anti-Dandruff", 38000, 20, "Pantene"));
        DataStore.listBarang.add(new Perawatan("P03", "Pepsodent Pasta Gigi", 13000, 50, "Pepsodent"));
        DataStore.listBarang.add(new Perawatan("P04", "Biore Men Facial Wash", 32000, 25, "Biore"));
        DataStore.listBarang.add(new Perawatan("P05", "Rexona Roll On Women", 19000, 30, "Rexona"));
        DataStore.listBarang.add(new Perawatan("P06", "Vaseline Body Lotion", 35000, 15, "Vaseline"));
        DataStore.listBarang.add(new Perawatan("P07", "Garnier Micellar Water", 45000, 20, "Garnier"));
        DataStore.listBarang.add(new Perawatan("P08", "Sikat Gigi Formula", 7000, 60, "Formula"));
        DataStore.listBarang.add(new Perawatan("P09", "Listerine Mouthwash", 24000, 25, "Listerine"));
        DataStore.listBarang.add(new Perawatan("P10", "Head & Shoulders Menthol", 39000, 18, "H&S"));

        // === Kategori: RUMAH TANGGA (10 Item) ===
        // Format: ID, Nama, Harga, Stok, Kegunaan/Fungsi
        DataStore.listBarang.add(new RumahTangga("H01", "Rinso Molto Deterjen", 28000, 40, "Cuci Baju"));
        DataStore.listBarang.add(new RumahTangga("H02", "Sunlight Jeruk Nipis", 16000, 50, "Cuci Piring"));
        DataStore.listBarang.add(new RumahTangga("H03", "Wipol Karbol Wangi", 19000, 30, "Pembersih Lantai"));
        DataStore.listBarang.add(new RumahTangga("H04", "Tisu Wajah Nice 180s", 14000, 60, "Tisu"));
        DataStore.listBarang.add(new RumahTangga("H05", "Baygon Semprot Nyamuk", 38000, 20, "Anti Serangga"));
        DataStore.listBarang.add(new RumahTangga("H06", "Stella Pewangi Ruangan", 18000, 25, "Pewangi"));
        DataStore.listBarang.add(new RumahTangga("H07", "Baterai ABC Alkaline AA", 12000, 50, "Elektronik"));
        DataStore.listBarang.add(new RumahTangga("H08", "Spons Cuci Piring", 3500, 100, "Dapur"));
        DataStore.listBarang.add(new RumahTangga("H09", "Kapur Barus Bagus", 15000, 30, "Pengusir Kecoa"));
        DataStore.listBarang.add(new RumahTangga("H10", "Sapu Ijuk Lantai", 25000, 15, "Kebersihan"));

        // Jalankan GUI
        javax.swing.SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}