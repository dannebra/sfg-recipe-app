package guru.springframework.sfgrecipeapp.service;

import guru.springframework.sfgrecipeapp.commands.IngredientCommand;

public interface IngredientService {

    IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId);

    IngredientCommand saveIngredientCommand(IngredientCommand commandToSave);

    boolean deleteById(Long recipeId, Long ingredientIdToDelete);
}
