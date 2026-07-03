package com.demo.recipe.service;

import com.demo.recipe.model.Recipe;
import com.demo.recipe.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeService recipeService;

    private Recipe testRecipe;

    @BeforeEach
    void setUp() {
        testRecipe = new Recipe();
        testRecipe.setId(1L);
        testRecipe.setName("Pasta");
        testRecipe.setVegetarian(true);
        testRecipe.setServings(2);
        testRecipe.setIngredients(Set.of("Pasta", "Tomato"));
        testRecipe.setInstructions("Boil pasta and add tomato sauce");
    }

    @Test
    void shouldGetAllRecipesWithFilters() {
        when(recipeRepository.findAll(any(Specification.class))).thenReturn(List.of(testRecipe));
        
        List<Recipe> result = recipeService.getAllRecipes(true, 2, List.of("Pasta"), null, "Boil");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Pasta");
        verify(recipeRepository).findAll(any(Specification.class));
    }

    @Test
    void shouldGetAllRecipesWithInstructionSearch() {
        when(recipeRepository.findAll(any(Specification.class))).thenReturn(List.of(testRecipe));

        List<Recipe> result = recipeService.getAllRecipes(null, null, null, null, "Boil");

        assertThat(result).hasSize(1);
        verify(recipeRepository).findAll(any(Specification.class));
    }

    @Test
    void shouldGetRecipeById() {
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(testRecipe));

        Optional<Recipe> result = recipeService.getRecipeById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Pasta");
    }

    @Test
    void shouldSaveRecipe() {
        when(recipeRepository.save(any(Recipe.class))).thenReturn(testRecipe);

        Recipe result = recipeService.saveRecipe(testRecipe);

        assertThat(result.getName()).isEqualTo("Pasta");
    }

    @Test
    void shouldDeleteRecipe() {
        doNothing().when(recipeRepository).deleteById(1L);

        recipeService.deleteRecipe(1L);

        verify(recipeRepository, times(1)).deleteById(1L);
    }
}
