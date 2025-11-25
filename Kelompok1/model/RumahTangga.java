package model;

public class RumahTangga extends Barang {
    private String kegunaan;

    public RumahTangga(String id, String n, double h, int s, String k) {
        super(id, n, h, s);
        this.kegunaan = k;
    }

    @Override public String getKategori() { return "Rumah Tangga"; }
    @Override public String getDetailKhusus() { return "Fungsi: " + kegunaan; }
}