package service.calendar_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import service.calendar_api.dto.OwnerDTO;
import service.calendar_api.entity.Owner;

@Mapper
public interface OwnerMapper {
	OwnerMapper INSTANCE = Mappers.getMapper(OwnerMapper.class);

	OwnerDTO toOwnerDTO(Owner owner);

	Owner toOwner(OwnerDTO dto);
}
