package no.tidly.modules.workflow.usecase.absencerequest;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.core.security.SecurityContextService;
import no.tidly.modules.organization.repository.EmployeeRepository;
import no.tidly.modules.workflow.dto.AbsenceRequestResponse;
import no.tidly.modules.workflow.mapper.AbsenceRequestMapper;
import no.tidly.modules.workflow.repository.AbsenceRequestRepository;

@Service
@RequiredArgsConstructor
public class GetAbsenceRequestsByEmployeeAndYearUseCase {

    private final AbsenceRequestRepository repository;
    private final AbsenceRequestMapper mapper;
    private final SecurityContextService securityContextService;
    private final EmployeeRepository employeeRepository;

    @Transactional(readOnly = true)
    public List<AbsenceRequestResponse> execute(Integer year) {
        var currentUserId = securityContextService.getCurrentUserId();
        if (currentUserId == null) {
            throw new AccessDeniedException("You don't have permission to access this resource");
        }
        var employee = employeeRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + currentUserId));

        var entities = repository.findByEmployeeIdAndYear(employee.getId(), year);
        return entities.stream()
                .map(mapper::toResponse)
                .toList();
    }
}
