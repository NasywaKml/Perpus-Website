package Perpuskaan.demo.dto.response;


import lombok.Data;
// import lombok.Getter;
// import lombok.Setter;

@Data
public class BukuSearchResponseDto {
    private Integer idBuku;
    private String judul;
    private String isbn;
    private String kategori;
    private String pengarang;
    private String penerbit;
    private Integer tahunTerbit;
    private String jenis;
    private String noRak;
    private String abstrak;
    private Integer jumlahStok;
    private Double hargaSewa;
    private Double dendaPerHari;
    private String status;
    private String urlGambarSampul;

}
