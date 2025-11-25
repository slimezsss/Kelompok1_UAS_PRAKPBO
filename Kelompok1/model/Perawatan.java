package model;

public class Perawatan extends Barang {
    private String merk;

    public Perawatan(String id, String n, double h, int s, String m) {
        super(id, n, h, s);
        this.merk = m;
    }

    @Override public String getKategori() { return "Perawatan"; }
    @Override public String getDetailKhusus() { return "Merk: " + merk; }
}