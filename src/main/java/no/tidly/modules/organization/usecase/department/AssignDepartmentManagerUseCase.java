package no.tidly.modules.organization.usecase.department;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.modules.organization.domain.DepartmentManagerHistoryEntity;
import no.tidly.modules.organization.dto.AssignManagerRequest;
import no.tidly.modules.organization.dto.DepartmentManagerHistoryResponse;
import no.tidly.modules.organization.mapper.DepartmentManagerHistoryMapper;
import no.tidly.modules.organization.repository.DepartmentManagerHistoryRepository;
import no.tidly.modules.organization.repository.DepartmentRepository;
import no.tidly.modules.organization.repository.EmployeeRepository;

@Service
public class AssignDepartmentManagerUseCase {

        private final DepartmentRepository departmentRepository;
        private final EmployeeRepository employeeRepository;
        private final DepartmentManagerHistoryRepository historyRepository;
        private final DepartmentManagerHistoryMapper mapper;

        public AssignDepartmentManagerUseCase(DepartmentRepository departmentRepository,
                        EmployeeRepository employeeRepository,
                        DepartmentManagerHistoryRepository historyRepository,
                        DepartmentManagerHistoryMapper mapper) {
                this.departmentRepository = departmentRepository;
                this.employeeRepository = employeeRepository;
                this.historyRepository = historyRepository;
                this.mapper = mapper;
        }

        @Transactional
        public DepartmentManagerHistoryResponse execute(UUID departmentId, AssignManagerRequest request) {
                var department = this.departmentRepository.findById(departmentId)
                                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

                var manager = this.employeeRepository.findById(request.managerId())
                                .orElseThrow(() -> new ResourceNotFoundException("Manager (Employee) not found"));

                // 1. Close current active manager tenure if exists
                this.historyRepository.findActiveByDepartmentId(departmentId)
                                .ifPresent(currentHistory -> {
                                        // If the same manager is already assigned and active, do nothing
                                        if (currentHistory.getManager().getId().equals(request.managerId())) {
                                                return;
                                        }
                                        currentHistory.setEndDate(request.startDate());
                                        this.historyRepository.save(currentHistory);
                                });
                boolean alreadyAssigned = this.historyRepository.findActiveByDepartmentId(departmentId)
                                .map(h -> h.getManager().getId().equals(request.managerId()) && h.getEndDate() == null)
                                .orElse(false);

                if (alreadyAssigned) {
                        return this.historyRepository.findActiveByDepartmentId(departmentId)
                                        .map(this.mapper::toResponse)
                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                        "Department Manager History not found"));
                }

                var newHistory = DepartmentManagerHistoryEntity.builder()
                                .department(department)
                                .manager(manager)
                                .startDate(request.startDate())
                                .endDate(null)
                                .build();

                var savedHistory = this.historyRepository.save(newHistory);
                return this.mapper.toResponse(savedHistory);

        }
}
