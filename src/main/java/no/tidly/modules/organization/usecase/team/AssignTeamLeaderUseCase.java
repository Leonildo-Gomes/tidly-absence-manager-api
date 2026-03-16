package no.tidly.modules.organization.usecase.team;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.modules.organization.domain.TeamLeaderHistoryEntity;
import no.tidly.modules.organization.repository.EmployeeRepository;
import no.tidly.modules.organization.repository.TeamLeaderHistoryRepository;
import no.tidly.modules.organization.repository.TeamRepository;

@Service
@RequiredArgsConstructor
public class AssignTeamLeaderUseCase {

        private final TeamRepository teamRepository;
        private final EmployeeRepository employeeRepository;
        private final TeamLeaderHistoryRepository historyRepository;

        @Transactional
        public void execute(UUID teamId, UUID leaderId) {
                var team = this.teamRepository.findById(teamId)
                                .orElseThrow(() -> new ResourceNotFoundException("Team not found"));

                var leader = this.employeeRepository.findById(leaderId)
                                .orElseThrow(() -> new ResourceNotFoundException("Leader (Employee) not found"));

                boolean alreadyAssigned = this.historyRepository.findActiveByTeamId(teamId)
                                .map(h -> h.getLeader().getId().equals(leaderId) && h.getEndDate() == null)
                                .orElse(false);

                if (alreadyAssigned) {
                        return;
                }

                this.historyRepository.findActiveByTeamId(teamId)
                                .ifPresent(currentHistory -> {
                                        currentHistory.setEndDate(LocalDate.now());
                                        this.historyRepository.save(currentHistory);
                                });

                var newHistory = TeamLeaderHistoryEntity.builder()
                                .team(team)
                                .leader(leader)
                                .startDate(LocalDate.now())
                                .build();

                this.historyRepository.save(newHistory);
        }
}
