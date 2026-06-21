package com.federicoinnocente.subs_tracker.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "SUBSCRIPTIONS")
@Getter
@Setter
public class SubscriptionEntity {

    @Id
    @Column(name = "SUBSCRIPTION_ID")
    @SequenceGenerator(name = "subscription_seq", sequenceName = "SEQ_SUBSCRIPTION", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subscription_seq")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "BILLING_CYCLE")
    @Enumerated(EnumType.STRING)
    private BillingCycle billingCycle;

    @Column(name = "FIRST_BILLING")
    private LocalDate firstBilling;

    @Column(name = "NEXT_BILLING")
    private LocalDate nextBilling;

    @Column(name = "IS_ACTIVE")
    private boolean active;

    @Column(name = "NOTES")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private AppUserEntity user;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    private CategoryEntity category;

}
