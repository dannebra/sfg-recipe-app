package guru.springframework.sfgrecipeapp.service;

import guru.springframework.sfgrecipeapp.commands.IngredientCommand;
import guru.springframework.sfgrecipeapp.converters.IngredientCommandToIngredient;
import guru.springframework.sfgrecipeapp.converters.IngredientToIngredientCommand;
import guru.springframework.sfgrecipeapp.model.Ingredient;
import guru.springframework.sfgrecipeapp.model.Recipe;
import guru.springframework.sfgrecipeapp.repositories.RecipeRepository;
import guru.springframework.sfgrecipeapp.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand,
                                 IngredientCommandToIngredient ingredientCommandToIngredient,
                                 RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if (!recipeOptional.isPresent()) {
            log.error("Recipe id not found: " + recipeId);
        }

        Recipe recipe = recipeOptional.get();

        Optional<IngredientCommand> ingredientCommandOptional = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map(ingredientToIngredientCommand::convert).findFirst();

        if (!ingredientCommandOptional.isPresent()) {
            log.error("Ingredient id not found: " + ingredientId);
        }

        return ingredientCommandOptional.get();
    }

    @Override
    @Transactional
    public IngredientCommand saveIngredientCommand(IngredientCommand commandToSave) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(commandToSave.getRecipeId());

        if (!recipeOptional.isPresent()) {
            log.error("Recipe not found, id: " + commandToSave.getRecipeId());
            return new IngredientCommand();
        } else {
            Recipe recipe = recipeOptional.get();

            Optional<Ingredient> ingredientOptional = recipe.getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(commandToSave.getId()))
                    .findFirst();

            if (ingredientOptional.isPresent()) {
                Ingredient foundIngredient = ingredientOptional.get();
                foundIngredient.setDescription(commandToSave.getDescription());
                foundIngredient.setAmount(commandToSave.getAmount());
                foundIngredient.setUom(unitOfMeasureRepository.findById(commandToSave.getUom().getId())
                .orElseThrow(() -> new RuntimeException("UOM NOT FOUND")));
            } else {
                Ingredient ingredient = ingredientCommandToIngredient.convert(commandToSave);
                ingredient.setRecipe(recipe);
                recipe.addIngredient(ingredient);
            }

            Recipe savedRecipe = recipeRepository.save(recipe);

            Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
                    .filter(recipeIngredients -> recipeIngredients.getId().equals(commandToSave.getId()))
                    .findFirst();

            if (!savedIngredientOptional.isPresent()) {
                savedIngredientOptional = savedRecipe.getIngredients().stream()
                        .filter(recipeIngredients -> recipeIngredients.getDescription().equals(commandToSave.getDescription()))
                        .filter(recipeIngredients -> recipeIngredients.getAmount().equals(commandToSave.getAmount()))
                        .filter(recipeIngredients -> recipeIngredients.getUom().getId().equals(commandToSave.getUom().getId()))
                        .findFirst();
            }

            return ingredientToIngredientCommand.convert(savedIngredientOptional.get());
        }
    }
}
