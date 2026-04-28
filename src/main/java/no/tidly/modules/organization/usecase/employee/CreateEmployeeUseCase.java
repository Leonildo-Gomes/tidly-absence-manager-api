package no.tidly.modules.organization.usecase.employee;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.modules.organization.domain.EmployeeEntity;
import no.tidly.modules.organization.domain.TeamEntity;
import no.tidly.modules.organization.dto.EmployeeRequest;
import no.tidly.modules.organization.dto.EmployeeResponse;
import no.tidly.modules.organization.mapper.EmployeeMapper;
import no.tidly.modules.organization.repository.EmployeeRepository;
import no.tidly.modules.organization.repository.TeamRepository;
import no.tidly.modules.organization.service.TenantService;

@Service
@RequiredArgsConstructor
public class CreateEmployeeUseCase {

    private final EmployeeRepository employeeRepository;
    private final TeamRepository teamRepository;
    private final EmployeeMapper mapper;
    private final TenantService tenantService;

    public EmployeeResponse execute(EmployeeRequest request) {
        var company = this.tenantService.getCurrentCompanyByTenant();

        TeamEntity team = null;
        if (request.teamId() != null) {
            team = this.teamRepository.findByIdAndCompanyId(request.teamId(), company.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Team not found"));
        }

        var employee = EmployeeEntity.builder()
                .userId(request.userId())
                .name(request.name())
                .email(request.email())
                .phone(request.phone())
                .gender(request.gender())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .isActive(request.isActive())
                .company(company)
                .team(team)
                .build();
        var savedEntity = this.employeeRepository.save(employee);
        return this.mapper.toResponse(savedEntity);
    }
}
