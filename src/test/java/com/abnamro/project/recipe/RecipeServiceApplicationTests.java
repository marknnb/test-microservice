package com.abnamro.project.recipe;

import com.abnamro.project.recipe.config.TestConfigs;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestConfigs.class)
class RecipeServiceApplicationTests {

    @Test
    void contextLoads() {}
}
