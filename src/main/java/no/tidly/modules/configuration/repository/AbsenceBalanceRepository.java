package no.tidly.modules.configuration.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import no.tidly.modules.configuration.domain.AbsenceBalanceEntity;

@Repository
public interface AbsenceBalanceRepository extends JpaRepository<AbsenceBalanceEntity, UUID> {
        List<AbsenceBalanceEntity> findByEmployeeId(UUID employeeId);

        Optional<AbsenceBalanceEntity> findByEmployeeIdAndYearAndAbsenceTypeId(UUID employeeId, Integer year,
                        UUID absenceTypeId);

        @Query("SELECT ab FROM AbsenceBalanceEntity ab WHERE ab.employee.company.id= :companyId AND ab.employee.id = :employeeId")
        List<AbsenceBalanceEntity> findByEmployeeIdAndCompanyId(@Param("employeeId") UUID employeeId,
                        @Param("companyId") UUID companyId);
}
