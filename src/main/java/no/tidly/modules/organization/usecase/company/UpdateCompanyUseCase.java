package no.tidly.modules.organization.usecase.company;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.ForbiddenAccessException;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.core.shared.Utils;
import no.tidly.modules.organization.dto.CompanyResponse;
import no.tidly.modules.organization.dto.UpdateCompanyRequest;
import no.tidly.modules.organization.mapper.CompanyMapper;
import no.tidly.modules.organization.repository.CompanyRepository;
import no.tidly.modules.organization.service.TenantService;

@Service
@RequiredArgsConstructor
public class UpdateCompanyUseCase {

    private final CompanyRepository companyRepository;
    private final CompanyMapper mapper;
    private final TenantService tenantService;

    public CompanyResponse execute(UUID id, UpdateCompanyRequest request) {
        var companyTenant = this.tenantService.getCurrentCompanyByTenant();
        var company = this.companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        if (companyTenant == null || !companyTenant.getId().equals(company.getId())) {
            throw new ForbiddenAccessException("O utilizador não tem permissão para atualizar esta empresa.");
        }

        Utils.copyNonNullProperties(request, company);
        var updatedEntity = this.companyRepository.save(company);
        return this.mapper.toResponse(updatedEntity);
    }
}
