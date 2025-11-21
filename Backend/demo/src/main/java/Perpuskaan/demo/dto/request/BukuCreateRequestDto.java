package Perpuskaan.demo.dto.request;

import java.util.Date;

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
    private Date tahunTerbit;

    private String jenis;

    private String noRak;

    private String abstrak;

    @NotNull
    private Integer jumlahStok;

    private Double hargaSewa;

    private String urlGambarSampul;
}