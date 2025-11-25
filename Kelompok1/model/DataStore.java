package model;
import java.util.ArrayList;

// Database Global Sederhana
public class DataStore {
    public static ArrayList<Akun> listAkun = new ArrayList<>();
    public static ArrayList<Barang> listBarang = new ArrayList<>();
    public static ArrayList<Transaksi> listTransaksi = new ArrayList<>();
    public static Akun currentUser; // User yang sedang login
}