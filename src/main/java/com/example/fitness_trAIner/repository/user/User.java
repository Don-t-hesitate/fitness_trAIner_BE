package com.example.fitness_trAIner.repository.user;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@Builder
@AllArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @Column(name = "user_id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @Column(name = "username", unique = true, nullable = false)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "nickname", nullable = false)
    private String nickname;
    @Column(name = "height")
    private Float height;
    @Column(name = "weight")
    private Float weight;
    @Column(name = "age")
    private Integer age;
    @Column(name = "role")
    private String role;
    @Column(name = "spicy_preference")
    private Integer spicyPreference;
    @Column(name = "meat_consumption")
    private Boolean meatConsumption;
    @Column(name = "taste_preference")
    private String tastePreference;
    @Column(name = "activity_level")
    private Integer activityLevel;
    @Column(name = "preference_type_food")
    private String preferenceTypeFood;
    @Column(name = "attendance_check")
    private boolean attendanceCheck;





}
