package no.tidly.modules.workflow.usecase.absencerequest;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.core.shared.Utils;
import no.tidly.modules.organization.service.TenantService;
import no.tidly.modules.workflow.domain.AbsenceRequestEntity;
import no.tidly.modules.workflow.dto.AbsenceRequestRequest;
import no.tidly.modules.workflow.dto.AbsenceRequestResponse;
import no.tidly.modules.workflow.mapper.AbsenceRequestMapper;
import no.tidly.modules.workflow.repository.AbsenceRequestRepository;

@Service
@RequiredArgsConstructor
public class UpdateAbsenceRequestUseCase {

    private final AbsenceRequestRepository repository;
    private final AbsenceRequestMapper mapper;
    private final TenantService tenantService;

    @Transactional
    public AbsenceRequestResponse execute(UUID id, AbsenceRequestRequest request) {
        var company = this.tenantService.getCurrentCompanyByTenant();
        AbsenceRequestEntity entity = this.repository.findByIdAndCompanyId(id, company.getId())
                .orElseThrow(() -> new ResourceNotFoundException("AbsenceRequest not found with id: " + id));

        Utils.copyNonNullProperties(request, entity);
        AbsenceRequestEntity updatedEntity = this.repository.save(entity);
        return this.mapper.toResponse(updatedEntity);
    }
}
