package com.federicoinnocente.subs_tracker.scheduler;

import com.federicoinnocente.subs_tracker.entity.BillingCycle;
import com.federicoinnocente.subs_tracker.entity.SubscriptionEntity;
import com.federicoinnocente.subs_tracker.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Scheduler {

    private final SubscriptionRepository subscriptionRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() {
        updateNextBillings();
    }

    @Scheduled(cron = "0 1 0 * * *")
    public void updateNextBillings() {
        List<SubscriptionEntity> subscriptions = subscriptionRepository.findSubsForNextBillingUpdate();
        subscriptions.forEach(s -> s.setNextBilling(computeNextBillingDate(s)));
        subscriptionRepository.saveAll(subscriptions);
    }

    private LocalDate computeNextBillingDate(SubscriptionEntity subscription) {
        LocalDate today = LocalDate.now();
        LocalDate firstBilling = subscription.getFirstBilling();

        return switch (subscription.getBillingCycle()) {
            case WEEKLY -> {
                long weeks = ChronoUnit.WEEKS.between(firstBilling, today) + 1;
                yield firstBilling.plusWeeks(weeks);
            }
            case MONTHLY -> {
                long months = ChronoUnit.MONTHS.between(firstBilling, today) + 1;
                yield firstBilling.plusMonths(months);
            }
            case YEARLY -> {
                long years = ChronoUnit.YEARS.between(firstBilling, today) + 1;
                yield firstBilling.plusYears(years);
            }
        };
    }

}
