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
2. [Class Diagram](#-class-diagram)
3. [Fitur Utama](#-fitur-utama)
4. [Cara Run](#-cara-run)
5. [Struktur Folder](#-struktur-folder)

---

## 🖼 Screenshot GUI
> Beberapa tampilan GUI DailyMart

- **Login**
![WhatsApp Image 2025-11-25 at 02 04 47_1e44988d](https://github.com/user-attachments/assets/9ac72aaf-a913-4132-aa1b-2d06ab55db27)

- **Dashboard Admin**
  <img width="1536" height="864" alt="Screenshot_2025-11-25_01_34_24 1" src="https://github.com/user-attachments/assets/82a42011-2135-4e36-be29-b8157fc632a5" />
  

- **Dashboard Customer**
  <img width="1536" height="864" alt="Screenshot_2025-11-25_01_34_02 1" src="https://github.com/user-attachments/assets/a4733837-d5f1-418f-8b25-579f8b46facb" />



---


## 🧩 Class Diagram
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

1. Masuk ke folder project
```bash
cd Kelompok1
```

2. Compile semua file
```bash
javac -d out -cp src $(find . -name "*.java")
```

3. Jalankan aplikasi
```bash
java -cp out:src Main
```


## 📁 Struktur Folder
       Kelompok1/
       │
       ├── Main.java
       │
       ├── model/
       │   ├── Admin.java
       │   ├── Akun.java
       │   ├── Bank.java
       │   ├── Barang.java
       │   ├── BarangWithImage.java
       │   ├── COD.java
       │   ├── Customer.java
       │   ├── DataStore.java
       │   ├── Kesehatan.java
       │   ├── ListBarang.java
       │   ├── Makanan.java
       │   ├── Member.java
       │   ├── Minuman.java
       │   ├── Pembayaran.java
       │   ├── Perawatan.java
       │   ├── Promo.java
       │   ├── QRIS.java
       │   ├── RumahTangga.java
       │   └── Transaksi.java
       │
       ├── util/
       │   └── StokHabisException.java
       │
       ├── view/
       │   ├── AdminFrame.java
       │   ├── CustomerFrame.java
       │   ├── LoginFrame.java
       │   └── RegisterFrame.java
       │
       └── src/
           └── image/
               └── default-barang.png
