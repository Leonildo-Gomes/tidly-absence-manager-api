package no.tidly.modules.organization.usecase.employee;

import java.util.UUID;

import org.springframework.stereotype.Service;

import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.core.shared.Utils;
import no.tidly.modules.organization.dto.EmployeeRequest;
import no.tidly.modules.organization.dto.EmployeeResponse;
import no.tidly.modules.organization.mapper.EmployeeMapper;
import no.tidly.modules.organization.repository.CompanyRepository;
import no.tidly.modules.organization.repository.EmployeeRepository;
import no.tidly.modules.organization.repository.TeamRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateEmployeeUseCase {

        private final EmployeeRepository employeeRepository;
        private final CompanyRepository companyRepository;
        private final TeamRepository teamRepository;
        private final EmployeeMapper mapper;

        public EmployeeResponse execute(UUID id, EmployeeRequest request) {
                var employee = this.employeeRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

                this.teamRepository.findById(request.teamId())
                                .orElseThrow(() -> new ResourceNotFoundException("Team not found"));

                this.companyRepository.findById(request.companyId())
                                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

                Utils.copyNonNullProperties(request, employee);
                var savedEntity = this.employeeRepository.save(employee);
                return this.mapper.toResponse(savedEntity);
        }
}
