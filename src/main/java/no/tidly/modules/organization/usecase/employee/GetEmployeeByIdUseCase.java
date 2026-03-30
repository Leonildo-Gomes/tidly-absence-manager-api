package no.tidly.modules.organization.usecase.employee;

import java.util.UUID;

import org.springframework.stereotype.Service;

import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.modules.organization.dto.EmployeeResponse;
import no.tidly.modules.organization.mapper.EmployeeMapper;
import no.tidly.modules.organization.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetEmployeeByIdUseCase {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper mapper;

    public EmployeeResponse execute(UUID id) {
        return this.employeeRepository.findById(id)
                .map(this.mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
    }
}
