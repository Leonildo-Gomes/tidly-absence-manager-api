package no.tidly.modules.organization.usecase.company;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.ForbiddenAccessException;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.modules.organization.dto.CompanyResponse;
import no.tidly.modules.organization.mapper.CompanyMapper;
import no.tidly.modules.organization.repository.CompanyRepository;
import no.tidly.modules.organization.service.TenantService;

@Service
@RequiredArgsConstructor
public class GetCompanyByIdUseCase {

    private final CompanyRepository companyRepository;
    private final CompanyMapper mapper;
    private final TenantService tenantService;

    public CompanyResponse execute(UUID id) {
        var currentCompany = this.tenantService.getCurrentCompanyByTenant();
        var company = this.companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        if (currentCompany == null || !currentCompany.getId().equals(id)) {
            throw new ForbiddenAccessException("O utilizador não tem permissão para aceder a esta empresa.");
        }

        return this.mapper.toResponse(company);
    }
}
