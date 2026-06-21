package com.federicoinnocente.subs_tracker.service;

import com.federicoinnocente.subs_tracker.common.security.SessionContext;
import com.federicoinnocente.subs_tracker.dto.CategoryDTO;
import com.federicoinnocente.subs_tracker.entity.AppUserEntity;
import com.federicoinnocente.subs_tracker.entity.CategoryEntity;
import com.federicoinnocente.subs_tracker.mapper.CategoryMapper;
import com.federicoinnocente.subs_tracker.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper mapper;
    private final SessionContext sessionContext;

    public List<CategoryDTO> getCategories() {
        AppUserEntity user = sessionContext.getUser();
        List<CategoryEntity> categories = categoryRepository.findByUser_UserId(user.getUserId());
        return mapper.toDtoList(categories);
    }

    public void addCategory(CategoryDTO category) {
        CategoryEntity categoryEntity = mapper.toEntity(category);
        categoryEntity.setUser(sessionContext.getUser());
        categoryRepository.save(categoryEntity);
    }

}
