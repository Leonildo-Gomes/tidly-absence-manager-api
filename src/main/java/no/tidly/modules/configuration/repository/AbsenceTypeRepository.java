package no.tidly.modules.configuration.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import no.tidly.modules.configuration.domain.AbsenceTypeEntity;

@Repository
public interface AbsenceTypeRepository extends JpaRepository<AbsenceTypeEntity, UUID> {

    @Query("SELECT at FROM AbsenceTypeEntity at "
            + "JOIN CompanyAbsenceSettingsEntity cas ON cas.absenceType.id = at.id "
            + "WHERE at.id = :id AND cas.company.id = :companyId")
    Optional<AbsenceTypeEntity> findByIdAndCompanyId(@Param("id") UUID id, @Param("companyId") UUID companyId);
}
