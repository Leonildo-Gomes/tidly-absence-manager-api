package no.tidly.modules.organization.usecase.department;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.modules.organization.domain.DepartmentManagerHistoryEntity;
import no.tidly.modules.organization.repository.DepartmentManagerHistoryRepository;
import no.tidly.modules.organization.repository.DepartmentRepository;

@Service
public class GetDepartmentManagerHistoryUseCase {

    private final DepartmentRepository departmentRepository;
    private final DepartmentManagerHistoryRepository historyRepository;

    public GetDepartmentManagerHistoryUseCase(DepartmentRepository departmentRepository,
            DepartmentManagerHistoryRepository historyRepository) {
        this.departmentRepository = departmentRepository;
        this.historyRepository = historyRepository;
    }

    public List<DepartmentManagerHistoryEntity> execute(UUID departmentId) {
        if (!this.departmentRepository.existsById(departmentId)) {
            throw new ResourceNotFoundException("Department not found");
        }
        // Use the optimized repository method
        return this.historyRepository.findByDepartmentIdOrderByStartDateDesc(departmentId);
    }
}
