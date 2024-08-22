package com.abnamro.project.recipe.service;

import com.abnamro.project.recipe.model.RecipeRequest;
import com.abnamro.project.recipe.model.RecipeSearch;
import org.owasp.encoder.Encode;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class InputSanitizationService {

    public String sanitize(String input) {
        if (!StringUtils.hasLength(input)) {
            return input;
        }
        // Encode input to prevent XSS attacks
        return Encode.forHtml(input);
    }

    public void sanitizeRecipe(RecipeSearch recipeRequest) throws IllegalArgumentException {
        String searchRequest = recipeRequest.getRecipeType()
                + recipeRequest.getServings()
                + recipeRequest.getInstructionText()
                + recipeRequest.getExcludeIngredientName()
                + recipeRequest.getIngredientName();
        String sanitizedSearchRequest = sanitize(searchRequest);
        if (containsIllegalCharacters(sanitizedSearchRequest)) {
            throw new IllegalArgumentException("Input contains illegal characters");
        }
    }

    public void sanitizeRecipe(RecipeRequest recipeRequest) throws IllegalArgumentException {
        String name = recipeRequest.getName();
        String ingredients = String.join(",", recipeRequest.getIngredients());
        String instructions = String.join(",", recipeRequest.getInstructions());

        String sanitizedRecipeName = sanitize(name);
        String sanitizedIngredients = sanitize(ingredients);
        String sanitizedInstructions = sanitize(instructions);

        if (containsIllegalCharacters(sanitizedRecipeName)
                || containsIllegalCharacters(sanitizedIngredients)
                || containsIllegalCharacters(sanitizedInstructions)) {
            throw new IllegalArgumentException("Input contains illegal characters");
        }
    }

    private boolean containsIllegalCharacters(String input) {
        // Check for any illegal characters you want to prevent
        String illegalCharacters = "<>\"'"; // Add more characters as needed
        for (char c : illegalCharacters.toCharArray()) {
            if (input.indexOf(c) != -1) {
                return true;
            }
        }
        return false;
    }
}
