package com.federicoinnocente.subs_tracker.mapper;

import com.federicoinnocente.subs_tracker.dto.SubscriptionDTO;
import com.federicoinnocente.subs_tracker.entity.SubscriptionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    @Mapping(source = "category.categoryId", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    SubscriptionDTO toDto(SubscriptionEntity subscription);

    @Mapping(target = "category", ignore = true)
    SubscriptionEntity toEntity(SubscriptionDTO subscriptionDTO);

    List<SubscriptionDTO> toDtoList(List<SubscriptionEntity> subscriptionList);

}
