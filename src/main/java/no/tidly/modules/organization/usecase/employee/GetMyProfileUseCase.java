package no.tidly.modules.organization.usecase.employee;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.core.security.SecurityContextService;
import no.tidly.modules.organization.dto.EmployeeResponse;
import no.tidly.modules.organization.mapper.EmployeeMapper;
import no.tidly.modules.organization.repository.EmployeeRepository;
import no.tidly.modules.organization.service.TenantService;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetMyProfileUseCase {

    private final EmployeeRepository employeeRepository;
    private final SecurityContextService securityContextService;
    private final EmployeeMapper mapper;
    private final TenantService tenantService;

    public EmployeeResponse execute() {
        var company = this.tenantService.getCurrentCompanyByTenant();
        var userId = securityContextService.getCurrentUserId();
        log.info("Fetching profile for userId={}", userId);

        var employee = employeeRepository.findByUserIdAndCompanyId(userId, company.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found for userId=" + userId));

        return mapper.toResponse(employee);
    }
}
