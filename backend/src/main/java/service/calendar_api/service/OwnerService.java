package service.calendar_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import service.calendar_api.dto.OwnerDTO;
import service.calendar_api.entity.Owner;
import service.calendar_api.exception.ResourceNotFoundException;
import service.calendar_api.mapper.OwnerMapper;
import service.calendar_api.repository.OwnerRepository;

@Service
@RequiredArgsConstructor
public class OwnerService {
	private final OwnerRepository repository;

	private Owner findById(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Owner not found"));
	}

	public OwnerDTO getOwner(Long id) {
		return OwnerMapper.INSTANCE.toOwnerDTO(findById(id));
	}

	public OwnerDTO create(OwnerDTO request) {
		var owner = repository.save(OwnerMapper.INSTANCE.toOwner(request));
		return OwnerMapper.INSTANCE.toOwnerDTO(owner);
	}

	public OwnerDTO update(Long id, OwnerDTO request) {
		var owner = findById(id);
		OwnerMapper.INSTANCE.updateOwnerFromDTO(request, owner);

		owner = repository.save(owner);
		return OwnerMapper.INSTANCE.toOwnerDTO(owner);
	}

	public void deleteById(Long id) {
		repository.deleteById(id);
	}
}
