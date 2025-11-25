package model;

public class Promo {
    public static boolean isActive = false;
    public static int persenDiskon = 0;

    public static double hitungHarga(double hargaAsli) {
        if (isActive) {
            return hargaAsli * (100 - persenDiskon) / 100.0;
        }
        return hargaAsli;
    }
}