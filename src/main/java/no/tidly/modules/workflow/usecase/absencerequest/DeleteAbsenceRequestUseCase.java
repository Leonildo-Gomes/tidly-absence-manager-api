package no.tidly.modules.workflow.usecase.absencerequest;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.modules.organization.service.TenantService;
import no.tidly.modules.workflow.repository.AbsenceRequestRepository;

@Service
@RequiredArgsConstructor
public class DeleteAbsenceRequestUseCase {

    private final AbsenceRequestRepository repository;
    private final TenantService tenantService;

    @Transactional
    public void execute(UUID id) {
        var company = tenantService.getCurrentCompanyByTenant();
        if (!this.repository.existsByIdAndCompanyId(id, company.getId())) {
            throw new ResourceNotFoundException("AbsenceRequest not found with id: " + id);
        }
        this.repository.deleteById(id);
    }
}
