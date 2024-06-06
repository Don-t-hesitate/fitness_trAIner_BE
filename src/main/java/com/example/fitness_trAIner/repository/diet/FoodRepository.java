package com.example.fitness_trAIner.repository.diet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, String> {
    Food findByFoodId(String foodId);
    List<Food> findByFoodName(String foodName);
}