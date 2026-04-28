package no.tidly.modules.configuration.usecase.holiday;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.modules.configuration.repository.HolidayRepository;
import no.tidly.modules.organization.service.TenantService;

@Service
@RequiredArgsConstructor
public class DeleteHolidayUseCase {

    private final HolidayRepository repository;
    private final TenantService tenantService;

    @Transactional
    public void execute(UUID id) {
        var company = this.tenantService.getCurrentCompanyByTenant();
        if (!repository.existsByIdAndCompanyId(id, company.getId())) {
            throw new ResourceNotFoundException("Holiday not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
