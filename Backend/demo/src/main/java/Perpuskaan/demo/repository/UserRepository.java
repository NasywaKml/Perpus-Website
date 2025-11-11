package Perpuskaan.demo.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Perpuskaan.demo.entity.Pemustaka;
import Perpuskaan.demo.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<Pemustaka> findByNoAnggota(Integer noAnggota);
    Optional<User> findByUsername(String username);
}