package guru.springframework.sfgrecipeapp.service;

import guru.springframework.sfgrecipeapp.commands.RecipeCommand;
import guru.springframework.sfgrecipeapp.converters.RecipeCommandToRecipe;
import guru.springframework.sfgrecipeapp.converters.RecipeToRecipeCommand;
import guru.springframework.sfgrecipeapp.exceptions.NotFoundException;
import guru.springframework.sfgrecipeapp.model.Recipe;
import guru.springframework.sfgrecipeapp.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    public RecipeServiceImpl(RecipeRepository recipeRepository,
                             RecipeCommandToRecipe recipeCommandToRecipe,
                             RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeRepository = recipeRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

    @Override
    public Set<Recipe> getRecipes() {
        log.debug("Added by @Slf4j annotation");

        HashSet<Recipe> recipes = new HashSet<>();
        recipeRepository.findAll().iterator().forEachRemaining(recipes::add);
        return recipes;
    }

    @Override
    public Recipe findById(Long id)
    {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);

        if (!recipeOptional.isPresent()) {
            throw new NotFoundException("Recipe Not Found");
        }

        return recipeOptional.get();
    }

    @Override
    @Transactional
    public RecipeCommand saveRecipeCommand(RecipeCommand command) {
        Recipe detachedRecipe = recipeCommandToRecipe.convert(command);
        if (detachedRecipe == null) {
            throw new IllegalArgumentException("Recipe can not be null.");
        }

        Recipe savedRecipe = recipeRepository.save(detachedRecipe);
        log.info("Saved recipe, id: :" + savedRecipe.getId());
        return recipeToRecipeCommand.convert(savedRecipe);
    }

    @Override
    @Transactional // transactional due to conversion outside scope, any lazy loaded loaded properties would not work
    public RecipeCommand findCommandById(Long id) {
        return recipeToRecipeCommand.convert(findById(id));
    }

    @Override
    public void deleteById(Long id) {
        recipeRepository.deleteById(id);
    }


}
