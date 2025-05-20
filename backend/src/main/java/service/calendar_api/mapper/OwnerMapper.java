package service.calendar_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import service.calendar_api.dto.OwnerDTO;
import service.calendar_api.entity.Owner;

@Mapper
public interface OwnerMapper {
	OwnerMapper INSTANCE = Mappers.getMapper(OwnerMapper.class);

	OwnerDTO toOwnerDTO(Owner owner);

	Owner toOwner(OwnerDTO dto);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "name", source = "name")
	@Mapping(target = "email", source = "email")
	void updateOwnerFromDTO(OwnerDTO dto, @MappingTarget Owner entity);
}
