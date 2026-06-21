package com.federicoinnocente.subs_tracker.service;

import com.federicoinnocente.subs_tracker.common.security.SessionContext;
import com.federicoinnocente.subs_tracker.dto.SummaryDTO;
import com.federicoinnocente.subs_tracker.entity.BillingCycle;
import com.federicoinnocente.subs_tracker.entity.CategoryEntity;
import com.federicoinnocente.subs_tracker.entity.SubscriptionEntity;
import com.federicoinnocente.subs_tracker.repository.SubscriptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatServiceTest {

    @Mock SessionContext sessionContext;
    @Mock SubscriptionRepository subscriptionRepository;

    @InjectMocks StatService statService;

    // --- normalizeAmount (private, tested via reflection) ---

    private BigDecimal normalize(BigDecimal amount, BillingCycle from, BillingCycle to) {
        return ReflectionTestUtils.invokeMethod(statService, "normalizeAmount", amount, from, to);
    }

    @Test
    void normalizeAmount_sameCycle_returnsUnchanged() {
        BigDecimal amount = new BigDecimal("10.00");
        assertEquals(0, normalize(amount, BillingCycle.MONTHLY, BillingCycle.MONTHLY).compareTo(amount));
    }

    @Test
    void normalizeAmount_monthlyToYearly_multipliesBy12() {
        assertEquals(0, normalize(new BigDecimal("10.00"), BillingCycle.MONTHLY, BillingCycle.YEARLY)
                .compareTo(new BigDecimal("120.00")));
    }

    @Test
    void normalizeAmount_yearlyToMonthly_dividesBy12() {
        assertEquals(0, normalize(new BigDecimal("120.00"), BillingCycle.YEARLY, BillingCycle.MONTHLY)
                .compareTo(new BigDecimal("10.00")));
    }

    @Test
    void normalizeAmount_weeklyToYearly_multipliesBy52() {
        assertEquals(0, normalize(new BigDecimal("10.00"), BillingCycle.WEEKLY, BillingCycle.YEARLY)
                .compareTo(new BigDecimal("520.00")));
    }

    @Test
    void normalizeAmount_yearlyToWeekly_dividesBy52() {
        assertEquals(0, normalize(new BigDecimal("520.00"), BillingCycle.YEARLY, BillingCycle.WEEKLY)
                .compareTo(new BigDecimal("10.00")));
    }

    // --- getSummary ---

    @Test
    void getSummary_noSubscriptions_returnsZeroTotal() {
        when(sessionContext.getEmail()).thenReturn("user@example.com");
        when(subscriptionRepository.findByUser_EmailAndActive("user@example.com", true))
                .thenReturn(List.of());

        SummaryDTO result = statService.getSummary();

        assertEquals(0, result.getTotalMonthly().compareTo(BigDecimal.ZERO));
        assertTrue(result.getCategories().isEmpty());
    }

    @Test
    void getSummary_singleMonthlySubscription_returnsCorrectTotals() {
        SubscriptionEntity sub = new SubscriptionEntity();
        sub.setName("Netflix");
        sub.setAmount(new BigDecimal("10.00"));
        sub.setBillingCycle(BillingCycle.MONTHLY);

        when(sessionContext.getEmail()).thenReturn("user@example.com");
        when(subscriptionRepository.findByUser_EmailAndActive("user@example.com", true))
                .thenReturn(List.of(sub));

        SummaryDTO result = statService.getSummary();

        assertEquals(0, result.getTotalMonthly().compareTo(new BigDecimal("10.00")));
        assertEquals(1, result.getCategories().size());
        assertEquals("Senza categoria", result.getCategories().get(0).getName());
        assertEquals(0, result.getCategories().get(0).getPercentage().compareTo(new BigDecimal("100.00")));
    }

    @Test
    void getSummary_twoSubscriptionsInDifferentCategories_calculatesPercentages() {
        CategoryEntity catA = new CategoryEntity();
        catA.setCategoryId(1L);
        catA.setName("Music");

        SubscriptionEntity sub1 = new SubscriptionEntity();
        sub1.setName("Spotify");
        sub1.setAmount(new BigDecimal("10.00"));
        sub1.setBillingCycle(BillingCycle.MONTHLY);
        sub1.setCategory(catA);

        SubscriptionEntity sub2 = new SubscriptionEntity();
        sub2.setName("Cloud");
        sub2.setAmount(new BigDecimal("120.00"));
        sub2.setBillingCycle(BillingCycle.YEARLY); // = 10.00/month

        when(sessionContext.getEmail()).thenReturn("user@example.com");
        when(subscriptionRepository.findByUser_EmailAndActive("user@example.com", true))
                .thenReturn(List.of(sub1, sub2));

        SummaryDTO result = statService.getSummary();

        assertEquals(0, result.getTotalMonthly().compareTo(new BigDecimal("20.00")));
        assertEquals(2, result.getCategories().size());
        result.getCategories().forEach(cs ->
                assertEquals(0, cs.getPercentage().compareTo(new BigDecimal("50.00"))));
    }
}
