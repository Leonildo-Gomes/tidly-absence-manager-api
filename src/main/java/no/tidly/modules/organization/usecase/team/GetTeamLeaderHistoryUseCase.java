package no.tidly.modules.organization.usecase.team;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.modules.organization.domain.TeamLeaderHistoryEntity;
import no.tidly.modules.organization.repository.TeamLeaderHistoryRepository;
import no.tidly.modules.organization.repository.TeamRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetTeamLeaderHistoryUseCase {

    private final TeamRepository teamRepository;
    private final TeamLeaderHistoryRepository historyRepository;

    public List<TeamLeaderHistoryEntity> execute(UUID teamId) {
        if (!this.teamRepository.existsById(teamId)) {
            throw new ResourceNotFoundException("Team not found");
        }
        return this.historyRepository.findAll().stream()
                .filter(h -> h.getTeam().getId().equals(teamId))
                .sorted((a, b) -> b.getStartDate().compareTo(a.getStartDate()))
                .toList();
    }
}
