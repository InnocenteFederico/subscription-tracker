package com.federicoinnocente.subs_tracker.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "SUBSCRIPTION")
@Getter
@Setter
public class Subscription {

    @Id
    @Column(name = "SUBSCRIPTION_ID")
    @SequenceGenerator(name = "subscription_seq", sequenceName = "SEQ_SUBSCRIPTION", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subscription_seq")
    private Long id;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "CATEGORY_ID")
    private Long categoryId;

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
    private boolean isActive;

    @Column(name = "NOTES")
    private String notes;


}
