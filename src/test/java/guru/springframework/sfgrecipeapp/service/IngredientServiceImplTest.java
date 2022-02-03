package guru.springframework.sfgrecipeapp.service;

import guru.springframework.sfgrecipeapp.commands.IngredientCommand;
import guru.springframework.sfgrecipeapp.converters.IngredientToIngredientCommand;
import guru.springframework.sfgrecipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.sfgrecipeapp.model.Ingredient;
import guru.springframework.sfgrecipeapp.model.Recipe;
import guru.springframework.sfgrecipeapp.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class IngredientServiceImplTest {
    private final IngredientToIngredientCommand ingredientToIngredientCommand;

    @Mock
    RecipeRepository recipeRepository;

    IngredientService ingredientService;

    public IngredientServiceImplTest() {
        this.ingredientToIngredientCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
    }

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        ingredientService = new IngredientServiceImpl(ingredientToIngredientCommand, recipeRepository);
    }

    @Test
    void findByRecipeIdAndIngredientId() {
    }

    @Test
    void findByRecipe() {
        // given
        Recipe recipe = new Recipe();
        recipe.setId(1L);

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(1L);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(2L);

        Ingredient ingredient3 = new Ingredient();
        ingredient3.setId(3L);

        recipe.addIngredient(ingredient1);
        recipe.addIngredient(ingredient2);
        recipe.addIngredient(ingredient3);

        Optional<Recipe> recipeOptional = Optional.of(recipe);

        // when
        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

        IngredientCommand command = ingredientService.findByRecipeIdAndIngredientId(1L, 3L);

        // then
        assertEquals(3L, command.getId());
        assertEquals(1L, command.getRecipeId());
        verify(recipeRepository).findById(anyLong());
    }
}