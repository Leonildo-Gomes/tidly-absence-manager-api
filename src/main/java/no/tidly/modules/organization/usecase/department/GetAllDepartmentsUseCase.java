package no.tidly.modules.organization.usecase.department;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.tidly.modules.organization.dto.DepartmentResponse;
import no.tidly.modules.organization.mapper.DepartmentMapper;
import no.tidly.modules.organization.repository.DepartmentRepository;
import no.tidly.modules.organization.service.TenantService;

@Service
@RequiredArgsConstructor
public class GetAllDepartmentsUseCase {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final TenantService tenantService;

    public List<DepartmentResponse> execute() {
        var company = this.tenantService.getCurrentCompanyByTenant();
        return this.departmentRepository.findAllByCompanyId(company.getId()).stream()
                .map(this.departmentMapper::toResponse)
                .toList();
    }
}
