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

import static org.junit.jupiter.api.Assertions.assertEquals;
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
