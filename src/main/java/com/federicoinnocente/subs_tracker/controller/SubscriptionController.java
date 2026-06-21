package com.federicoinnocente.subs_tracker.controller;

import com.federicoinnocente.subs_tracker.dto.SubscriptionDTO;
import com.federicoinnocente.subs_tracker.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping
    public List<SubscriptionDTO> getSubscriptions() {
        return subscriptionService.getSubscriptions();
    }

    @PostMapping
    public List<SubscriptionDTO> addSubscription(@RequestBody SubscriptionDTO subscription) {
        subscriptionService.saveSubscription(subscription);
        return subscriptionService.getSubscriptions();
    }

    @PatchMapping("/{id}")
    public List<SubscriptionDTO> deactivateSubscription(@PathVariable Long id) {
        subscriptionService.deactivateSubscription(id);
        return subscriptionService.getSubscriptions();
    }

    @GetMapping("/upcoming")
    public List<SubscriptionDTO> getUpcomingSubscriptions(@RequestParam(defaultValue = "30") int days) {
        return subscriptionService.getUpcomingSubscriptions(days);
    }


}
