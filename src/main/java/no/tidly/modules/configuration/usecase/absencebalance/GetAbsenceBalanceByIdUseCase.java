package no.tidly.modules.configuration.usecase.absencebalance;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.modules.configuration.domain.AbsenceBalanceEntity;
import no.tidly.modules.configuration.dto.AbsenceBalanceResponse;
import no.tidly.modules.configuration.mapper.AbsenceBalanceMapper;
import no.tidly.modules.configuration.repository.AbsenceBalanceRepository;
import no.tidly.modules.organization.service.TenantService;

@Service
@RequiredArgsConstructor
public class GetAbsenceBalanceByIdUseCase {

    private final AbsenceBalanceRepository repository;
    private final AbsenceBalanceMapper mapper;
    private final TenantService tenantService;

    @Transactional(readOnly = true)
    public AbsenceBalanceResponse execute(UUID id) {
        var company = this.tenantService.getCurrentCompanyByTenant();
        AbsenceBalanceEntity entity = repository.findByIdAndCompanyId(id, company.getId())
                .orElseThrow(() -> new ResourceNotFoundException("AbsenceBalance not found with id: " + id));

        return mapper.toResponse(entity);
    }
}
