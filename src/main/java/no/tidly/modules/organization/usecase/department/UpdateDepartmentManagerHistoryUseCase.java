package no.tidly.modules.organization.usecase.department;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.modules.organization.dto.DepartmentManagerHistoryResponse;
import no.tidly.modules.organization.dto.UpdateDepartmentManagerHistoryRequest;
import no.tidly.modules.organization.repository.DepartmentManagerHistoryRepository;
import no.tidly.modules.organization.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateDepartmentManagerHistoryUseCase {

    private final DepartmentManagerHistoryRepository historyRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional
    public DepartmentManagerHistoryResponse execute(UUID id, UpdateDepartmentManagerHistoryRequest request) {
        var history = this.historyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department Manager History not found"));

        var newManager = this.employeeRepository.findById(request.managerId())
                .orElseThrow(() -> new ResourceNotFoundException("Manager (Employee) not found"));

        history.setManager(newManager);
        history.setStartDate(request.startDate());
        history.setEndDate(request.endDate());

        var updatedHistory = this.historyRepository.save(history);

        return new DepartmentManagerHistoryResponse(
                updatedHistory.getId(),
                updatedHistory.getDepartment().getId(),
                updatedHistory.getDepartment().getName(),
                updatedHistory.getManager().getId(),
                updatedHistory.getManager().getName(),
                updatedHistory.getStartDate(),
                updatedHistory.getEndDate());
    }
}
