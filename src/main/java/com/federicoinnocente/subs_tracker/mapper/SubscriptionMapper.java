package com.federicoinnocente.subs_tracker.mapper;

import com.federicoinnocente.subs_tracker.dto.SubscriptionDTO;
import com.federicoinnocente.subs_tracker.entity.SubscriptionEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    SubscriptionDTO toDto(SubscriptionEntity subscription);

    SubscriptionEntity toEntity(SubscriptionDTO subscriptionDTO);

    List<SubscriptionDTO> toDtoList(List<SubscriptionEntity> subscriptionList);

}
