package no.tidly.modules.organization.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.core.security.SecurityContextService;
import no.tidly.modules.organization.domain.CompanyEntity;
import no.tidly.modules.organization.domain.EmployeeEntity;
import no.tidly.modules.organization.repository.CompanyRepository;
import no.tidly.modules.organization.repository.EmployeeRepository;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;
    private final SecurityContextService securityContextService;

    public CompanyEntity getCurrentCompanyByTenant() {
        String clerkOrgId = securityContextService.getCurrentOrganizationId();
        if (clerkOrgId == null) {
            throw new ResourceNotFoundException("No active organization in JWT");
        }
        return companyRepository.findByClerkOrgId(clerkOrgId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
    }

    public EmployeeEntity getCurrentEmployee() {
        var company = getCurrentCompanyByTenant();
        var userId = securityContextService.getCurrentUserId();
        if (userId == null || userId.isBlank()) {
            throw new ResourceNotFoundException("User not authenticated");
        }
        return employeeRepository.findByUserIdAndCompanyId(userId, company.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found for userId=" + userId));
    }

}