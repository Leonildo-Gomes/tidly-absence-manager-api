package no.tidly.modules.workflow.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import no.tidly.modules.workflow.domain.AbsenceApprovalEntity;

@Repository
public interface AbsenceApprovalRepository extends JpaRepository<AbsenceApprovalEntity, UUID> {
    List<AbsenceApprovalEntity> findByAbsenceRequestId(UUID absenceRequestId);

    List<AbsenceApprovalEntity> findByApproverId(UUID approverId);

    @Query("SELECT aa FROM AbsenceApprovalEntity aa "
            + "JOIN aa.absenceRequest ar "
            + "JOIN ar.employee e "
            + "WHERE aa.absenceRequestId = :absenceRequestId "
            + "AND e.company.id = :companyId")
    List<AbsenceApprovalEntity> findByAbsenceRequestIdAndCompanyId(@Param("absenceRequestId") UUID absenceRequestId,
            @Param("companyId") UUID companyId);
}
