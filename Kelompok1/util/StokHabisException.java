package util;

/**
 * Custom Exception untuk menangani kondisi stok kosong.
 * Diterapkan saat user mencoba menambahkan barang melebihi stok tersedia.
 */
public class StokHabisException extends Exception {
    public StokHabisException(String message) {
        super(message);
    }
}