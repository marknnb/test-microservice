package com.abnamro.project.recipe.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

public class RecipeControllerTests extends AbstractIT {

    @Test
    void shouldReturnRecipe() {
        given().contentType(ContentType.JSON)
                .when()
                .get("/api/v1/recipe/page/1/size/2")
                .then()
                .statusCode(200)
                .body("size()", is(2));
    }

    @Test
    void shouldReturnRecipeById() {
        given().contentType(ContentType.JSON)
                .when()
                .get("/api/v1/recipe/1")
                .then()
                .statusCode(200)
                .body("recipeId", is("1"));
    }

    @Test
    void shouldReturnNotFoundWhenRecipeNotExists() {
        given().contentType(ContentType.JSON)
                .when()
                .get("/api/v1/recipe/112333")
                .then()
                .statusCode(404);
    }
}
