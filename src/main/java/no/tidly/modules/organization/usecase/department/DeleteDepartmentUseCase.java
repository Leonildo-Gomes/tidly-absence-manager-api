package no.tidly.modules.organization.usecase.department;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.modules.organization.repository.DepartmentRepository;
import no.tidly.modules.organization.service.TenantService;

@Service
@RequiredArgsConstructor
public class DeleteDepartmentUseCase {

    private final DepartmentRepository repository;
    private final TenantService tenantService;

    public void execute(UUID id) {
        var company = tenantService.getCurrentCompanyByTenant();
        var department = this.repository.findByIdAndCompanyId(id, company.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        department.setIsActive(false);
        this.repository.save(department);
    }
}
