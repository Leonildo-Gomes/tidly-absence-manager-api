package no.tidly.modules.organization.usecase.department;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.modules.organization.domain.DepartmentEntity;
import no.tidly.modules.organization.dto.DepartmentRequest;
import no.tidly.modules.organization.dto.DepartmentResponse;
import no.tidly.modules.organization.mapper.DepartmentMapper;
import no.tidly.modules.organization.repository.DepartmentRepository;
import no.tidly.modules.organization.service.TenantService;

@Service
@RequiredArgsConstructor
public class CreateDepartmentUseCase {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final TenantService tenantService;

    public DepartmentResponse execute(DepartmentRequest request) {
        var company = this.tenantService.getCurrentCompanyByTenant();

        DepartmentEntity parentDepartment = null;
        if (request.parentDepartmentId() != null) {
            parentDepartment = this.departmentRepository
                    .findByIdAndCompanyId(request.parentDepartmentId(), company.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent department not found"));
        }

        var department = DepartmentEntity.builder()
                .name(request.name())
                .code(request.code())
                .company(company)
                .parentDepartment(parentDepartment)
                .build();
        var savedEntity = this.departmentRepository.save(department);
        return this.departmentMapper.toResponse(savedEntity);
    }
}
