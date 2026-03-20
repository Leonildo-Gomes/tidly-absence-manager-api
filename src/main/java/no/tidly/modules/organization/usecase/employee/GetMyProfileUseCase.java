package no.tidly.modules.organization.usecase.employee;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.core.security.SecurityContextService;
import no.tidly.modules.organization.dto.EmployeeResponse;
import no.tidly.modules.organization.mapper.EmployeeMapper;
import no.tidly.modules.organization.repository.EmployeeRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetMyProfileUseCase {

    private final EmployeeRepository employeeRepository;
    private final SecurityContextService securityContextService;
    private final EmployeeMapper mapper;

    public EmployeeResponse execute() {
        var userId = securityContextService.getCurrentUserId();
        log.info("Fetching profile for userId={}", userId);

        var employee = employeeRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found for userId=" + userId));

        return mapper.toResponse(employee);
    }
}
