package com.federicoinnocente.subs_tracker.controller;

import com.federicoinnocente.subs_tracker.dto.SubscriptionDTO;
import com.federicoinnocente.subs_tracker.service.SubscriptionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@AllArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping
    public List<SubscriptionDTO> getSubscriptions() {
        return subscriptionService.findAll();
    }

    @PostMapping
    public void addSubscription(@RequestBody SubscriptionDTO subscription) {
        subscriptionService.save(subscription);
    }

}
