package com.abnamro.project.recipe.controller;

import com.abnamro.project.recipe.model.RecipeRequest;
import com.abnamro.project.recipe.model.RecipeSearch;
import com.abnamro.project.recipe.model.create_recipe.response.CreateRecipeResponse;
import com.abnamro.project.recipe.model.get_recipe.response.RecipeResponse;
import com.abnamro.project.recipe.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import java.io.FileNotFoundException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/recipe")
@Slf4j
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Operation(description = "List all recipes")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Successful request"),
            })
    @GetMapping(path = "/page/{page}/size/{size}")
    public List<RecipeResponse> getRecipeList(
            @PathVariable(name = "page") int page, @PathVariable(name = "size") int size) {
        log.info("Getting the recipes");
        return recipeService.getRecipeList(page, size);
    }

    @Operation(description = "List one recipe by its ID")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Successful request"),
                @ApiResponse(responseCode = "404", description = "Recipe not found by the given ID")
            })
    @GetMapping(value = "/{id}")
    public RecipeResponse getRecipe(
            @Parameter(description = "Recipe ID", required = true) @PathVariable(name = "id") Integer id) {
        log.info("Getting the recipe by its id. Id: {}", id);
        return recipeService.getRecipeById(id);
    }

    @PostMapping
    @Operation(description = "Create a recipe")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "201", description = "Recipe created"),
                @ApiResponse(responseCode = "400", description = "Bad input")
            })
    @ResponseStatus(HttpStatus.CREATED)
    public CreateRecipeResponse createRecipe(
            @Parameter(description = "Properties of the recipe", required = true) @Valid @RequestBody
                    RecipeRequest request) {
        log.info("Creating the recipe with properties");
        return recipeService.createRecipe(request);
    }

    @PutMapping(value = "/{id}")
    public void updateRecipe(@PathVariable("id") Long recipeId, @RequestBody RecipeRequest request) {
        log.info("Updating the recipe by given properties");
        recipeService.updateRecipe(recipeId, request);
    }

    @Operation(description = "Delete the recipe")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Successful operation"),
                @ApiResponse(responseCode = "400", description = "Invalid input"),
                @ApiResponse(responseCode = "404", description = "Recipe not found by the given ID")
            })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteRecipeById(@PathVariable("id") Long recipeId) {
        recipeService.deleteRecipeById(recipeId);
        return ResponseEntity.noContent().build();
    }

    @Operation(description = "Search recipes by given parameters")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Successful request"),
                @ApiResponse(
                        responseCode = "404",
                        description = "Different error messages related to criteria and recipe")
            })
    @PostMapping(value = "/search")
    public List<RecipeResponse> filterRecipe(@RequestBody RecipeSearch recipeSearch) {
        log.info("Updating the recipe by given properties");
        return recipeService.filterRecipe(recipeSearch);
    }
}
