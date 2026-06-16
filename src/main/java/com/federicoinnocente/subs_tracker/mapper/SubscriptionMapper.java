package com.federicoinnocente.subs_tracker.mapper;

import com.federicoinnocente.subs_tracker.entity.Subscription;
import com.federicoinnocente.subs_tracker.dto.SubscriptionDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    SubscriptionDTO toDto(Subscription subscription);

    Subscription toEntity(SubscriptionDTO subscriptionDTO);

    List<SubscriptionDTO> toDtoList(List<Subscription> subscriptionList);
}
