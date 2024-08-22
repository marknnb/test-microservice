package com.abnamro.project.recipe;

import com.abnamro.project.recipe.config.TestConfigs;
import org.springframework.boot.SpringApplication;

public class TestRecipeServiceApplication {
    public static void main(String[] args) {
        SpringApplication.from(RecipeServiceApplication::main)
                .with(TestConfigs.class)
                .run(args);
    }
}
