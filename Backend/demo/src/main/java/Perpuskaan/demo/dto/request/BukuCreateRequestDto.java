package Perpuskaan.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BukuCreateRequestDto {

    @NotBlank
    private String judul;

    private String isbn;

    private String kategori;

    private String pengarang;

    private String penerbit;

    @NotNull
    private Integer tahunTerbit;

    private String jenis;

    private String noRak;

    private String abstrak;

    @NotNull
    private Integer jumlahStok;

    private Double hargaSewa;

    private Double dendaPerHari;

    private String status;

    private String urlGambarSampul;
}
