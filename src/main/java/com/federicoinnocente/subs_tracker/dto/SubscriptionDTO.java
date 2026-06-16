package com.federicoinnocente.subs_tracker.dto;

import com.federicoinnocente.subs_tracker.entity.BillingCycle;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class SubscriptionDTO {

    private Long id;
    private Long userId;
    private String name;
    private String description;
    private BigDecimal amount;
    private String currency;
    private BillingCycle billingCycle;
    private LocalDate firstBilling;
    private LocalDate nextBilling;
    private boolean isActive;
    private String notes;
}
