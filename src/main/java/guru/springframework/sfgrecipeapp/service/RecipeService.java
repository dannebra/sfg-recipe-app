package guru.springframework.sfgrecipeapp.service;

import guru.springframework.sfgrecipeapp.model.Recipe;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface RecipeService {
    Set<Recipe> getRecipes();
}
