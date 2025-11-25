package model;

public class Kesehatan extends Barang {
    private boolean butuhResep;

    public Kesehatan(String id, String n, double h, int s, boolean r) {
        super(id, n, h, s);
        this.butuhResep = r;
    }

    @Override public String getKategori() { return "Kesehatan"; }
    @Override public String getDetailKhusus() { return butuhResep ? "Wajib Resep" : "Obat Bebas"; }
}