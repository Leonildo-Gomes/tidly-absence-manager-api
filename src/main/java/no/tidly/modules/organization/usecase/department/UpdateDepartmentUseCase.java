package no.tidly.modules.organization.usecase.department;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.core.shared.Utils;
import no.tidly.modules.organization.dto.DepartmentRequest;
import no.tidly.modules.organization.dto.DepartmentResponse;
import no.tidly.modules.organization.mapper.DepartmentMapper;
import no.tidly.modules.organization.repository.DepartmentRepository;
import no.tidly.modules.organization.service.TenantService;

@Service
@RequiredArgsConstructor
public class UpdateDepartmentUseCase {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper mapper;
    private final TenantService tenantService;

    public DepartmentResponse execute(UUID id, DepartmentRequest request) {
        var company = tenantService.getCurrentCompanyByTenant();
        var department = this.departmentRepository.findByIdAndCompanyId(id, company.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        Utils.copyNonNullProperties(request, department);
        var updatedEntity = this.departmentRepository.save(department);
        return this.mapper.toResponse(updatedEntity);
    }
}
