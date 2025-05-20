package service.calendar_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import service.calendar_api.dto.EventDTO;
import service.calendar_api.dto.OwnerDTO;
import service.calendar_api.entity.Event;

import java.util.List;

@Mapper(uses = OwnerMapper.class)
public interface EventMapper {
	EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

	List<EventDTO> toEventDTOList(List<Event> events);

	@Mapping(target = "ownerId", source = "owner.id")
	EventDTO toEventDTO(Event event);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "status", constant = "TO_DO")
	Event toEvent(EventDTO dto, OwnerDTO owner);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "status", ignore = true)
	void updateEventFromDTO(EventDTO dto, @MappingTarget Event entity);
}
