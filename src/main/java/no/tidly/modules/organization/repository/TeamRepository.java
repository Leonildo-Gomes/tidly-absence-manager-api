package no.tidly.modules.organization.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import no.tidly.modules.organization.domain.TeamEntity;

public interface TeamRepository extends JpaRepository<TeamEntity, UUID> {

    List<TeamEntity> findAllByDepartmentId(UUID departmentId);

    List<TeamEntity> findAllByCompanyId(UUID companyId);

    Optional<TeamEntity> findByIdAndCompanyId(UUID id, UUID companyId);

}
