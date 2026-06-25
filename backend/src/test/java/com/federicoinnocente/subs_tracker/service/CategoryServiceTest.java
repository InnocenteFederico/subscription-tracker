package com.federicoinnocente.subs_tracker.service;

import com.federicoinnocente.subs_tracker.common.security.SessionContext;
import com.federicoinnocente.subs_tracker.dto.CategoryDTO;
import com.federicoinnocente.subs_tracker.entity.AppUserEntity;
import com.federicoinnocente.subs_tracker.entity.CategoryEntity;
import com.federicoinnocente.subs_tracker.mapper.CategoryMapper;
import com.federicoinnocente.subs_tracker.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock CategoryRepository categoryRepository;
    @Mock CategoryMapper mapper;
    @Mock SessionContext sessionContext;

    @InjectMocks CategoryService categoryService;

    @Test
    void addCategory_savesEntityWithCurrentUser() {
        CategoryDTO dto = new CategoryDTO();
        CategoryEntity entity = new CategoryEntity();
        AppUserEntity user = new AppUserEntity();

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(sessionContext.getUser()).thenReturn(user);

        categoryService.addCategory(dto);

        assertEquals(user, entity.getUser());
        verify(categoryRepository).save(entity);
    }

    @Test
    void updateCategory_updatesFieldsAndSaves() {
        AppUserEntity owner = new AppUserEntity();
        owner.setUserId(1L);

        CategoryEntity entity = new CategoryEntity();
        entity.setUser(owner);

        CategoryDTO dto = new CategoryDTO();
        dto.setName("Updated");
        dto.setColor("#00FF00");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(sessionContext.getUser()).thenReturn(owner);

        categoryService.updateCategory(1L, dto);

        assertEquals("Updated", entity.getName());
        assertEquals("#00FF00", entity.getColor());
        verify(categoryRepository).save(entity);
    }

    @Test
    void updateCategory_throwsWhenNotFound() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> categoryService.updateCategory(99L, new CategoryDTO()));
    }

    @Test
    void updateCategory_throwsWhenUserDoesNotOwn() {
        AppUserEntity owner = new AppUserEntity();
        owner.setUserId(1L);

        AppUserEntity other = new AppUserEntity();
        other.setUserId(2L);

        CategoryEntity entity = new CategoryEntity();
        entity.setUser(owner);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(sessionContext.getUser()).thenReturn(other);

        assertThrows(RuntimeException.class, () -> categoryService.updateCategory(1L, new CategoryDTO()));
    }

    @Test
    void deleteCategory_deletesEntity() {
        AppUserEntity owner = new AppUserEntity();
        owner.setUserId(1L);

        CategoryEntity entity = new CategoryEntity();
        entity.setUser(owner);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(sessionContext.getUser()).thenReturn(owner);

        categoryService.deleteCategory(1L);

        verify(categoryRepository).delete(entity);
    }

    @Test
    void deleteCategory_throwsWhenNotFound() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> categoryService.deleteCategory(99L));
    }

    @Test
    void deleteCategory_throwsWhenUserDoesNotOwn() {
        AppUserEntity owner = new AppUserEntity();
        owner.setUserId(1L);

        AppUserEntity other = new AppUserEntity();
        other.setUserId(2L);

        CategoryEntity entity = new CategoryEntity();
        entity.setUser(owner);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(sessionContext.getUser()).thenReturn(other);

        assertThrows(RuntimeException.class, () -> categoryService.deleteCategory(1L));
    }

    @Test
    void getCategories_returnsMappedList() {
        Long userId = 123L;
        AppUserEntity user = new AppUserEntity();
        user.setUserId(userId);

        List<CategoryEntity> entities = List.of(new CategoryEntity());
        List<CategoryDTO> dtos = List.of(new CategoryDTO());

        when(sessionContext.getUser()).thenReturn(user);
        when(categoryRepository.findByUser_UserId(userId)).thenReturn(entities);
        when(mapper.toDtoList(entities)).thenReturn(dtos);

        List<CategoryDTO> result = categoryService.getCategories();

        assertEquals(1, result.size());
    }
}
