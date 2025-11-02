package Perpuskaan.demo.repository;
import java.util.Optional;

import Perpuskaan.demo.entity.User;
// import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Perpuskaan.demo.entity.Pemustaka;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Pemustaka findByNoAnggota(String noAnggota);
    Optional<User> findByUsername(String username);
}