package com.federicoinnocente.subs_tracker.controller;

import com.federicoinnocente.subs_tracker.dto.CategoryDTO;
import com.federicoinnocente.subs_tracker.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDTO> getCategories() {
        return categoryService.getCategories();
    }

    @PostMapping
    public List<CategoryDTO> addCategory(@RequestBody CategoryDTO category) {
        categoryService.addCategory(category);
        return categoryService.getCategories();
    }

    @PatchMapping("/{id}")
    public List<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO category) {
        categoryService.updateCategory(id, category);
        return categoryService.getCategories();
    }

    @DeleteMapping("/{id}")
    public List<CategoryDTO> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return categoryService.getCategories();
    }

}
