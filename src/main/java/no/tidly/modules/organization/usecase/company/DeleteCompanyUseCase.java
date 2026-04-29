package no.tidly.modules.organization.usecase.company;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.ForbiddenAccessException;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.modules.organization.domain.CompanyEntity;
import no.tidly.modules.organization.repository.CompanyRepository;
import no.tidly.modules.organization.service.TenantService;

@Service
@RequiredArgsConstructor
public class DeleteCompanyUseCase {

    private final CompanyRepository repository;
    private final TenantService tenantService;

    public void execute(UUID id) {
        var companyTenant = this.tenantService.getCurrentCompanyByTenant();

        if (companyTenant == null || !companyTenant.getId().equals(id)) {
            throw new ForbiddenAccessException("O utilizador não tem permissão para eliminar esta empresa.");
        }

        CompanyEntity company = this.repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        company.setIsActive(false);

        this.repository.save(company);
    }
}
