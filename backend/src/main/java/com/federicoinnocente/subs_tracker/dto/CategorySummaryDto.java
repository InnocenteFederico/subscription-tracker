package com.federicoinnocente.subs_tracker.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CategorySummaryDto {

    private String name;
    private String color;
    private BigDecimal subtotal;
    private BigDecimal percentage;
    private List<SubscriptionSummaryDto> subscriptions = new ArrayList<>();
}
