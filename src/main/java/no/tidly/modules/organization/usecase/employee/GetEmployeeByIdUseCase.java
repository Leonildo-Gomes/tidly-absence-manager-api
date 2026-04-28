package no.tidly.modules.organization.usecase.employee;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.modules.organization.dto.EmployeeResponse;
import no.tidly.modules.organization.mapper.EmployeeMapper;
import no.tidly.modules.organization.repository.EmployeeRepository;
import no.tidly.modules.organization.service.TenantService;

@Service
@RequiredArgsConstructor
public class GetEmployeeByIdUseCase {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper mapper;
    private final TenantService tenantService;

    public EmployeeResponse execute(UUID id) {
        var company = this.tenantService.getCurrentCompanyByTenant();
        return this.employeeRepository.findByIdAndCompanyId(id, company.getId())
                .map(this.mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
    }
}
