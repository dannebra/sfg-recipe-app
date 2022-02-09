package guru.springframework.sfgrecipeapp.controllers;

import guru.springframework.sfgrecipeapp.commands.RecipeCommand;
import guru.springframework.sfgrecipeapp.service.ImageService;
import guru.springframework.sfgrecipeapp.service.RecipeService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import sun.nio.ch.IOUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@Controller
public class ImageController {

    private final RecipeService recipeService;
    private final ImageService imageService;

    public ImageController(RecipeService recipeService, ImageService imageService) {
        this.recipeService = recipeService;
        this.imageService = imageService;
    }

    @GetMapping("recipe/{id}/image")
    public String showUploadForm(@PathVariable String id, Model model) {
        model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(id)));

        return "recipe/imageuploadform";
    }

    @PostMapping("recipe/{id}/image")
    public String uploadImage(@PathVariable String id, @RequestParam("imagefile") MultipartFile file) {
        imageService.saveImageFile(Long.valueOf(id), file);

        return "redirect:/recipe/" + id + "/show";
    }

    @GetMapping("recipe/{id}/recipeimage")
    public void getImageFromDatabase(@PathVariable String id, HttpServletResponse servletResponse) throws IOException {
        RecipeCommand command = recipeService.findCommandById(Long.valueOf(id));

        Byte[] image = command.getImage();
        if (image != null) {
            byte[] byteArray = new byte[image.length];

            int i = 0;
            for (Byte wrappedByte : image) {
                byteArray[i++] = wrappedByte;
            }

            servletResponse.setContentType("image/jpeg");
            InputStream inputStream = new ByteArrayInputStream(byteArray);
            IOUtils.copy(inputStream, servletResponse.getOutputStream());
        }
    }
}
