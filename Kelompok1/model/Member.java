package model;

public class Member extends Customer {
    private int poin;

    public Member(String u, String p) {
        super(u, p, "MEMBER");
        this.poin = 0;
    }

    public int getPoin() { return poin; }

    public void tambahPoin(double total) {
        // 1000 Rupiah = 100 Poin
        this.poin += (int)(total / 1000) * 10;
    }

    public void pakaiPoin(int jumlah) {
        if (jumlah <= poin) poin -= jumlah;
    }
}