package no.tidly.modules.configuration.usecase.companyabsencesettings;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.modules.configuration.repository.CompanyAbsenceSettingsRepository;
import no.tidly.modules.organization.service.TenantService;

@Service
@RequiredArgsConstructor
public class DeleteCompanyAbsenceSettingsUseCase {

    private final CompanyAbsenceSettingsRepository repository;
    private final TenantService tenantService;

    @Transactional
    public void execute(UUID id) {
        var company = this.tenantService.getCurrentCompanyByTenant();
        if (!repository.existsByIdAndCompanyId(id, company.getId())) {
            throw new ResourceNotFoundException("CompanyAbsenceSettings not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
