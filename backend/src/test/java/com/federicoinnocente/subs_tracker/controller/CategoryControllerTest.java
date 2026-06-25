package com.federicoinnocente.subs_tracker.controller;

import com.federicoinnocente.subs_tracker.dto.CategoryDTO;
import com.federicoinnocente.subs_tracker.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Mock CategoryService categoryService;
    @InjectMocks CategoryController categoryController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    void getCategories_returnsOkWithList() throws Exception {
        when(categoryService.getCategories()).thenReturn(List.of(new CategoryDTO()));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void addCategory_returnsOkAndDelegatestoService() throws Exception {
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Music\",\"color\":\"#FF0000\"}"))
                .andExpect(status().isOk());

        verify(categoryService).addCategory(any());
    }

    @Test
    void updateCategory_callsServiceWithIdAndBody() throws Exception {
        mockMvc.perform(patch("/api/categories/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated\",\"color\":\"#00FF00\"}"))
                .andExpect(status().isOk());

        verify(categoryService).updateCategory(eq(5L), any());
    }

    @Test
    void deleteCategory_callsServiceWithId() throws Exception {
        mockMvc.perform(delete("/api/categories/5"))
                .andExpect(status().isOk());

        verify(categoryService).deleteCategory(5L);
    }
}
