package com.federicoinnocente.subs_tracker.service;

import com.federicoinnocente.subs_tracker.common.security.SessionContext;
import com.federicoinnocente.subs_tracker.dto.SubscriptionDTO;
import com.federicoinnocente.subs_tracker.entity.CategoryEntity;
import com.federicoinnocente.subs_tracker.entity.SubscriptionEntity;
import com.federicoinnocente.subs_tracker.mapper.SubscriptionMapper;
import com.federicoinnocente.subs_tracker.repository.CategoryRepository;
import com.federicoinnocente.subs_tracker.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final CategoryRepository categoryRepository;
    private final SubscriptionMapper mapper;
    private final SessionContext sessionContext;

    public List<SubscriptionDTO> getSubscriptions() {
        return mapper.toDtoList(subscriptionRepository.findByUser_Email(sessionContext.getEmail()));
    }

    public void saveSubscription(SubscriptionDTO subscription) {
        SubscriptionEntity subscriptionEntity = mapper.toEntity(subscription);
        subscriptionEntity.setUser(sessionContext.getUser());
        if (subscription.getCategoryId() != null) {
            subscriptionEntity.setCategory(categoryRepository.getReferenceById(subscription.getCategoryId()));
        }
        subscriptionRepository.save(subscriptionEntity);
    }

    public void deactivateSubscription(Long subscriptionId) {
        SubscriptionEntity subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription " + subscriptionId + " non trovata"));
        if (subscription.isActive() && subscription.getUser().getEmail().equals(sessionContext.getEmail())) {
            subscription.setActive(false);
            subscription.setNextBilling(null);
            subscriptionRepository.save(subscription);
        }
    }

    public List<SubscriptionDTO> getUpcomingSubscriptions(int days) {
        LocalDate cutoff = LocalDate.now().plusDays(days);
        return mapper.toDtoList(subscriptionRepository.findAllWithinCutoff(sessionContext.getEmail(), cutoff));
    }

}
