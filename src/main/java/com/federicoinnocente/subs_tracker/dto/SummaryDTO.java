package com.federicoinnocente.subs_tracker.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class SummaryDTO {

    private BigDecimal totalMonthly;
    private List<CategorySummaryDto> categories;
}
