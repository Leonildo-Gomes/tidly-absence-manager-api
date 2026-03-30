package no.tidly.modules.workflow.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import no.tidly.modules.workflow.domain.AbsenceRequestEntity;
import no.tidly.modules.workflow.domain.enums.AbsenceStatus;

@Repository
public interface AbsenceRequestRepository extends JpaRepository<AbsenceRequestEntity, UUID> {
    List<AbsenceRequestEntity> findByEmployeeId(UUID employeeId);

    List<AbsenceRequestEntity> findByStatus(AbsenceStatus status);

    List<AbsenceRequestEntity> findByEmployeeIdAndYear(UUID employeeId, Integer year);
}
