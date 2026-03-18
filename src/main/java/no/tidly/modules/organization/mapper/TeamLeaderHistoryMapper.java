package no.tidly.modules.organization.mapper;

import org.springframework.stereotype.Component;

import no.tidly.modules.organization.domain.TeamLeaderHistoryEntity;
import no.tidly.modules.organization.dto.TeamLeaderHistoryResponse;

@Component
public class TeamLeaderHistoryMapper {

    public TeamLeaderHistoryResponse toResponse(TeamLeaderHistoryEntity entity) {
        return new TeamLeaderHistoryResponse(
                entity.getId(),
                entity.getTeam().getId(),
                entity.getTeam().getName(),
                entity.getLeader().getId(),
                entity.getLeader().getName(),
                entity.getStartDate(),
                entity.getEndDate());
    }
}
