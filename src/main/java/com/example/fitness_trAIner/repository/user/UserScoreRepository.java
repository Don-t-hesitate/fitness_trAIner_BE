package com.example.fitness_trAIner.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserScoreRepository extends JpaRepository<UserScore, Long> {
}
