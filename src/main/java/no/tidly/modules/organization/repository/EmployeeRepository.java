package no.tidly.modules.organization.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import no.tidly.modules.organization.domain.EmployeeEntity;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, UUID> {

    List<EmployeeEntity> findAllByCompanyId(UUID companyId);

    Optional<EmployeeEntity> findByUserId(String userId);

    Optional<EmployeeEntity> findByIdAndCompanyId(UUID id, UUID companyId);

    Optional<EmployeeEntity> findByUserIdAndCompanyId(String userId, UUID companyId);

}
