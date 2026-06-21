package com.federicoinnocente.subs_tracker.repository;

import com.federicoinnocente.subs_tracker.entity.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {

    List<SubscriptionEntity> findByUser_Email(String email);

    List<SubscriptionEntity> findByUser_EmailAndActive(String email, boolean isActive);

    @Query(value = """
        select sub from SubscriptionEntity sub
        where sub.user.email = :email
            and sub.nextBilling <= :cutoff
        """)
    List<SubscriptionEntity> findAllWithinCutoff(
            @Param("email") String email,
            @Param("cutoff") LocalDate cutoff);

    @Query(value = """
        select sub from SubscriptionEntity sub
        where sub.active = true and sub.nextBilling < CURRENT_DATE
        """)
    List<SubscriptionEntity> findSubsForNextBillingUpdate();

}
