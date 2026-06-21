package com.federicoinnocente.subs_tracker.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CATEGORY")
@Getter
@Setter
public class CategoryEntity {

    @Id
    @Column(name = "CATEGORY_ID")
    @SequenceGenerator(name = "seq_category", sequenceName = "SEQ_CATEGORY", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_category")
    private Long categoryId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "color")
    private String color;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private AppUserEntity user;

}
