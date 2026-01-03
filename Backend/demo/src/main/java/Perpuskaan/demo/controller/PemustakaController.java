package Perpuskaan.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import Perpuskaan.demo.dto.request.UpdateStatusPemustakaRequest;
import Perpuskaan.demo.entity.StatusKeanggotaan;
import Perpuskaan.demo.service.PemustakaService;

@RestController
@RequestMapping("/api/pemustaka")
@RequiredArgsConstructor
public class PemustakaController {

    private final PemustakaService pemustakaService;

    /**
     * Update status keanggotaan pemustaka (ADMIN ONLY)
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateStatusPemustaka(
            @PathVariable Integer id,
            @RequestBody UpdateStatusPemustakaRequest request
    ) {
        return ResponseEntity.ok(
            pemustakaService.updateStatus(id, request.getStatusKeanggotaan())
        );
    }

}
