package no.tidly.modules.workflow.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import no.tidly.modules.workflow.domain.AbsenceRequestEntity;
import no.tidly.modules.workflow.domain.enums.AbsenceStatus;

@Repository
public interface AbsenceRequestRepository extends JpaRepository<AbsenceRequestEntity, UUID> {
    List<AbsenceRequestEntity> findByEmployeeId(UUID employeeId);

    List<AbsenceRequestEntity> findByStatus(AbsenceStatus status);

    List<AbsenceRequestEntity> findByEmployeeIdAndYearAndCompanyId(UUID employeeId, Integer year, UUID companyId);

    @Query("SELECT ar FROM AbsenceRequestEntity ar JOIN ar.employee e WHERE e.team.id = :teamId AND ar.year = :year AND ar.company.id = :companyId")
    List<AbsenceRequestEntity> findAllByTeamIdAndYearAndCompanyId(@Param("teamId") UUID teamId,
            @Param("year") Integer year, @Param("companyId") UUID companyId);

    @Query("SELECT ar FROM AbsenceRequestEntity ar JOIN ar.employee e WHERE e.team.department.id = :departmentId AND ar.year = :year AND ar.company.id = :companyId")
    List<AbsenceRequestEntity> findAllByDepartmentIdAndYearAndCompanyId(@Param("departmentId") UUID departmentId,
            @Param("year") Integer year, @Param("companyId") UUID companyId);

    Optional<AbsenceRequestEntity> findByIdAndCompanyId(UUID id, UUID companyId);

    boolean existsByIdAndCompanyId(UUID id, UUID companyId);
}
