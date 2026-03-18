package no.tidly.modules.organization.usecase.employee;

import java.util.UUID;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import no.tidly.modules.organization.domain.EmployeeJobTitle;
import no.tidly.modules.organization.dto.EmployeeJobTitleRequest;
import no.tidly.modules.organization.dto.EmployeeJobTitleResponse;
import no.tidly.modules.organization.mapper.EmployeeJobTitleMapper;
import no.tidly.modules.organization.repository.EmployeeJobTitleRepository;
import no.tidly.modules.organization.repository.EmployeeRepository;
import no.tidly.modules.organization.repository.JobTitleRepository;

@Service
@RequiredArgsConstructor
public class AssignJobTitleUseCase {

        private final EmployeeJobTitleRepository repository;
        private final JobTitleRepository jobTitleRepository;
        private final EmployeeRepository employeeRepository;
        private final EmployeeJobTitleMapper mapper;

        public EmployeeJobTitleResponse execute(UUID employeeId, EmployeeJobTitleRequest request) {
                var jobTitle = jobTitleRepository.findById(request.jobTitleId())
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "JobTitle not found with id: " + request.jobTitleId()));

                var employee = employeeRepository.findById(employeeId)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Employee not found with id: " + employeeId));

                var entity = EmployeeJobTitle.builder()
                                .employee(employee)
                                .jobTitle(jobTitle)
                                .startDate(request.startDate())
                                .endDate(request.endDate())
                                .build();

                return mapper.toResponse(repository.save(entity));
        }
}
