package no.tidly.modules.organization.usecase.department;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.modules.organization.domain.DepartmentManagerHistoryEntity;
import no.tidly.modules.organization.dto.DepartmentManagerHistoryRequest;
import no.tidly.modules.organization.dto.DepartmentManagerHistoryResponse;
import no.tidly.modules.organization.repository.DepartmentManagerHistoryRepository;
import no.tidly.modules.organization.repository.DepartmentRepository;
import no.tidly.modules.organization.repository.EmployeeRepository;

@Service
public class CreateDepartmentManagerHistoryUseCase {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentManagerHistoryRepository historyRepository;

    public CreateDepartmentManagerHistoryUseCase(DepartmentRepository departmentRepository,
            EmployeeRepository employeeRepository,
            DepartmentManagerHistoryRepository historyRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.historyRepository = historyRepository;
    }

    @Transactional
    public DepartmentManagerHistoryResponse execute(DepartmentManagerHistoryRequest request) {
        var department = this.departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        var manager = this.employeeRepository.findById(request.managerId())
                .orElseThrow(() -> new ResourceNotFoundException("Manager (Employee) not found"));

        var newHistory = DepartmentManagerHistoryEntity.builder()
                .department(department)
                .manager(manager)
                .startDate(request.startDate())
                .endDate(request.endDate())
                .build();

        var savedHistory = this.historyRepository.save(newHistory);

        return new DepartmentManagerHistoryResponse(
                savedHistory.getId(),
                department.getId(),
                department.getName(),
                manager.getId(),
                manager.getName(),
                savedHistory.getStartDate(),
                savedHistory.getEndDate());
    }
}
