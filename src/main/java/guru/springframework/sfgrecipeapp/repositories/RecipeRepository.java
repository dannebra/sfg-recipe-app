package guru.springframework.sfgrecipeapp.repositories;

import guru.springframework.sfgrecipeapp.model.Recipe;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Long> {
}
