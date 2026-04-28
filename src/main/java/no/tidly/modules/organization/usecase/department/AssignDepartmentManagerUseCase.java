package no.tidly.modules.organization.usecase.department;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.modules.organization.domain.DepartmentManagerHistoryEntity;
import no.tidly.modules.organization.dto.AssignManagerRequest;
import no.tidly.modules.organization.dto.DepartmentManagerHistoryResponse;
import no.tidly.modules.organization.mapper.DepartmentManagerHistoryMapper;
import no.tidly.modules.organization.repository.DepartmentManagerHistoryRepository;
import no.tidly.modules.organization.repository.DepartmentRepository;
import no.tidly.modules.organization.repository.EmployeeRepository;
import no.tidly.modules.organization.service.TenantService;

@Service
@RequiredArgsConstructor
public class AssignDepartmentManagerUseCase {

        private final DepartmentRepository departmentRepository;
        private final EmployeeRepository employeeRepository;
        private final DepartmentManagerHistoryRepository historyRepository;
        private final DepartmentManagerHistoryMapper mapper;
        private final TenantService tenantService;

        @Transactional
        public DepartmentManagerHistoryResponse execute(UUID departmentId, AssignManagerRequest request) {
                var company = this.tenantService.getCurrentCompanyByTenant();
                var department = this.departmentRepository.findByIdAndCompanyId(departmentId, company.getId())
                                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

                var manager = this.employeeRepository.findByIdAndCompanyId(request.managerId(), company.getId())
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
