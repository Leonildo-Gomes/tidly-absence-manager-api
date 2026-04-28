package no.tidly.modules.organization.usecase.team;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.modules.organization.dto.TeamResponse;
import no.tidly.modules.organization.mapper.TeamMapper;
import no.tidly.modules.organization.repository.TeamRepository;
import no.tidly.modules.organization.service.TenantService;

@Service
@RequiredArgsConstructor
public class GetTeamByIdUseCase {

    private final TeamRepository teamRepository;
    private final TeamMapper mapper;
    private final TenantService tenantService;

    public TeamResponse execute(UUID id) {
        var company = this.tenantService.getCurrentCompanyByTenant();
        return this.teamRepository.findByIdAndCompanyId(id, company.getId())
                .map(this.mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found"));
    }
}
