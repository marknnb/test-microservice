package com.abnamro.project.recipe.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.abnamro.project.recipe.config.TestConfigs;
import com.abnamro.project.recipe.entity.Recipe;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest(
        properties = {
            "spring.test.database.replace=none",
            "spring.datasource.url=jdbc:tc:postgresql:16-alpine:///db",
        })
@Import(TestConfigs.class)
class RecipeRepositoryTest {

    @Autowired
    RecipeRepository recipeRepository;

    @Test
    void shouldGetAllProducts() {
        List<Recipe> recipes = recipeRepository.findAll();
        assertThat(recipes).hasSize(2);
    }

    @Test
    void shouldGetRecipeById() {
        Recipe recipe = recipeRepository.findById(1L).orElseThrow();
        assertThat(recipe.getName()).isEqualTo("Recipe 1");
    }
}
