package com.demo.recipe.service;

import com.demo.recipe.model.Recipe;
import com.demo.recipe.repository.RecipeRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
//@RequiredArgsConstructor
public class RecipeService {


    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }


    /**
     * This method return all recipe base on filters
     * @param vegetarian Filter by vegetarian status
     * @param servings Filter by number of servings
     * @param includeIngredients Include recipes containing these ingredients
     * @param excludeIngredients Exclude recipes containing these ingredients
     * @param instructionsQuery Filter by text in instructions
     * @return List of matching recipes
     */
    @Transactional(readOnly = true)
    public List<Recipe> getAllRecipes(Boolean vegetarian, Integer servings, List<String> includeIngredients, List<String> excludeIngredients, String instructionsQuery) {
        return recipeRepository.findAll(Specification.where(filterByVegetarian(vegetarian))
                .and(filterByServings(servings))
                .and(filterByIncludeIngredients(includeIngredients))
                .and(filterByExcludeIngredients(excludeIngredients))
                .and(filterByInstructions(instructionsQuery)));
    }

    @Transactional(readOnly = true)
    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
    }

    @Transactional
    public Recipe saveRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    @Transactional
    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }


    private Specification<Recipe> filterByVegetarian(Boolean vegetarian) {
        return (root, query, cb) -> vegetarian == null ? null : cb.equal(root.get("vegetarian"), vegetarian);
    }

    private Specification<Recipe> filterByServings(Integer servings) {
        return (root, query, cb) -> servings == null ? null : cb.equal(root.get("servings"), servings);
    }

    private Specification<Recipe> filterByIncludeIngredients(List<String> includeIngredients) {
        return (root, query, cb) -> {
            if (includeIngredients == null || includeIngredients.isEmpty()) {
                return null;
            }
            List<Predicate> predicates = new ArrayList<>();
            for (String ingredient : includeIngredients) {
                Join<Recipe, String> ingredientsJoin = root.join("ingredients");
                predicates.add(cb.like(cb.lower(ingredientsJoin), "%" + ingredient.toLowerCase() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Specification<Recipe> filterByExcludeIngredients(List<String> excludeIngredients) {
        return (root, query, cb) -> {
            if (excludeIngredients == null || excludeIngredients.isEmpty()) {
                return null;
            }

            // Subquery to find recipes that HAVE any of the excluded ingredients
            var subquery = query.subquery(Long.class);
            var subRoot = subquery.from(Recipe.class);
            Join<Recipe, String> subIngredientsJoin = subRoot.join("ingredients");
            
            List<Predicate> subPredicates = new ArrayList<>();
            for (String ingredient : excludeIngredients) {
                subPredicates.add(cb.like(cb.lower(subIngredientsJoin), "%" + ingredient.toLowerCase() + "%"));
            }
            
            subquery.select(subRoot.get("id")).where(cb.or(subPredicates.toArray(new Predicate[0])));
            
            return cb.not(root.get("id").in(subquery));
        };
    }

    private Specification<Recipe> filterByInstructions(String instructionsQuery) {
        return (root, query, cb) -> instructionsQuery == null || instructionsQuery.isBlank() ? null :
                cb.like(cb.lower(root.get("instructions")), "%" + instructionsQuery.toLowerCase() + "%");
    }
}
