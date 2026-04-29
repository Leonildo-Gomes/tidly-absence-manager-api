package no.tidly.modules.organization.usecase.team;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.tidly.modules.organization.dto.TeamResponse;
import no.tidly.modules.organization.mapper.TeamMapper;
import no.tidly.modules.organization.repository.TeamRepository;
import no.tidly.modules.organization.service.TenantService;

@Service
@RequiredArgsConstructor
public class GetAllTeamsUseCase {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;
    private final TenantService tenantService;

    public List<TeamResponse> execute() {
        var company = this.tenantService.getCurrentCompanyByTenant();
        return this.teamRepository.findAllByCompanyId(company.getId()).stream()
                .map(this.teamMapper::toResponse)
                .toList();
    }
}
