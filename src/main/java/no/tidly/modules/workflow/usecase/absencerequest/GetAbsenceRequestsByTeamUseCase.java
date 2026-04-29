package no.tidly.modules.workflow.usecase.absencerequest;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import no.tidly.modules.organization.service.TenantService;
import no.tidly.modules.workflow.dto.AbsenceRequestResponse;
import no.tidly.modules.workflow.mapper.AbsenceRequestMapper;
import no.tidly.modules.workflow.repository.AbsenceRequestRepository;

@Service
@RequiredArgsConstructor
public class GetAbsenceRequestsByTeamUseCase {

    private final AbsenceRequestRepository repository;
    private final AbsenceRequestMapper mapper;
    private final TenantService tenantService;

    @Transactional(readOnly = true)
    public List<AbsenceRequestResponse> execute(UUID teamId, Integer year) {
        var company = this.tenantService.getCurrentCompanyByTenant();
        var entities = repository.findAllByTeamIdAndYearAndCompanyId(teamId, year, company.getId());
        return entities.stream()
                .map(mapper::toResponse)
                .toList();
    }
}
