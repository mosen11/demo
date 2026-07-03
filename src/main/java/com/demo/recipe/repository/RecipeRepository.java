package com.demo.recipe.repository;

import com.demo.recipe.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * For now just CRUD functionalities. May be we can add specific queries here. Please use JPA CriteriaBuilder and CriteriaQuery for complex queries
 */
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long>, JpaSpecificationExecutor<Recipe> {
}
