package no.tidly.modules.organization.usecase.department;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.modules.organization.repository.DepartmentManagerHistoryRepository;

@Service
public class DeleteDepartmentManagerHistoryUseCase {

    private final DepartmentManagerHistoryRepository historyRepository;

    public DeleteDepartmentManagerHistoryUseCase(DepartmentManagerHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Transactional
    public void execute(UUID id) {
        if (!this.historyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Department Manager History not found");
        }
        this.historyRepository.deleteById(id);
    }
}
