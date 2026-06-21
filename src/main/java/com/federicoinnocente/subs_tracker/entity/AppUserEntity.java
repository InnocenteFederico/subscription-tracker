package com.federicoinnocente.subs_tracker.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "APP_USER")
public class AppUserEntity implements UserDetails {

    @Id
    @Column(name = "USER_ID")
    @SequenceGenerator(name = "seq_app_user", sequenceName = "SEQ_APP_USER", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_app_user")
    private Long userId;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "CREATION_DATE", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "PASSWORD_HASH", nullable = false)
    private String passwordHash;

    @OneToMany(mappedBy = "user")
    private List<SubscriptionEntity> subscriptions;

    @OneToMany(mappedBy = "user")
    private List<CategoryEntity> categories;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public @Nullable String getPassword() {
        return this.passwordHash;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
