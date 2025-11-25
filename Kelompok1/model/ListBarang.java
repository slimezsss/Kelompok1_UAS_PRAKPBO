package model;
import java.util.ArrayList;

/**
 * Bertindak sebagai Database In-Memory.
 * Menyimpan semua data statis (Barang, User, Transaksi).
 */
public class ListBarang {
    public static ArrayList<Barang> dbBarang = new ArrayList<>();
    public static ArrayList<Akun> dbAkun = new ArrayList<>();
    public static ArrayList<Transaksi> dbTransaksi = new ArrayList<>();
    
    // Menyimpan siapa yang sedang login saat ini
    public static Akun sessionUser; 
}