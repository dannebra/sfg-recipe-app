package guru.springframework.sfgrecipeapp.service;

import guru.springframework.sfgrecipeapp.model.Recipe;
import guru.springframework.sfgrecipeapp.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    RecipeRepository recipeRepository;

    public ImageServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    @Transactional
    public void saveImageFile(Long id, MultipartFile file) {
        try {
            Optional<Recipe> recipeOptional = recipeRepository.findById(id);
            if (recipeOptional.isPresent()) {
                Recipe recipe = recipeOptional.get();

                byte[] bytes = file.getBytes();
                Byte[] image = new Byte[bytes.length];
                Arrays.setAll(image, n -> bytes[n]);

                recipe.setImage(image);
                recipeRepository.save(recipe);
            }
        } catch (IOException e) {
            log.error("Error trying to read bytes!", e);
            e.printStackTrace();
        }
    }
}
