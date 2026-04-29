package no.tidly.modules.organization.usecase.company;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.tidly.modules.organization.dto.CompanyResponse;
import no.tidly.modules.organization.mapper.CompanyMapper;
import no.tidly.modules.organization.repository.CompanyRepository;
import no.tidly.modules.organization.service.TenantService;

@Service
@RequiredArgsConstructor
public class GetAllCompaniesUseCase {

    private final CompanyRepository companyRepository;
    private final CompanyMapper mapper;
    private final TenantService tenantService;

    public List<CompanyResponse> execute() {
        var company = this.tenantService.getCurrentCompanyByTenant();
        return this.companyRepository.findAllByClerkOrgId(company.getClerkOrgId()).stream()
                .map(this.mapper::toResponse)
                .toList();
    }
}
