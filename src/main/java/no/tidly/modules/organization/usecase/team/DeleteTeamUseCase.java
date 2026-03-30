package no.tidly.modules.organization.usecase.team;

import java.util.UUID;

import org.springframework.stereotype.Service;

import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.modules.organization.domain.TeamEntity;
import no.tidly.modules.organization.repository.TeamRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteTeamUseCase {

    private final TeamRepository repository;

    public void execute(UUID id) {
        TeamEntity team = this.repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found"));

        team.setIsActive(false);
        
        this.repository.save(team);
    }
}
