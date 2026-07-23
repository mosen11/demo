package com.demo.recipe.controller;

import com.demo.recipe.model.Recipe;
import com.demo.recipe.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
//@RequiredArgsConstructor
@Tag(name = "Recipes", description = "Recipe management APIs")
public class RecipeController {

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    @Operation(summary = "Fetch all recipes with optional filtering")
    public List<Recipe> getRecipes(
            @Parameter(description = "Filter by vegetarian status") @RequestParam(required = false) Boolean vegetarian,
            @Parameter(description = "Filter by number of servings") @RequestParam(required = false) Integer servings,
            @Parameter(description = "Include recipes containing these ingredients") @RequestParam(required = false) List<String> includeIngredients,
            @Parameter(description = "Exclude recipes containing these ingredients") @RequestParam(required = false) List<String> excludeIngredients,
            @Parameter(description = "Filter by text in instructions") @RequestParam(required = false) String instructions) {
        return recipeService.getAllRecipes(vegetarian, servings, includeIngredients, excludeIngredients, instructions);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get recipe by ID")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable Long id) {
        return recipeService.getRecipeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a new recipe")
    public Recipe addRecipe(@Valid @RequestBody Recipe recipe) {
        return recipeService.saveRecipe(recipe);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing recipe")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable Long id, @Valid @RequestBody Recipe recipe) {
        return recipeService.getRecipeById(id)
                .map(existingRecipe -> {
                    recipe.setId(id);
                    return ResponseEntity.ok(recipeService.saveRecipe(recipe));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove a recipe")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        return recipeService.getRecipeById(id)
                .map(recipe -> {
                    recipeService.deleteRecipe(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
