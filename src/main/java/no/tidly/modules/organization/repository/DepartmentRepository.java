package no.tidly.modules.organization.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import no.tidly.modules.organization.domain.DepartmentEntity;

public interface DepartmentRepository extends JpaRepository<DepartmentEntity, UUID> {
    List<DepartmentEntity> findAllByCompanyId(UUID companyId);

    Optional<DepartmentEntity> findByIdAndCompanyId(UUID id, UUID companyId);
}
