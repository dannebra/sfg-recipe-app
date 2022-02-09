package guru.springframework.sfgrecipeapp.controllers;

import guru.springframework.sfgrecipeapp.commands.RecipeCommand;
import guru.springframework.sfgrecipeapp.service.ImageService;
import guru.springframework.sfgrecipeapp.service.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ImageControllerTest {

    @Mock
    ImageService imageService;

    @Mock
    RecipeService recipeService;

    ImageController imageController;

    MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        openMocks(this);
        imageController = new ImageController(recipeService, imageService);

        mockMvc = MockMvcBuilders.standaloneSetup(imageController).build();
    }

    @Test
    void showUploadForm() throws Exception {
        RecipeCommand recipeCommand = new RecipeCommand();

        when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);

        mockMvc.perform(get("/recipe/1/image"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(view().name("recipe/imageuploadform"));

        verify(recipeService).findCommandById(anyLong());
    }

    @Test
    void uploadImage() throws Exception {
        MockMultipartFile mockMultipartFile =
                new MockMultipartFile(
                        "imagefile",
                        "test.txt",
                        "text/plain",
                        "My Image Test".getBytes());

        mockMvc.perform(multipart("/recipe/1/image").file(mockMultipartFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("location", "/recipe/1/show"));

        verify(imageService).saveImageFile(anyLong(), any());
    }

    @Test
    public void renderImageFromDatabase() throws Exception {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);

        String text = "Test text";
        Byte[] image = new Byte[text.getBytes().length];

        Arrays.setAll(image, n -> text.getBytes()[n]);

        recipeCommand.setImage(image);

        when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);

        MockHttpServletResponse response = mockMvc.perform(get("/recipe/1/recipeimage"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        byte[] responseBytes = response.getContentAsByteArray();

        assertEquals(text.getBytes().length, responseBytes.length);
    }
}