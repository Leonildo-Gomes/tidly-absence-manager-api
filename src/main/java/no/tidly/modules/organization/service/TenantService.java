package no.tidly.modules.organization.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.core.security.SecurityContextService;
import no.tidly.modules.organization.domain.CompanyEntity;
import no.tidly.modules.organization.repository.CompanyRepository;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final CompanyRepository companyRepository;
    private final SecurityContextService securityContextService;

    public CompanyEntity getCurrentCompanyByTenant() {
        String clerkOrgId = securityContextService.getCurrentOrganizationId();
        if (clerkOrgId == null) {
            throw new ResourceNotFoundException("No active organization in JWT");
        }
        return companyRepository.findByClerkOrgId(clerkOrgId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
    }

}