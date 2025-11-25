package model;

public class Minuman extends Barang {
    private String ukuran;

    public Minuman(String id, String n, double h, int s, String u) {
        super(id, n, h, s);
        this.ukuran = u;
    }

    @Override public String getKategori() { return "Minuman"; }
    @Override public String getDetailKhusus() { return "Isi: " + ukuran; }
}