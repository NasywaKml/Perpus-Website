package Perpuskaan.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "buku")
public class Buku {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int idBuku;

    @Column(name="judul")
    private String judul;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "pengarang")
    private String pengarang;

    @Column(name = "penerbit")
    private String penerbit;

    @Column(name = "tahunTerbit")
    private int tahunTerbit;

    @Column (name="no_rak")
    private int no_rak;

    @Column ( name = "abstrak")
    private String abstrak;

    @Column (name = "jumlah_stock")
    private int jumlah_stock;

    @Column (name = "harga_sewa")
    private double harga_sewa;

    @Column(name  = "denda_per_hari")
    private double denda_per_hari;

    @Column(name = "url_gambar_sampul")
    private String url;

    @Column(name = "status")
    private boolean status;

    public Buku(){

    }

    public Buku(String judul, String isbn, String pengarang, String penerbit, int tahunTerbit,
                 int no_rak, String abstrak, int jumlah_stock, double harga_sewa, double denda_per_hari, 
                 String url, boolean status){
        this.judul = judul;
        this.isbn = isbn;
        this.pengarang = pengarang;
        this.penerbit = penerbit;
        this.tahunTerbit = tahunTerbit;
        this.no_rak = no_rak;
        this.abstrak = abstrak;
        this.jumlah_stock = jumlah_stock;
        this.harga_sewa = harga_sewa;
        this.denda_per_hari = denda_per_hari;
        this.url = url;
        this.status = status;
    }

    public int getIdBuku() {
        return idBuku;
    }
    public String getJudul() {
        return judul;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPengarang() {
        return pengarang;
    }

    public void setPengarang(String pengarang) {
        this.pengarang = pengarang;
    }

    public String getPenerbit() {
        return penerbit;
    }

    public void setPenerbit(String penerbit) {
        this.penerbit = penerbit;
    }

    public int getTahunTerbit() {
        return tahunTerbit;
    }

    public void setTahunTerbit(int tahunTerbit) {
        this.tahunTerbit = tahunTerbit;
    }

    public int getNo_rak() {
        return no_rak;
    }

    public void setNo_rak(int no_rak) {
        this.no_rak = no_rak;
    }

    public String getAbstrak() {
        return abstrak;
    }

    public void setAbstrak(String abstrak) {
        this.abstrak = abstrak;
    }

    public int getJumlah_stock() {
        return jumlah_stock;
    }

    public void setJumlah_stock(int jumlah_stock) {
        this.jumlah_stock = jumlah_stock;
    }

    public double getHarga_sewa() {
        return harga_sewa;
    }

    public void setHarga_sewa(double harga_sewa) {
        this.harga_sewa = harga_sewa;
    }

    public double getDenda_per_hari() {
        return denda_per_hari;
    }

    public void setDenda_per_hari(double denda_per_hari) {
        this.denda_per_hari = denda_per_hari;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}
