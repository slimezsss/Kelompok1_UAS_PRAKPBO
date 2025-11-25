package model;

public class Bank implements Pembayaran {
    @Override
    public String prosesBayar() { return "Transfer Virtual Account Berhasil."; }
    @Override
    public String getNamaMetode() { return "Transfer Bank"; }
}