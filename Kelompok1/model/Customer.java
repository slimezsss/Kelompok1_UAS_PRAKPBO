package model;

public class Customer extends Akun {
    public Customer(String u, String p) {
        super(u, p, "CUSTOMER");
    }
    protected Customer(String u, String p, String r) {
        super(u, p, r);
    }
}