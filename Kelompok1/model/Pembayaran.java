package model;

/**
 * Interface untuk menerapkan Polimorfisme pada metode pembayaran.
 */
public interface Pembayaran {
    String prosesBayar();
    String getNamaMetode();
}