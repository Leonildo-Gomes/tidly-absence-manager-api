package no.tidly.modules.organization.usecase.employee;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.tidly.modules.organization.dto.EmployeeResponse;
import no.tidly.modules.organization.mapper.EmployeeMapper;
import no.tidly.modules.organization.repository.EmployeeRepository;
import no.tidly.modules.organization.service.TenantService;

@Service
@RequiredArgsConstructor
public class GetAllEmployeesUseCase {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper mapper;
    private final TenantService tenantService;

    public List<EmployeeResponse> execute() {
        var company = this.tenantService.getCurrentCompanyByTenant();
        return this.employeeRepository.findAllByCompanyId(company.getId()).stream()
                .map(this.mapper::toResponse)
                .toList();
    }
}
