package no.tidly.modules.organization.usecase.department;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.modules.organization.domain.CompanyEntity;
import no.tidly.modules.organization.dto.DepartmentResponse;
import no.tidly.modules.organization.mapper.DepartmentMapper;
import no.tidly.modules.organization.repository.DepartmentRepository;
import no.tidly.modules.organization.service.TenantService;

@Service
@RequiredArgsConstructor
public class GetDepartmentByIdUseCase {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final TenantService tenantService;

    public DepartmentResponse execute(UUID id) {
        CompanyEntity company = tenantService.getCurrentCompanyByTenant();
        return this.departmentRepository.findByIdAndCompanyId(id, company.getId())
                .map(this.departmentMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
    }
}
