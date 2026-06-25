package com.federicoinnocente.subs_tracker.controller;

import com.federicoinnocente.subs_tracker.service.SubscriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SubscriptionControllerTest {

    @Mock SubscriptionService subscriptionService;
    @InjectMocks SubscriptionController subscriptionController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(subscriptionController).build();
    }

    @Test
    void getSubscriptions_returnsOk() throws Exception {
        when(subscriptionService.getSubscriptions(null)).thenReturn(List.of());

        mockMvc.perform(get("/api/subscriptions"))
                .andExpect(status().isOk());

        verify(subscriptionService).getSubscriptions(null);
    }

    @Test
    void getSubscriptions_withActiveTrue_passesFilterToService() throws Exception {
        when(subscriptionService.getSubscriptions(true)).thenReturn(List.of());

        mockMvc.perform(get("/api/subscriptions").param("active", "true"))
                .andExpect(status().isOk());

        verify(subscriptionService).getSubscriptions(true);
    }

    @Test
    void getSubscriptions_withActiveFalse_passesFilterToService() throws Exception {
        when(subscriptionService.getSubscriptions(false)).thenReturn(List.of());

        mockMvc.perform(get("/api/subscriptions").param("active", "false"))
                .andExpect(status().isOk());

        verify(subscriptionService).getSubscriptions(false);
    }

    @Test
    void addSubscription_callsSaveAndReturnsUpdatedList() throws Exception {
        when(subscriptionService.getSubscriptions(true)).thenReturn(List.of());

        mockMvc.perform(post("/api/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Netflix\",\"billingCycle\":\"MONTHLY\"}"))
                .andExpect(status().isOk());

        verify(subscriptionService).saveSubscription(any());
    }

    @Test
    void updateSubscription_callsServiceWithIdAndBodyAndReturnsUpdatedList() throws Exception {
        when(subscriptionService.getSubscriptions(true)).thenReturn(List.of());

        mockMvc.perform(patch("/api/subscriptions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"active\":false}"))
                .andExpect(status().isOk());

        verify(subscriptionService).updateSubscription(eq(1L), any());
    }

    @Test
    void getUpcomingSubscriptions_usesDefaultDaysParam() throws Exception {
        when(subscriptionService.getUpcomingSubscriptions(30)).thenReturn(List.of());

        mockMvc.perform(get("/api/subscriptions/upcoming"))
                .andExpect(status().isOk());

        verify(subscriptionService).getUpcomingSubscriptions(30);
    }
}
