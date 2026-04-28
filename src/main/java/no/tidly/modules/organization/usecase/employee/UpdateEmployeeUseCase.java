package no.tidly.modules.organization.usecase.employee;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.core.shared.Utils;
import no.tidly.modules.organization.dto.EmployeeRequest;
import no.tidly.modules.organization.dto.EmployeeResponse;
import no.tidly.modules.organization.mapper.EmployeeMapper;
import no.tidly.modules.organization.repository.CompanyRepository;
import no.tidly.modules.organization.repository.EmployeeRepository;
import no.tidly.modules.organization.repository.TeamRepository;
import no.tidly.modules.organization.service.TenantService;

@Service
@RequiredArgsConstructor
public class UpdateEmployeeUseCase {

        private final EmployeeRepository employeeRepository;
        private final CompanyRepository companyRepository;
        private final TeamRepository teamRepository;
        private final EmployeeMapper mapper;
        private final TenantService tenantService;

        public EmployeeResponse execute(UUID id, EmployeeRequest request) {
                var company = this.tenantService.getCurrentCompanyByTenant();
                var employee = this.employeeRepository.findByIdAndCompanyId(id, company.getId())
                                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

                this.teamRepository.findByIdAndCompanyId(request.teamId(), company.getId())
                                .orElseThrow(() -> new ResourceNotFoundException("Team not found"));

                Utils.copyNonNullProperties(request, employee);
                var savedEntity = this.employeeRepository.save(employee);
                return this.mapper.toResponse(savedEntity);
        }
}
