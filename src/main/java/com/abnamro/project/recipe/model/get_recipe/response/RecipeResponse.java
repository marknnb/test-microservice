package com.abnamro.project.recipe.model.get_recipe.response;

import java.util.List;
import lombok.Builder;

@Builder
public class RecipeResponse {
    public String recipeId;
    public String name;
    public String type;
    public int numberOfServings;
    public List<String> ingredientResponses;
    public List<String> instructionResponses;
}
