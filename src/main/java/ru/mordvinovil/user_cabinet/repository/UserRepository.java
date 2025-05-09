package ru.mordvinovil.user_cabinet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mordvinovil.user_cabinet.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    void deleteByEmail(String email);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
