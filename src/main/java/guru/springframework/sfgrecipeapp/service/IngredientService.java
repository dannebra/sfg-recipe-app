package guru.springframework.sfgrecipeapp.service;

import guru.springframework.sfgrecipeapp.commands.IngredientCommand;
import guru.springframework.sfgrecipeapp.model.Ingredient;

public interface IngredientService {

    IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId);

    IngredientCommand saveIngredientCommand(IngredientCommand commandToSave);

    boolean deleteById(Long recipeId, Long ingredientIdToDelete);
}
