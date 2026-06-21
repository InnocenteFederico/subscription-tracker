package com.federicoinnocente.subs_tracker.service;

import com.federicoinnocente.subs_tracker.common.security.SessionContext;
import com.federicoinnocente.subs_tracker.dto.CategorySummaryDto;
import com.federicoinnocente.subs_tracker.dto.SubscriptionSummaryDto;
import com.federicoinnocente.subs_tracker.dto.SummaryDTO;
import com.federicoinnocente.subs_tracker.entity.BillingCycle;
import com.federicoinnocente.subs_tracker.entity.SubscriptionEntity;
import com.federicoinnocente.subs_tracker.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatService {

    private final SessionContext sessionContext;
    private final SubscriptionRepository subscriptionRepository;

    public SummaryDTO getSummary() {
        List<SubscriptionEntity> activeSubscriptions = subscriptionRepository.findByUser_EmailAndActive(sessionContext.getEmail(), true);

        SummaryDTO summary = new SummaryDTO();
        summary.setTotalMonthly(BigDecimal.ZERO);

        Map<Long, CategorySummaryDto> mapIdToCategory = new HashMap<>();
        for (SubscriptionEntity subscription : activeSubscriptions) {
            Long categoryKey = subscription.getCategory() != null
                    ? subscription.getCategory().getCategoryId()
                    : null;

            if (!mapIdToCategory.containsKey(categoryKey)) {
                CategorySummaryDto categorySummary = new CategorySummaryDto();
                if (categoryKey != null) {
                    categorySummary.setName(subscription.getCategory().getName());
                    categorySummary.setColor(subscription.getCategory().getColor());
                } else {
                    categorySummary.setName("Senza categoria");
                }
                categorySummary.setSubtotal(BigDecimal.ZERO);
                mapIdToCategory.put(categoryKey, categorySummary);
            }

            CategorySummaryDto categorySummary = mapIdToCategory.get(categoryKey);

            BigDecimal monthlyAmount = normalizeAmount(
                    subscription.getAmount(),
                    subscription.getBillingCycle(),
                    BillingCycle.MONTHLY);

            SubscriptionSummaryDto subscriptionSummary = new SubscriptionSummaryDto();
            subscriptionSummary.setName(subscription.getName());
            subscriptionSummary.setAmount(subscription.getAmount());
            subscriptionSummary.setBillingCycle(subscription.getBillingCycle());
            subscriptionSummary.setMonthlyAmount(monthlyAmount);

            categorySummary.getSubscriptions().add(subscriptionSummary);
            categorySummary.setSubtotal(categorySummary.getSubtotal().add(monthlyAmount));

            summary.setTotalMonthly(summary.getTotalMonthly().add(monthlyAmount));
        }

        summary.setCategories(new ArrayList<>(mapIdToCategory.values()));

        summary.getCategories().forEach(cs ->
                cs.setPercentage(cs.getSubtotal()
                        .divide(summary.getTotalMonthly(), 10, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP)));

        return summary;
    }

    private BigDecimal normalizeAmount(BigDecimal amount, BillingCycle from, BillingCycle target) {
        if (from == target) return amount;

        BigDecimal annual = switch (from) {
            case WEEKLY  -> amount.multiply(BigDecimal.valueOf(52));
            case MONTHLY -> amount.multiply(BigDecimal.valueOf(12));
            case YEARLY  -> amount;
        };

        return switch (target) {
            case WEEKLY  -> annual.divide(BigDecimal.valueOf(52), 10, RoundingMode.HALF_UP);
            case MONTHLY -> annual.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
            case YEARLY  -> annual;
        };
    }
}
