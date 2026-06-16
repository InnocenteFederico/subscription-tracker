package com.federicoinnocente.subs_tracker.service;

import com.federicoinnocente.subs_tracker.entity.Subscription;
import com.federicoinnocente.subs_tracker.mapper.SubscriptionMapper;
import com.federicoinnocente.subs_tracker.dto.SubscriptionDTO;
import com.federicoinnocente.subs_tracker.repository.SubscriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper mapper;

    @Override
    public List<SubscriptionDTO> findAll() {
        return mapper.toDtoList(subscriptionRepository.findAll());
    }

    @Override
    public void save(SubscriptionDTO subscription) {
        Subscription subscriptionEntity = mapper.toEntity(subscription);
        subscriptionRepository.save(subscriptionEntity);
    }
}
