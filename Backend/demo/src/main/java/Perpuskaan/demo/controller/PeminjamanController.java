package Perpuskaan.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Perpuskaan.demo.dto.request.PeminjamanRequestDto;
import Perpuskaan.demo.dto.response.PeminjamanResponseDto;
import Perpuskaan.demo.service.PeminjamanService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/peminjaman")
@RequiredArgsConstructor
public class PeminjamanController {

    private final PeminjamanService peminjamanService;

    @PostMapping("/pinjam")
    public PeminjamanResponseDto pinjam(@RequestBody PeminjamanRequestDto req) {
        return peminjamanService.pinjam(req);
    }

    @GetMapping
    public List<PeminjamanResponseDto> getAll() {
        return peminjamanService.getAll();
    }

    @GetMapping("/{id}")
    public PeminjamanResponseDto getById(@PathVariable Integer id) {
        return peminjamanService.getById(id);
    }

    @GetMapping("/user/{idUser}")
    public List<PeminjamanResponseDto> getLogPeminjamanUser(@PathVariable Integer idUser) {
        return peminjamanService.getAllByUserId(idUser);
    }

}
