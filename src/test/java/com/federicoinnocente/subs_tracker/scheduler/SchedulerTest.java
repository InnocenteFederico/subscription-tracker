package com.federicoinnocente.subs_tracker.scheduler;

import com.federicoinnocente.subs_tracker.entity.BillingCycle;
import com.federicoinnocente.subs_tracker.entity.SubscriptionEntity;
import com.federicoinnocente.subs_tracker.repository.SubscriptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchedulerTest {

    @Mock SubscriptionRepository subscriptionRepository;

    @InjectMocks Scheduler scheduler;

    // --- computeNextBillingDate (private, tested via reflection) ---

    private LocalDate computeNext(LocalDate firstBilling, BillingCycle cycle) {
        SubscriptionEntity sub = new SubscriptionEntity();
        sub.setFirstBilling(firstBilling);
        sub.setBillingCycle(cycle);
        return ReflectionTestUtils.invokeMethod(scheduler, "computeNextBillingDate", sub);
    }

    @Test
    void computeNextBillingDate_weekly_returnsOneWeekAhead_whenFirstBillingIsToday() {
        LocalDate today = LocalDate.now();
        assertEquals(today.plusWeeks(1), computeNext(today, BillingCycle.WEEKLY));
    }

    @Test
    void computeNextBillingDate_monthly_returnsOneMonthAhead_whenFirstBillingIsToday() {
        LocalDate today = LocalDate.now();
        assertEquals(today.plusMonths(1), computeNext(today, BillingCycle.MONTHLY));
    }

    @Test
    void computeNextBillingDate_yearly_returnsOneYearAhead_whenFirstBillingIsToday() {
        LocalDate today = LocalDate.now();
        assertEquals(today.plusYears(1), computeNext(today, BillingCycle.YEARLY));
    }

    @Test
    void computeNextBillingDate_alwaysReturnsDateInFuture() {
        LocalDate pastDate = LocalDate.now().minusYears(2);
        LocalDate today = LocalDate.now();

        for (BillingCycle cycle : BillingCycle.values()) {
            assertTrue(computeNext(pastDate, cycle).isAfter(today),
                    "Expected next billing to be strictly in the future for cycle " + cycle);
        }
    }

    // --- updateNextBillings ---

    @Test
    void updateNextBillings_updatesNextBillingAndSavesAll() {
        SubscriptionEntity sub = new SubscriptionEntity();
        sub.setFirstBilling(LocalDate.now());
        sub.setBillingCycle(BillingCycle.MONTHLY);

        when(subscriptionRepository.findSubsForNextBillingUpdate()).thenReturn(List.of(sub));

        scheduler.updateNextBillings();

        assertEquals(LocalDate.now().plusMonths(1), sub.getNextBilling());
        verify(subscriptionRepository).saveAll(List.of(sub));
    }
}
