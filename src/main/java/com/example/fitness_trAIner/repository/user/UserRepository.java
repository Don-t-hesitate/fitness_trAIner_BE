package com.example.fitness_trAIner.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User save(User user);
    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);

    void deleteById(Long id);
    Boolean existsByUsername(String username);
    Boolean existsByNickname(String nickname);
}
