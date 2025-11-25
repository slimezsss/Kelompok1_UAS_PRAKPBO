package model;

public class Makanan extends Barang {
    private String expiredDate;

    public Makanan(String id, String n, double h, int s, String exp) {
        super(id, n, h, s);
        this.expiredDate = exp;
    }

    @Override public String getKategori() { return "Makanan"; }
    @Override public String getDetailKhusus() { return "Exp: " + expiredDate; }
}