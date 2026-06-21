package com.federicoinnocente.subs_tracker.repository;

import com.federicoinnocente.subs_tracker.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    List<CategoryEntity> findByUser_UserId(Long userId);
}
