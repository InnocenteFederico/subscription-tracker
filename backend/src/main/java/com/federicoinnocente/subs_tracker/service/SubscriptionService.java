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

    public List<SubscriptionDTO> getSubscriptions(Boolean active) {
        if (active != null) {
            return mapper.toDtoList(subscriptionRepository.findByUser_EmailAndActive(sessionContext.getEmail(), active));
        }
        return mapper.toDtoList(subscriptionRepository.findByUser_Email(sessionContext.getEmail()));
    }

    public void saveSubscription(SubscriptionDTO subscription) {
        SubscriptionEntity subscriptionEntity = mapper.toEntity(subscription);
        subscriptionEntity.setUser(sessionContext.getUser());
        subscriptionEntity.setActive(true);
        if (subscription.getNextBilling() == null) {
            subscriptionEntity.setNextBilling(subscription.getFirstBilling());
        }
        if (subscription.getCategoryId() != null) {
            subscriptionEntity.setCategory(categoryRepository.getReferenceById(subscription.getCategoryId()));
        }
        subscriptionRepository.save(subscriptionEntity);
    }

    public void updateSubscription(Long id, SubscriptionDTO dto) {
        SubscriptionEntity subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription " + id + " non trovata"));
        if (!subscription.getUser().getEmail().equals(sessionContext.getEmail())) {
            throw new RuntimeException("Non autorizzato");
        }
        if (!dto.isActive()) {
            subscription.setActive(false);
            subscription.setNextBilling(null);
        } else {
            subscription.setName(dto.getName());
            subscription.setDescription(dto.getDescription());
            subscription.setAmount(dto.getAmount());
            subscription.setCurrency(dto.getCurrency());
            subscription.setBillingCycle(dto.getBillingCycle());
            subscription.setFirstBilling(dto.getFirstBilling());
            subscription.setNextBilling(dto.getNextBilling());
            subscription.setNotes(dto.getNotes());
            subscription.setActive(true);
            if (dto.getCategoryId() != null) {
                subscription.setCategory(categoryRepository.getReferenceById(dto.getCategoryId()));
            } else {
                subscription.setCategory(null);
            }
        }
        subscriptionRepository.save(subscription);
    }

    public List<SubscriptionDTO> getUpcomingSubscriptions(int days) {
        LocalDate cutoff = LocalDate.now().plusDays(days);
        return mapper.toDtoList(subscriptionRepository.findAllWithinCutoff(sessionContext.getEmail(), cutoff));
    }

}
