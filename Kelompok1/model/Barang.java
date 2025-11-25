package model;

public abstract class Barang {
    protected String id;
    protected String nama;
    protected double harga;
    protected int stok;

    public Barang(String id, String nama, double harga, int stok) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
    }

    // Abstract Method Wajib
    public abstract String getKategori();
    public abstract String getDetailKhusus();

    public String getId() { return id; }
    public String getNama() { return nama; }
    public double getHarga() { return harga; }
    public int getStok() { return stok; }
    
    public void setStok(int s) { this.stok = s; }
    public void setNama(String n) { this.nama = n; }
    public void setHarga(double h) { this.harga = h; }

}

