package no.tidly.modules.organization.usecase.team;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.modules.organization.domain.TeamLeaderHistoryEntity;
import no.tidly.modules.organization.dto.AssignLeaderRequest;
import no.tidly.modules.organization.dto.TeamLeaderHistoryResponse;
import no.tidly.modules.organization.mapper.TeamLeaderHistoryMapper;
import no.tidly.modules.organization.repository.EmployeeRepository;
import no.tidly.modules.organization.repository.TeamLeaderHistoryRepository;
import no.tidly.modules.organization.repository.TeamRepository;
import no.tidly.modules.organization.service.TenantService;

@Service
@RequiredArgsConstructor
public class AssignTeamLeaderUseCase {

        private final TeamRepository teamRepository;
        private final EmployeeRepository employeeRepository;
        private final TeamLeaderHistoryRepository historyRepository;
        private final TeamLeaderHistoryMapper mapper;
        private final TenantService tenantService;

        @Transactional
        public TeamLeaderHistoryResponse execute(UUID teamId, AssignLeaderRequest request) {
                var company = this.tenantService.getCurrentCompanyByTenant();
                var team = this.teamRepository.findByIdAndCompanyId(teamId, company.getId())
                                .orElseThrow(() -> new ResourceNotFoundException("Team not found"));

                var leader = this.employeeRepository.findByIdAndCompanyId(request.leaderId(), company.getId())
                                .orElseThrow(() -> new ResourceNotFoundException("Leader (Employee) not found"));

                this.historyRepository.findActiveByTeamId(teamId)
                                .ifPresent(currentHistory -> {
                                        if (currentHistory.getLeader().getId().equals(request.leaderId())) {
                                                return;
                                        }
                                        currentHistory.setEndDate(request.startDate());
                                        this.historyRepository.save(currentHistory);
                                });

                boolean alreadyAssigned = this.historyRepository.findActiveByTeamId(teamId)
                                .map(h -> h.getLeader().getId().equals(request.leaderId()) && h.getEndDate() == null)
                                .orElse(false);

                if (alreadyAssigned) {
                        return this.historyRepository.findActiveByTeamId(teamId)
                                        .map(this.mapper::toResponse)
                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                        "Team Leader History not found"));
                }

                var newHistory = TeamLeaderHistoryEntity.builder()
                                .team(team)
                                .leader(leader)
                                .startDate(request.startDate())
                                .build();

                this.historyRepository.save(newHistory);
                return this.mapper.toResponse(newHistory);
        }
}
