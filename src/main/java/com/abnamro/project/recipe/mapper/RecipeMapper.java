package com.abnamro.project.recipe.mapper;

import com.abnamro.project.recipe.entity.Ingredient;
import com.abnamro.project.recipe.entity.Instruction;
import com.abnamro.project.recipe.entity.Recipe;
import com.abnamro.project.recipe.model.RecipeRequest;
import com.abnamro.project.recipe.model.get_recipe.response.RecipeResponse;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RecipeMapper {

    public List<RecipeResponse> mapToRecipeResponse(List<Recipe> content) {
        log.info(content.toString());
        return content.stream().map(this::mapToRecipeResponse).toList();
    }

    public RecipeResponse mapToRecipeResponse(Recipe recipe) {
        return Optional.of(recipe)
                .map(item -> {
                    List<Instruction> instructions = item.getInstructions();
                    List<Ingredient> ingredients = item.getIngredients();
                    return RecipeResponse.builder()
                            .recipeId(item.getId().toString())
                            .name(item.getName())
                            .type(item.getType())
                            .numberOfServings(item.getServings())
                            .ingredientResponses(ingredients.stream()
                                    .map(Ingredient::getName)
                                    .toList())
                            .instructionResponses(instructions.stream()
                                    .map(Instruction::getDescription)
                                    .toList())
                            .build();
                })
                .orElseThrow(() -> new RuntimeException("Error in mapping Recipe"));
    }

    public Recipe mapToRecipeEntity(RecipeRequest request) {
        List<String> instructions = request.getInstructions();
        List<String> ingredientIds = request.getIngredients();
        AtomicInteger count = new AtomicInteger(0);
        Recipe recipeEntity = new Recipe();
        recipeEntity.setName(request.getName());
        recipeEntity.setType(request.getType().toString());
        recipeEntity.setServings(request.getNumberOfServings());
        recipeEntity.setName(request.getName());
        recipeEntity.setInstructions(mapToInstructionEntity(instructions, count, recipeEntity));
        recipeEntity.setIngredients(mapToIngredientEntity(ingredientIds, recipeEntity));
        return recipeEntity;
    }

    private static List<Instruction> mapToInstructionEntity(
            List<String> instructions, AtomicInteger count, Recipe recipeEntity) {
        return instructions.stream()
                .map(instruction -> {
                    Instruction instructionEntity = new Instruction();
                    instructionEntity.setDescription(instruction);
                    instructionEntity.setStep(count.incrementAndGet());
                    instructionEntity.setRecipe(recipeEntity);
                    return instructionEntity;
                })
                .toList();
    }

    private static List<Ingredient> mapToIngredientEntity(List<String> ingredientIds, Recipe recipeEntity) {
        return ingredientIds.stream()
                .map(ingredientName -> {
                    Ingredient ingredientEntity = new Ingredient();
                    ingredientEntity.setName(ingredientName);
                    ingredientEntity.setRecipe(recipeEntity);
                    return ingredientEntity;
                })
                .toList();
    }
}
