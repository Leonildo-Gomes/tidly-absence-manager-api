package no.tidly.modules.workflow.usecase.absenceapproval;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import no.tidly.modules.organization.service.TenantService;
import no.tidly.modules.workflow.dto.AbsenceApprovalResponse;
import no.tidly.modules.workflow.mapper.AbsenceApprovalMapper;
import no.tidly.modules.workflow.repository.AbsenceApprovalRepository;

@Service
@RequiredArgsConstructor
public class GetAbsenceApprovalsByRequestUseCase {

    private final AbsenceApprovalRepository repository;
    private final AbsenceApprovalMapper mapper;
    private final TenantService tenantService;

    @Transactional(readOnly = true)
    public List<AbsenceApprovalResponse> execute(UUID absenceRequestId) {
        var company = this.tenantService.getCurrentCompanyByTenant();
        return this.repository.findByAbsenceRequestIdAndCompanyId(absenceRequestId, company.getId()).stream()
                .map(mapper::toResponse).toList();
    }
}
