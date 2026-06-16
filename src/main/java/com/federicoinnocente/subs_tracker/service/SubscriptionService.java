package com.federicoinnocente.subs_tracker.service;

import com.federicoinnocente.subs_tracker.dto.SubscriptionDTO;

import java.util.List;

public interface SubscriptionService {

    List<SubscriptionDTO> findAll();

    void save(SubscriptionDTO subscription);

}
