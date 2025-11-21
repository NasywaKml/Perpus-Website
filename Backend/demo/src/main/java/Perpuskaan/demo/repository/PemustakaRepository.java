package Perpuskaan.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Perpuskaan.demo.entity.Pemustaka;

@Repository
public interface PemustakaRepository extends JpaRepository<Pemustaka, Integer> {
}
