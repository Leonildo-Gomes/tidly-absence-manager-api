package no.tidly.modules.configuration.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import no.tidly.modules.configuration.domain.CompanyAbsenceSettingsEntity;

@Repository
public interface CompanyAbsenceSettingsRepository extends JpaRepository<CompanyAbsenceSettingsEntity, UUID> {
    List<CompanyAbsenceSettingsEntity> findAllByCompanyId(UUID companyId);

    Optional<CompanyAbsenceSettingsEntity> findByIdAndCompanyId(UUID id, UUID companyId);

    boolean existsByIdAndCompanyId(UUID id, UUID companyId);

    Optional<CompanyAbsenceSettingsEntity> findByCompanyIdAndAbsenceTypeId(UUID companyId, UUID absenceTypeId);
}
