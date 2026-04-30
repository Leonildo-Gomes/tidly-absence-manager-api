package no.tidly.modules.configuration.usecase.balancetransaction;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.modules.configuration.domain.AbsenceTypeEntity;
import no.tidly.modules.configuration.domain.BalanceTransactionEntity;
import no.tidly.modules.configuration.dto.BalanceTransactionRequest;
import no.tidly.modules.configuration.dto.BalanceTransactionResponse;
import no.tidly.modules.configuration.mapper.BalanceTransactionMapper;
import no.tidly.modules.configuration.repository.AbsenceTypeRepository;
import no.tidly.modules.configuration.repository.BalanceTransactionRepository;
import no.tidly.modules.organization.domain.EmployeeEntity;
import no.tidly.modules.organization.repository.EmployeeRepository;
import no.tidly.modules.organization.service.TenantService;

@Service
@RequiredArgsConstructor
public class CreateBalanceTransactionUseCase {

        private final BalanceTransactionRepository repository;
        private final BalanceTransactionMapper mapper;
        private final EmployeeRepository employeeRepository;
        private final AbsenceTypeRepository absenceTypeRepository;
        private final TenantService tenantService;

        @Transactional
        public BalanceTransactionResponse execute(BalanceTransactionRequest request) {
                var company = this.tenantService.getCurrentCompanyByTenant();
                EmployeeEntity employee = employeeRepository.findByIdAndCompanyId(request.employeeId(), company.getId())
                                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

                AbsenceTypeEntity absenceType = absenceTypeRepository
                                .findByIdAndCompanyId(request.absenceTypeId(), company.getId())
                                .orElseThrow(() -> new ResourceNotFoundException("Absence type not found"));

                BalanceTransactionEntity entity = BalanceTransactionEntity.builder()
                                .employee(employee)
                                .company(company)
                                .absenceType(absenceType)
                                .year(request.year())
                                .amount(request.amount())
                                .transactionType(request.transactionType())
                                .description(request.description())
                                .createdBy(request.createdBy())
                                .build();

                BalanceTransactionEntity savedEntity = repository.save(entity);
                return mapper.toResponse(savedEntity);
        }

}
