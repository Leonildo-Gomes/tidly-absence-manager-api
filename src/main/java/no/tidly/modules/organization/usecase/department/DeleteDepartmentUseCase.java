package no.tidly.modules.organization.usecase.department;

import java.util.UUID;

import org.springframework.stereotype.Service;

import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.modules.organization.domain.DepartmentEntity;
import no.tidly.modules.organization.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteDepartmentUseCase {

    private final DepartmentRepository repository;

    public void execute(UUID id) {
        DepartmentEntity department = this.repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        department.setIsActive(false);
        
        this.repository.save(department);
    }
}
