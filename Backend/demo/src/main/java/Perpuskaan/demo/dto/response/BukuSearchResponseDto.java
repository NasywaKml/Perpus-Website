package Perpuskaan.demo.dto.response;

import java.util.Date;

import lombok.Data;
// import lombok.Getter;
// import lombok.Setter;

@Data
public class BukuSearchResponseDto {
    private String judul;
    private String isbn;
    private String kategori;
    private String pengarang;
    private Date tahunTerbit;
    private String jenis;
    private String noRak;
    private String abstrak;
    private Integer jumlahStok;
    private String urlGambarSampul;

}
