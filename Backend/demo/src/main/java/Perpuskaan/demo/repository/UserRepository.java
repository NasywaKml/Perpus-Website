package Perpuskaan.demo.repository;
import java.util.Optional;
import java.util.List;
import Perpuskaan.demo.entity.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Perpuskaan.demo.entity.Pemustaka;
import Perpuskaan.demo.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByUsername(String username);
    Optional<Pemustaka> findByNoAnggota(Integer noAnggota);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    List<User> findByRole(Role role);
}