package com.abnamro.project.recipe.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeSearch {
    @Schema(description = "The Type of the recipe", example = "VEGETARIAN")
    String recipeType;

    @Schema(description = "No. of service for Recipe", example = "4")
    Integer servings;

    @Schema(description = "Ingredients included  in Recipe", example = "Onions")
    String ingredientName;

    @Schema(description = "Ingredients must not be  in Recipe", example = "Pasta")
    String excludeIngredientName;

    @Schema(description = "Instruction text  must not be  in Recipe", example = "Pasta")
    String instructionText;
}
