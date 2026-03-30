package no.tidly.modules.organization.usecase.employee;

import org.springframework.stereotype.Service;

import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.modules.organization.domain.CompanyEntity;
import no.tidly.modules.organization.domain.EmployeeEntity;
import no.tidly.modules.organization.domain.TeamEntity;
import no.tidly.modules.organization.dto.EmployeeRequest;
import no.tidly.modules.organization.dto.EmployeeResponse;
import no.tidly.modules.organization.mapper.EmployeeMapper;
import no.tidly.modules.organization.repository.CompanyRepository;
import no.tidly.modules.organization.repository.EmployeeRepository;
import no.tidly.modules.organization.repository.TeamRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateEmployeeUseCase {

    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;
    private final TeamRepository teamRepository;
    private final EmployeeMapper mapper;

    public EmployeeResponse execute(EmployeeRequest request) {
        CompanyEntity company = null;
        if (request.companyId() != null) {
            company = this.companyRepository.findById(request.companyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
        }

        TeamEntity team = null;
        if (request.teamId() != null) {
            team = this.teamRepository.findById(request.teamId())
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
