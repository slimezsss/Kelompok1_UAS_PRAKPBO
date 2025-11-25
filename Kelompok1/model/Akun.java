package model;

public abstract class Akun {
    protected String username;
    protected String password;
    protected String role;

    public Akun(String u, String p, String r) {
        this.username = u;
        this.password = p;
        this.role = r;
    }

    public boolean login(String u, String p) {
        return this.username.equals(u) && this.password.equals(p);
    }
    public String getUsername() { return username; }
}