package model;

public class QRIS implements Pembayaran {
    @Override
    public String prosesBayar() { return "Scan QR Code Berhasil."; }
    @Override
    public String getNamaMetode() { return "QRIS"; }
}