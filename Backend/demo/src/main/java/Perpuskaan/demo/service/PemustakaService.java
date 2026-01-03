package Perpuskaan.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import Perpuskaan.demo.dto.response.UpdateStatusPemustakaResponse;
import Perpuskaan.demo.entity.Pemustaka;
import Perpuskaan.demo.entity.StatusKeanggotaan;
import Perpuskaan.demo.repository.PemustakaRepository;

@Service
@RequiredArgsConstructor
public class PemustakaService {

    private final PemustakaRepository pemustakaRepository;

   @Transactional
    public UpdateStatusPemustakaResponse updateStatus(
            Integer pemustakaId,
            StatusKeanggotaan status
    ) {
        Pemustaka pemustaka = pemustakaRepository.findById(pemustakaId)
                .orElseThrow(() ->
                    new RuntimeException("Pemustaka tidak ditemukan"));

        pemustaka.setStatusKeanggotaan(status);
        pemustakaRepository.save(pemustaka);

        UpdateStatusPemustakaResponse dto = new UpdateStatusPemustakaResponse();
        dto.setIdUser(pemustaka.getIdUser());
        dto.setUsername(pemustaka.getUsername());
        dto.setStatusKeanggotaan(pemustaka.getStatusKeanggotaan().name());

        return dto;
    }

}
