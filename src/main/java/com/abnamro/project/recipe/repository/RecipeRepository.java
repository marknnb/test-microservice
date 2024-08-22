package com.abnamro.project.recipe.repository;

import com.abnamro.project.recipe.entity.Recipe;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecipeRepository extends JpaRepository<Recipe, Long>, JpaSpecificationExecutor<Recipe> {
    @Query(
            value = "SELECT DISTINCT r.*\n" + "FROM Recipe r\n"
                    + "WHERE "
                    + "(:recipeType IS NULL OR r.recipe_type = :recipeType)\n"
                    + "  AND (:servings IS NULL OR r.servings = :servings)\n"
                    + "  AND (\n"
                    + "        :includeIngredient IS NULL\n"
                    + "        OR EXISTS ("
                    + "SELECT 1\n"
                    + "                   FROM Ingredient i\n"
                    + "                   WHERE i.recipe_id = r.id\n"
                    + "                     AND i.name LIKE CONCAT('%', :includeIngredient, '%')"
                    + ")\n"
                    + "    )\n"
                    + "  AND (\n"
                    + "        :excludeIngredient is NULL\n"
                    + "        OR NOT EXISTS ("
                    + "SELECT 1\n"
                    + "                       FROM Ingredient i\n"
                    + "                       WHERE i.recipe_id = r.id\n"
                    + "                         AND i.name LIKE CONCAT('%', :excludeIngredient, '%'))\n"
                    + "    )"
                    + "\n"
                    + "  AND (\n"
                    + "        :instructionText IS NULL\n"
                    + "        OR EXISTS ("
                    + "SELECT 1\n"
                    + "                   FROM Instruction instr\n"
                    + "                   WHERE instr.recipe_id = r.id\n"
                    + "                     AND instr.description LIKE CONCAT('%', :instructionText, '%')"
                    + ")\n"
                    + "    );",
            nativeQuery = true)
    List<Recipe> findRecipesByCriteria(
            @Param("recipeType") String recipeType,
            @Param("servings") Integer servings,
            @Param("includeIngredient") String includeIngredient,
            @Param("excludeIngredient") String excludeIngredientName,
            @Param("instructionText") String instructionText);
}
