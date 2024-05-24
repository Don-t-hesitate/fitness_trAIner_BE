package com.example.fitness_trAIner.repository.diet;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Builder
@Table(name = "food")
public class Food implements Comparable<Food> {

    @Id
    @Column(name = "food_id", updatable = false)
    private String foodId;

    @Column(name = "food_name", nullable = false)
    private String foodName;

    @Column(name = "main_type", nullable = false)
    private String mainType;

    @Column(name = "detail_type", nullable = false)
    private String detailType;

    @Column(name = "serving_size", nullable = false)
    private Integer servingSize;

    @Column(name = "calories", nullable = false)
    private Double calories;

    @Column(name = "protein", nullable = false)
    private Double protein;

    @Column(name = "fat", nullable = false)
    private Double fat;

    @Column(name = "carbohydr", nullable = false)
    private Double carbohydr;

    @Column(name = "sugar")
    private Double sugar;

    @Column(name = "sodium")
    private Double sodium;

    @Column(name = "cholesterol")
    private Double cholesterol;

    @Column(name = "sat_fatty_acid")
    private Double satFattyAcid;

    @Column(name = "trans_fatty_acid")
    private Double transFattyAcid;

    @Column(name = "taste", nullable = false)
    private String taste;

    @Column(name = "main_ingred", nullable = false)
    private String mainIngredient;

    @Column(name = "sec_ingred", nullable = false)
    private String secondaryIngredient;

    @Column(name = "cook_method", nullable = false)
    private String cookingMethod;

    @Column(name = "allergens", nullable = false)
    private String allergens;

    @Override
    public int compareTo(Food other) {
        return this.foodName.compareTo(other.foodName);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Food food = (Food) obj;
        return Objects.equals(foodId, food.foodId);
    }

    public boolean isChanged(Food other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        return Objects.equals(foodName, other.foodName) &&
                Objects.equals(mainType, other.mainType) &&
                Objects.equals(detailType, other.detailType) &&
                Objects.equals(calories, other.calories) &&
                Objects.equals(protein, other.protein) &&
                Objects.equals(fat, other.fat) &&
                Objects.equals(carbohydr, other.carbohydr) &&
                Objects.equals(taste, other.taste) &&
                Objects.equals(mainIngredient, other.mainIngredient) &&
                Objects.equals(secondaryIngredient, other.secondaryIngredient) &&
                Objects.equals(cookingMethod, other.cookingMethod);
    }
    @Override
    public int hashCode() {
        return Objects.hash(foodId);
    }
}
