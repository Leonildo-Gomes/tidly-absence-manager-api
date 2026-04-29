package no.tidly.modules.configuration.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import no.tidly.modules.configuration.domain.HolidayEntity;

@Repository
public interface HolidayRepository extends JpaRepository<HolidayEntity, UUID> {
    List<HolidayEntity> findAllByCompanyId(UUID companyId);

    @Query("SELECT h FROM HolidayEntity h WHERE h.company.id = :companyId OR h.company.id IS NULL")
    List<HolidayEntity> findAllByCompanyIdOrGlobal(UUID companyId);

    Optional<HolidayEntity> findByIdAndCompanyId(UUID id, UUID companyId);

    boolean existsByIdAndCompanyId(UUID id, UUID companyId);
}
