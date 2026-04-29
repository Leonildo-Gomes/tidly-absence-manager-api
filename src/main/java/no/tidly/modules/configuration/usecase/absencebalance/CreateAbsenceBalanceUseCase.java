package no.tidly.modules.configuration.usecase.absencebalance;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.modules.configuration.domain.AbsenceBalanceEntity;
import no.tidly.modules.configuration.domain.AbsenceTypeEntity;
import no.tidly.modules.configuration.dto.AbsenceBalanceRequest;
import no.tidly.modules.configuration.dto.AbsenceBalanceResponse;
import no.tidly.modules.configuration.mapper.AbsenceBalanceMapper;
import no.tidly.modules.configuration.repository.AbsenceBalanceRepository;
import no.tidly.modules.configuration.repository.AbsenceTypeRepository;
import no.tidly.modules.organization.domain.EmployeeEntity;
import no.tidly.modules.organization.repository.EmployeeRepository;
import no.tidly.modules.organization.service.TenantService;

@Service
@RequiredArgsConstructor
public class CreateAbsenceBalanceUseCase {

        private final AbsenceBalanceRepository repository;
        private final EmployeeRepository employeeRepository;
        private final AbsenceTypeRepository absenceTypeRepository;
        private final TenantService tenantService;
        private final AbsenceBalanceMapper mapper;

        @Transactional
        public AbsenceBalanceResponse execute(AbsenceBalanceRequest request) {
                var company = this.tenantService.getCurrentCompanyByTenant();
                EmployeeEntity employee = employeeRepository
                                .findByUserIdAndCompanyId(request.employeeId(), company.getId())
                                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

                AbsenceTypeEntity absenceType = absenceTypeRepository.findById(request.absenceTypeId())
                                .orElseThrow(() -> new ResourceNotFoundException("Absence type not found"));

                AbsenceBalanceEntity entity = AbsenceBalanceEntity.builder()
                                .employee(employee)
                                .absenceType(absenceType)
                                .year(request.year())
                                .totalEntitled(request.totalEntitled())
                                .usedDays(request.usedDays() != null ? request.usedDays() : BigDecimal.ZERO)
                                .pendingDays(request.pendingDays() != null ? request.pendingDays() : BigDecimal.ZERO)
                                .build();

                AbsenceBalanceEntity savedEntity = repository.save(entity);
                return mapper.toResponse(savedEntity);
        }

}
