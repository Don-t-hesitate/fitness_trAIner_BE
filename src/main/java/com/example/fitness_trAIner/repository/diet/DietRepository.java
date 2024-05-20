package com.example.fitness_trAIner.repository.diet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DietRepository extends JpaRepository<Diet, Long> {
    Diet save(Diet diet);

    List<Diet> findByUserIdAndEatDate(Long userId, LocalDate parse);
}