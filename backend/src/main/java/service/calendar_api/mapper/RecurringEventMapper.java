package service.calendar_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import service.calendar_api.dto.OwnerDTO;
import service.calendar_api.dto.RecurringEventDTO;
import service.calendar_api.entity.RecurringEvent;

import java.util.List;

@Mapper(uses = OwnerMapper.class)
public interface RecurringEventMapper {
	RecurringEventMapper INSTANCE = Mappers.getMapper(RecurringEventMapper.class);

	List<RecurringEventDTO> toRecurringEventDTOList(List<RecurringEvent> events);

	@Mapping(target = "ownerId", source = "owner.id")
	RecurringEventDTO toRecurringEventDTO(RecurringEvent event);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "status", constant = "TO_DO")
	RecurringEvent toRecurringEvent(RecurringEventDTO dto, OwnerDTO owner);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "status", ignore = true)
	void updateRecurringEventFromDTO(RecurringEventDTO dto, @MappingTarget RecurringEvent entity);
}
