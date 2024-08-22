package com.abnamro.project.recipe.service;

import com.abnamro.project.recipe.config.ErrorMessagePropertyConfig;
import com.abnamro.project.recipe.entity.Ingredient;
import com.abnamro.project.recipe.entity.Instruction;
import com.abnamro.project.recipe.entity.Recipe;
import com.abnamro.project.recipe.exception.RecipeNotFoundException;
import com.abnamro.project.recipe.mapper.RecipeMapper;
import com.abnamro.project.recipe.model.RecipeRequest;
import com.abnamro.project.recipe.model.RecipeSearch;
import com.abnamro.project.recipe.model.create_recipe.response.CreateRecipeResponse;
import com.abnamro.project.recipe.model.get_recipe.response.RecipeResponse;
import com.abnamro.project.recipe.repository.RecipeRepository;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;
    private final ErrorMessagePropertyConfig messageProvider;
    private final InputSanitizationService inputSanitizationService;

    public List<RecipeResponse> getRecipeList(int page, int size) {
        page = page <= 1 ? 0 : page - 1;
        Pageable pageRequest = PageRequest.of(page, size);
        List<Recipe> listOfRecipes = recipeRepository.findAll(pageRequest).getContent();
        return recipeMapper.mapToRecipeResponse(listOfRecipes);
    }

    public RecipeResponse getRecipeById(Integer id) {
        return recipeRepository
                .findById(Long.valueOf(id))
                .map(recipeMapper::mapToRecipeResponse)
                .orElseThrow(this::throwRecipeNotFoundException);
    }

    public CreateRecipeResponse createRecipe(RecipeRequest request) {
        inputSanitizationService.sanitizeRecipe(request);
        Recipe recipe = recipeMapper.mapToRecipeEntity(request);
        Recipe save = recipeRepository.save(recipe);
        return CreateRecipeResponse.builder().recipeId(save.getId().toString()).build();
    }

    public void updateRecipe(Long id, RecipeRequest updatedRecipeRequest) {
        inputSanitizationService.sanitizeRecipe(updatedRecipeRequest);
        Recipe existingRecipe = recipeRepository.findById(id).orElseThrow(this::throwRecipeNotFoundException);
        existingRecipe.setName(updatedRecipeRequest.getName());

        // Convert the updated instructions and ingredients from String to Entity
        Recipe updateRecipe = recipeMapper.mapToRecipeEntity(updatedRecipeRequest);
        List<Instruction> updatedInstructions = updateRecipe.getInstructions();
        List<Ingredient> updatedIngredients = updateRecipe.getIngredients();

        // Handle Instructions
        List<Instruction> currentInstructions = existingRecipe.getInstructions();
        // Remove instructions that are not in the updated set
        currentInstructions.removeIf(instruction -> !updatedInstructions.contains(instruction));
        // Add instructions that are in the updated set but not in the current set
        for (Instruction updatedInstruction : updatedInstructions) {
            if (!currentInstructions.contains(updatedInstruction)) {
                currentInstructions.add(updatedInstruction);
                updatedInstruction.setRecipe(existingRecipe);
            }
        }

        // Handle Ingredients
        List<Ingredient> currentIngredients = existingRecipe.getIngredients();
        // Remove ingredients that are not in the updated set
        currentIngredients.removeIf(ingredient -> !updatedIngredients.contains(ingredient));
        // Add ingredients that are in the updated set but not in the current set
        for (Ingredient updatedIngredient : updatedIngredients) {
            if (!currentIngredients.contains(updatedIngredient)) {
                currentIngredients.add(updatedIngredient);
                updatedIngredient.setRecipe(existingRecipe);
            }
        }

        AtomicInteger count = new AtomicInteger(0);
        currentInstructions.forEach(i -> i.setStep(count.incrementAndGet()));
        recipeRepository.save(existingRecipe);
    }

    public void deleteRecipeById(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(this::throwRecipeNotFoundException);
        recipeRepository.delete(recipe);
    }

    public List<RecipeResponse> filterRecipe(RecipeSearch recipeSearch) {
        inputSanitizationService.sanitizeRecipe(recipeSearch);
        List<Recipe> recipesByCriteria = recipeRepository.findRecipesByCriteria(
                recipeSearch.getRecipeType(),
                recipeSearch.getServings(),
                recipeSearch.getIngredientName(),
                recipeSearch.getExcludeIngredientName(),
                recipeSearch.getInstructionText());
        return recipeMapper.mapToRecipeResponse(recipesByCriteria);
    }

    private RecipeNotFoundException throwRecipeNotFoundException() {
        return new RecipeNotFoundException(messageProvider.getMessage("recipe.notFound"));
    }
}
