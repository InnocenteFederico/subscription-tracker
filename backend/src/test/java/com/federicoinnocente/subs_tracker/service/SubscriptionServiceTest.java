package com.federicoinnocente.subs_tracker.service;

import com.federicoinnocente.subs_tracker.common.security.SessionContext;
import com.federicoinnocente.subs_tracker.dto.SubscriptionDTO;
import com.federicoinnocente.subs_tracker.entity.AppUserEntity;
import com.federicoinnocente.subs_tracker.entity.CategoryEntity;
import com.federicoinnocente.subs_tracker.entity.SubscriptionEntity;
import com.federicoinnocente.subs_tracker.mapper.SubscriptionMapper;
import com.federicoinnocente.subs_tracker.repository.CategoryRepository;
import com.federicoinnocente.subs_tracker.repository.SubscriptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock SubscriptionRepository subscriptionRepository;
    @Mock CategoryRepository categoryRepository;
    @Mock SubscriptionMapper mapper;
    @Mock SessionContext sessionContext;

    @InjectMocks SubscriptionService subscriptionService;

    @Test
    void getSubscriptions_withNoFilter_returnsAllSubscriptions() {
        List<SubscriptionEntity> entities = List.of(new SubscriptionEntity());
        List<SubscriptionDTO> dtos = List.of(new SubscriptionDTO());

        when(sessionContext.getEmail()).thenReturn("user@example.com");
        when(subscriptionRepository.findByUser_Email("user@example.com")).thenReturn(entities);
        when(mapper.toDtoList(entities)).thenReturn(dtos);

        List<SubscriptionDTO> result = subscriptionService.getSubscriptions(null);

        assertEquals(1, result.size());
        verify(subscriptionRepository).findByUser_Email("user@example.com");
        verify(subscriptionRepository, never()).findByUser_EmailAndActive(any(), anyBoolean());
    }

    @Test
    void getSubscriptions_withActiveTrue_returnsOnlyActiveSubscriptions() {
        List<SubscriptionEntity> entities = List.of(new SubscriptionEntity());
        List<SubscriptionDTO> dtos = List.of(new SubscriptionDTO());

        when(sessionContext.getEmail()).thenReturn("user@example.com");
        when(subscriptionRepository.findByUser_EmailAndActive("user@example.com", true)).thenReturn(entities);
        when(mapper.toDtoList(entities)).thenReturn(dtos);

        List<SubscriptionDTO> result = subscriptionService.getSubscriptions(true);

        assertEquals(1, result.size());
        verify(subscriptionRepository).findByUser_EmailAndActive("user@example.com", true);
        verify(subscriptionRepository, never()).findByUser_Email(any());
    }

    @Test
    void getSubscriptions_withActiveFalse_returnsOnlyInactiveSubscriptions() {
        List<SubscriptionEntity> entities = List.of(new SubscriptionEntity());
        List<SubscriptionDTO> dtos = List.of(new SubscriptionDTO());

        when(sessionContext.getEmail()).thenReturn("user@example.com");
        when(subscriptionRepository.findByUser_EmailAndActive("user@example.com", false)).thenReturn(entities);
        when(mapper.toDtoList(entities)).thenReturn(dtos);

        List<SubscriptionDTO> result = subscriptionService.getSubscriptions(false);

        assertEquals(1, result.size());
        verify(subscriptionRepository).findByUser_EmailAndActive("user@example.com", false);
        verify(subscriptionRepository, never()).findByUser_Email(any());
    }

    @Test
    void saveSubscription_withoutCategory_savesWithUser() {
        SubscriptionDTO dto = new SubscriptionDTO();
        SubscriptionEntity entity = new SubscriptionEntity();
        AppUserEntity user = new AppUserEntity();

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(sessionContext.getUser()).thenReturn(user);

        subscriptionService.saveSubscription(dto);

        assertEquals(user, entity.getUser());
        verify(categoryRepository, never()).getReferenceById(any());
        verify(subscriptionRepository).save(entity);
    }

    @Test
    void saveSubscription_withCategory_setsCategory() {
        SubscriptionDTO dto = new SubscriptionDTO();
        dto.setCategoryId(5L);

        SubscriptionEntity entity = new SubscriptionEntity();
        AppUserEntity user = new AppUserEntity();
        CategoryEntity category = new CategoryEntity();

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(sessionContext.getUser()).thenReturn(user);
        when(categoryRepository.getReferenceById(5L)).thenReturn(category);

        subscriptionService.saveSubscription(dto);

        assertEquals(category, entity.getCategory());
        verify(subscriptionRepository).save(entity);
    }

    @Test
    void updateSubscription_throwsWhenNotFound() {
        when(subscriptionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> subscriptionService.updateSubscription(99L, new SubscriptionDTO()));
    }

    @Test
    void updateSubscription_throwsWhenUserDoesNotMatch() {
        AppUserEntity owner = new AppUserEntity();
        owner.setEmail("owner@example.com");

        SubscriptionEntity sub = new SubscriptionEntity();
        sub.setActive(true);
        sub.setUser(owner);

        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(sub));
        when(sessionContext.getEmail()).thenReturn("intruder@example.com");

        assertThrows(RuntimeException.class, () -> subscriptionService.updateSubscription(1L, new SubscriptionDTO()));
        verify(subscriptionRepository, never()).save(any());
    }

    @Test
    void updateSubscription_withActiveFalse_deactivatesOnly() {
        AppUserEntity owner = new AppUserEntity();
        owner.setEmail("user@example.com");

        SubscriptionEntity sub = new SubscriptionEntity();
        sub.setActive(true);
        sub.setNextBilling(LocalDate.now());
        sub.setUser(owner);

        SubscriptionDTO dto = new SubscriptionDTO();
        dto.setActive(false);

        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(sub));
        when(sessionContext.getEmail()).thenReturn("user@example.com");

        subscriptionService.updateSubscription(1L, dto);

        assertFalse(sub.isActive());
        assertNull(sub.getNextBilling());
        verify(subscriptionRepository).save(sub);
    }

    @Test
    void updateSubscription_withActiveTrue_updatesAllFields() {
        AppUserEntity owner = new AppUserEntity();
        owner.setEmail("user@example.com");

        SubscriptionEntity sub = new SubscriptionEntity();
        sub.setUser(owner);
        sub.setActive(true);

        SubscriptionDTO dto = new SubscriptionDTO();
        dto.setActive(true);
        dto.setName("Spotify");
        dto.setDescription("Music");
        dto.setNotes("note");

        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(sub));
        when(sessionContext.getEmail()).thenReturn("user@example.com");

        subscriptionService.updateSubscription(1L, dto);

        assertEquals("Spotify", sub.getName());
        assertEquals("Music", sub.getDescription());
        assertEquals("note", sub.getNotes());
        assertTrue(sub.isActive());
        verify(categoryRepository, never()).getReferenceById(any());
        verify(subscriptionRepository).save(sub);
    }

    @Test
    void updateSubscription_withActiveTrue_andCategory_setsCategory() {
        AppUserEntity owner = new AppUserEntity();
        owner.setEmail("user@example.com");

        SubscriptionEntity sub = new SubscriptionEntity();
        sub.setUser(owner);
        sub.setActive(true);

        CategoryEntity category = new CategoryEntity();

        SubscriptionDTO dto = new SubscriptionDTO();
        dto.setActive(true);
        dto.setCategoryId(5L);

        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(sub));
        when(sessionContext.getEmail()).thenReturn("user@example.com");
        when(categoryRepository.getReferenceById(5L)).thenReturn(category);

        subscriptionService.updateSubscription(1L, dto);

        assertEquals(category, sub.getCategory());
        verify(subscriptionRepository).save(sub);
    }

    @Test
    void getUpcomingSubscriptions_returnsMappedList() {
        List<SubscriptionEntity> entities = List.of(new SubscriptionEntity());
        List<SubscriptionDTO> dtos = List.of(new SubscriptionDTO());

        when(sessionContext.getEmail()).thenReturn("user@example.com");
        when(subscriptionRepository.findAllWithinCutoff(eq("user@example.com"), any(LocalDate.class)))
                .thenReturn(entities);
        when(mapper.toDtoList(entities)).thenReturn(dtos);

        List<SubscriptionDTO> result = subscriptionService.getUpcomingSubscriptions(30);

        assertEquals(1, result.size());
    }
}
