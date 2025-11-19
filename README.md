# 🏪 Kelompok1_UAS_PRAKPBO
**UAS Praktikum Pemrograman Berorientasi Objek**  


| Nama Anggota Kelompok 1    | NPM            |
|----------------------------|----------------|
| Muhammad Hafidz Zuliesky   | 123456789      |
| Hazairin                   | 2408107010001  |
| Rijaluddin Abdul Ghani     | 2408107010008  |
| Abdi Dzil Ikram            | 2408107010024  |


---

## 🛒 DailyMart
**DailyMart** adalah aplikasi toko online sederhana yang dibuat dengan **Java Swing**.  
Aplikasi ini menggunakan konsep **OOP (Object-Oriented Programming)** dan **MVC (Model-View-Controller)** agar program lebih terstruktur dan mudah dikembangkan.  

Fitur utamanya:  
- **Admin:** bisa mengelola stok barang, mengatur FlashSale, dan memantau transaksi.  
- **Customer:** bisa belanja, menggunakan voucher, melihat keranjang, dan memantau riwayat transaksi.  

Fitur tambahan:  
- **FlashSale & diskon** untuk promosi barang  
- **Sistem poin member** untuk pelanggan setia  
- **Invoice & transaksi otomatis** untuk mencatat pembelian  
- Barang terbagi dalam kategori: **Makanan, Minuman, Rumah Tangga, Perawatan, Kesehatan**  

---

## 📖 Daftar Isi
1. [Screenshot GUI](#-screenshot-gui)
2. [UML Class Diagram](#-uml-class-diagram)
3. [Fitur Utama](#-fitur-utama)
4. [Cara Run](#-cara-run)
5. [Struktur Folder](#-struktur-folder)

---

## 🖼 Screenshot GUI
> Beberapa tampilan GUI DailyMart

- **Login**
![Login](screenshots/login.png)  

- **Dashboard Admin**
![Admin](screenshots/admin.png)  

- **Dashboard Customer**
![Customer](screenshots/customer.png)  

- **FlashSale & Keranjang**
![FlashSale](screenshots/flashsale.png)  

---


## 🧩 UML Class Diagram
> Visualisasi Class Diagram DailyMart

<img width="4626" height="3758" alt="class diagram DailyMart" src="https://github.com/user-attachments/assets/e9bd3924-c4be-4c5f-a858-30d1ccca7219" />

---


## ✨ Fitur Utama
-  Login **Admin** & **Customer**  
-  Admin mengelola **stok barang** & **FlashSale**  
-  Customer bisa **klaim voucher**, **lihat keranjang**, dan **checkout**  
-  Sistem **poin member**  
-  **Transaksi & invoice** otomatis  
-  Barang dikategorikan: **Makanan, Minuman, Rumah Tangga, Perawatan, Kesehatan**  

---


## 🚀 Cara Run
1. Clone repository:  
```bash
git clone https://github.com/username/Kelompok1_UAS_PRAKPBO.git
```

2. Masuk folder src:
```bash
cd src
```

3. Compile & jalankan program:
```bash
javac main/Main.java
java main.Main
```


## 📁 Struktur Folder
       src/
    ├── main/
    │   └── Main.java               # Menjalankan aplikasi & GUI login
    ├── util/
    │   └── StokHabisException.java # Exception khusus saat stok barang habis
    ├── model/
    │   ├── Pembayaran.java         # Interface pembayaran
    │   ├── QRIS.java               # Implementasi pembayaran via QRIS
    │   ├── Bank.java               # Implementasi pembayaran via Bank
    │   ├── COD.java                # Implementasi pembayaran COD
    │   ├── Akun.java               # Class abstrak akun (Admin/Customer)
    │   ├── Admin.java              # Data & metode admin
    │   ├── Customer.java           # Data & metode customer
    │   ├── Member.java             # Customer member & sistem poin
    │   ├── Barang.java             # Class abstrak barang
    │   ├── Makanan.java            # Kategori makanan
    │   ├── Minuman.java            # Kategori minuman
    │   ├── Kesehatan.java          # Kategori kesehatan
    │   ├── Perawatan.java          # Kategori perawatan
    │   ├── RumahTangga.java        # Kategori rumah tangga
    │   ├── ListBarang.java         # Menyimpan daftar semua barang
    │   └── Promo.java              # Class promo / flashsale
    └── view/
        ├── LoginFrame.java         # GUI login
        ├── AdminFrame.java         # GUI dashboard admin
        └── CustomerFrame.java      # GUI dashboard customer
    
    
    
