package no.tidly.modules.organization.usecase.team;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.core.shared.Utils;
import no.tidly.modules.organization.domain.TeamEntity;
import no.tidly.modules.organization.dto.TeamRequest;
import no.tidly.modules.organization.dto.TeamResponse;
import no.tidly.modules.organization.mapper.TeamMapper;
import no.tidly.modules.organization.repository.DepartmentRepository;
import no.tidly.modules.organization.repository.TeamRepository;
import no.tidly.modules.organization.service.TenantService;

@Service
@RequiredArgsConstructor
public class UpdateTeamUseCase {

    private final TeamRepository teamRepository;
    private final DepartmentRepository departmentRepository;
    private final TeamMapper mapper;
    private final TenantService tenantService;

    public TeamResponse execute(UUID id, TeamRequest request) {
        var company = this.tenantService.getCurrentCompanyByTenant();
        TeamEntity team = this.teamRepository.findByIdAndCompanyId(id, company.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Team not found"));

        this.departmentRepository.findByIdAndCompanyId(request.departmentId(), company.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        Utils.copyNonNullProperties(request, team);
        var updatedEntity = this.teamRepository.save(team);
        return this.mapper.toResponse(updatedEntity);
    }
}
