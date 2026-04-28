package no.tidly.modules.organization.usecase.employee;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.modules.organization.domain.EmployeeEntity;
import no.tidly.modules.organization.repository.EmployeeRepository;
import no.tidly.modules.organization.service.TenantService;

@Service
@RequiredArgsConstructor
public class DeleteEmployeeUseCase {

    private final EmployeeRepository repository;
    private final TenantService tenantService;

    public void execute(UUID id) {
        var company = this.tenantService.getCurrentCompanyByTenant();
        EmployeeEntity employee = this.repository.findByIdAndCompanyId(id, company.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        employee.setIsActive(false);
        this.repository.save(employee);
    }
}
