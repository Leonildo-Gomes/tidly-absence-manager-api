package no.tidly.modules.organization.usecase.team;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.modules.organization.domain.TeamEntity;
import no.tidly.modules.organization.repository.TeamRepository;
import no.tidly.modules.organization.service.TenantService;

@Service
@RequiredArgsConstructor
public class DeleteTeamUseCase {

    private final TeamRepository repository;
    private final TenantService tenantService;

    public void execute(UUID id) {
        var company = this.tenantService.getCurrentCompanyByTenant();
        TeamEntity team = this.repository.findByIdAndCompanyId(id, company.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Team not found"));
        team.setIsActive(false);
        this.repository.save(team);
    }
}
