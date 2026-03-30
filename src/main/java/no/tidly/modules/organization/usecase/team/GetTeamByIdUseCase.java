package no.tidly.modules.organization.usecase.team;

import java.util.UUID;

import org.springframework.stereotype.Service;

import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.modules.organization.dto.TeamResponse;
import no.tidly.modules.organization.mapper.TeamMapper;
import no.tidly.modules.organization.repository.TeamRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetTeamByIdUseCase {

    private final TeamRepository teamRepository;
    private final TeamMapper mapper;

    public TeamResponse execute(UUID id) {
        return this.teamRepository.findById(id)
                .map(this.mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found"));
    }
}
