package no.tidly.modules.organization.usecase.company;

import java.util.List;

import org.springframework.stereotype.Service;

import no.tidly.core.security.SecurityContextService;
import no.tidly.modules.organization.dto.CompanyResponse;
import no.tidly.modules.organization.mapper.CompanyMapper;
import no.tidly.modules.organization.repository.CompanyRepository;

@Service
public class GetAllCompaniesUseCase {

    private final CompanyRepository companyRepository;
    private final CompanyMapper mapper;
    private final SecurityContextService securityContextService;

    public GetAllCompaniesUseCase(CompanyRepository companyRepository, CompanyMapper mapper,
            SecurityContextService securityContextService) {
        this.companyRepository = companyRepository;
        this.mapper = mapper;
        this.securityContextService = securityContextService;
    }

    public List<CompanyResponse> execute() {
        String activeClerkOrgId = securityContextService.getCurrentOrganizationId();
        System.out.println("Active Clerk Org ID: " + activeClerkOrgId);
        if (activeClerkOrgId == null) {
            return List.of();
        }

        return this.companyRepository.findAllByClerkOrgId(activeClerkOrgId).stream()
                .map(this.mapper::toResponse)
                .toList();
    }
}
