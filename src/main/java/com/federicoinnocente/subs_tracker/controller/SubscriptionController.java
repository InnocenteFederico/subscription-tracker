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

    private final SubscriptionService subscriptionServiceInterface;

    @GetMapping
    public List<SubscriptionDTO> getSubscriptions() {
        return subscriptionServiceInterface.findAll();
    }

    @PostMapping
    public void addSubscription(@RequestBody SubscriptionDTO subscription) {
        subscriptionServiceInterface.save(subscription);
    }

}
