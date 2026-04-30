package no.tidly.modules.configuration.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import no.tidly.modules.configuration.domain.AbsenceBalanceEntity;

@Repository
public interface AbsenceBalanceRepository extends JpaRepository<AbsenceBalanceEntity, UUID> {
        List<AbsenceBalanceEntity> findByEmployeeId(UUID employeeId);

        Optional<AbsenceBalanceEntity> findByEmployeeIdAndYearAndAbsenceTypeId(UUID employeeId, Integer year,
                        UUID absenceTypeId);

        List<AbsenceBalanceEntity> findByEmployeeIdAndCompanyId(UUID employeeId, UUID companyId);

        boolean existsByIdAndCompanyId(UUID id, UUID id2);

        Optional<AbsenceBalanceEntity> findByIdAndCompanyId(UUID id, UUID id2);

        Optional<AbsenceBalanceEntity> findByEmployeeIdAndYearAndAbsenceTypeIdAndCompanyId(UUID employeeId,
                        Integer year, UUID absenceTypeId, UUID companyId);

        List<AbsenceBalanceEntity> findByEmployeeIdAndCompanyIdAndYear(UUID employeeId, UUID companyId, Integer year);
}
