package model;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Transaksi {
    private String id;
    private String username;
    private double total;
    private String metode;
    private String status;
    private Date tanggal;

    public Transaksi(String id, String u, double t, String m) {
        this.id = id;
        this.username = u;
        this.total = t;
        this.metode = m;
        this.status = "SELESAI"; 
        this.tanggal = new Date();
    }

    public String getId() { return id; }
    public String getUser() { return username; }
    public double getTotal() { return total; }
    
    public String getMetode() { return metode; } 
    public String getStatus() { return status; }
    
    public String getTanggalStr() { 
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(tanggal); 
    }
}