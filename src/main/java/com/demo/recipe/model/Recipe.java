package com.demo.recipe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotNull(message = "Vegetarian staus is mandatory")
    private Boolean vegetarian;

    @Min(value = 1, message = "Servings must be at least 1")
    private Integer servings;

    @ElementCollection
    @CollectionTable(name = "recipe_ingredients", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "ingredient")
    private Set<String> ingredients = new HashSet<>();

    @NotBlank(message = "Instructions are mandatory")
    @Column(length = 2000)
    private String instructions;

}
