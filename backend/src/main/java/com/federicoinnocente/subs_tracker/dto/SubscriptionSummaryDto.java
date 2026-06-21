package com.federicoinnocente.subs_tracker.dto;

import com.federicoinnocente.subs_tracker.entity.BillingCycle;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SubscriptionSummaryDto {

    private String name;
    private BigDecimal amount;
    private BillingCycle billingCycle;
    private BigDecimal monthlyAmount;

}
