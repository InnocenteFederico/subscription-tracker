package com.federicoinnocente.subs_tracker.repository;

import com.federicoinnocente.subs_tracker.entity.AppUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUserEntity, Long> {

    Optional<AppUserEntity> findByEmail(String email);

    boolean existsByEmail(String email);

}
