package com.federicoinnocente.subs_tracker;

import com.federicoinnocente.subs_tracker.repository.AppUserRepository;
import com.federicoinnocente.subs_tracker.repository.CategoryRepository;
import com.federicoinnocente.subs_tracker.repository.SubscriptionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class SubsTrackerApplicationTests {

    @MockitoBean AppUserRepository userRepository;
    @MockitoBean SubscriptionRepository subscriptionRepository;
    @MockitoBean CategoryRepository categoryRepository;

    @Test
    void contextLoads() {
    }

}
