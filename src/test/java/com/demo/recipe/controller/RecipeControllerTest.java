package com.demo.recipe.controller;

import com.demo.recipe.model.Recipe;
import com.demo.recipe.service.RecipeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecipeController.class)
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeService recipeService;

    @Autowired
    private ObjectMapper myObjectMapper;

    private Recipe sampleRecipe;

    @BeforeEach
    void setUp() {
        sampleRecipe = Recipe.builder()
                .id(1L)
                .name("Pasta")
                .vegetarian(true)
                .servings(2)
                .ingredients(new HashSet<>(Set.of("Pasta", "Tomato")))
                .instructions("Boil pasta and add tomato sauce")
                .build();
    }

    @Test
    void shouldGetAllRecipes() throws Exception {
        when(recipeService.getAllRecipes(eq(true), eq(2), any(), any(), any()))
                .thenReturn(List.of(sampleRecipe));

        mockMvc.perform(get("/api/recipes")
                .param("vegetarian", "true")
                .param("servings", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Pasta"));
    }

    @Test
    void shouldGetRecipeById() throws Exception {
        when(recipeService.getRecipeById(1L)).thenReturn(Optional.of(sampleRecipe));

        mockMvc.perform(get("/api/recipes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pasta"));
    }

    @Test
    void shouldAddRecipe() throws Exception {
        when(recipeService.saveRecipe(any(Recipe.class))).thenReturn(sampleRecipe);

        mockMvc.perform(post("/api/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(myObjectMapper.writeValueAsString(sampleRecipe)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Pasta"));
    }

    @Test
    void shouldDeleteRecipe() throws Exception {
        when(recipeService.getRecipeById(1L)).thenReturn(Optional.of(sampleRecipe));

        mockMvc.perform(delete("/api/recipes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnBadRequestWhenRecipeIsInvalid() throws Exception {
        Recipe invalidRecipe = Recipe.builder()
                .name("") // Invalid: blank
                .vegetarian(null) // Invalid: null
                .servings(0) // Invalid: < 1
                .instructions("") // Invalid: blank
                .build();

        mockMvc.perform(post("/api/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(myObjectMapper.writeValueAsString(invalidRecipe)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Name is mandatory"))
                .andExpect(jsonPath("$.vegetarian").value("Vegetarian staus is mandatory"))
                .andExpect(jsonPath("$.servings").value("Serving must be at least 1"))
                .andExpect(jsonPath("$.instructions").value("Instructions are mandatory"));
    }
}
