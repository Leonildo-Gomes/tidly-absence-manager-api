package no.tidly.modules.organization.repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import no.tidly.modules.organization.domain.DepartmentManagerHistoryEntity;

@Repository
public interface DepartmentManagerHistoryRepository
        extends JpaRepository<DepartmentManagerHistoryEntity, UUID> {

    @Query("SELECT h FROM DepartmentManagerHistoryEntity h WHERE h.department.id = :departmentId AND h.endDate IS NULL")
    Optional<DepartmentManagerHistoryEntity> findActiveByDepartmentId(UUID departmentId);

    List<DepartmentManagerHistoryEntity> findByDepartmentIdOrderByStartDateDesc(UUID departmentId);
}
