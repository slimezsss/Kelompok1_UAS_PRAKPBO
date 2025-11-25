package model;

public class COD implements Pembayaran {
    @Override
    public String prosesBayar() { return "Siapkan Uang Tunai untuk Kurir."; }
    @Override
    public String getNamaMetode() { return "COD (Bayar di Tempat)"; }
}