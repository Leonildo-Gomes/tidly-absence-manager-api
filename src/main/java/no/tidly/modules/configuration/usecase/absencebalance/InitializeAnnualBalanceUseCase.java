package no.tidly.modules.configuration.usecase.absencebalance;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.modules.configuration.domain.AbsenceBalanceEntity;
import no.tidly.modules.configuration.domain.BalanceTransactionEntity;
import no.tidly.modules.configuration.domain.CompanyAbsenceSettingsEntity;
import no.tidly.modules.configuration.domain.enums.TransactionType;
import no.tidly.modules.configuration.dto.InitializeAnnualBalanceRequest;
import no.tidly.modules.configuration.dto.InitializeAnnualBalanceResponse;
import no.tidly.modules.configuration.repository.AbsenceBalanceRepository;
import no.tidly.modules.configuration.repository.BalanceTransactionRepository;
import no.tidly.modules.configuration.repository.CompanyAbsenceSettingsRepository;
import no.tidly.modules.organization.domain.CompanyEntity;
import no.tidly.modules.organization.domain.EmployeeEntity;
import no.tidly.modules.organization.repository.EmployeeRepository;
import no.tidly.modules.organization.service.TenantService;

@Service
@RequiredArgsConstructor
public class InitializeAnnualBalanceUseCase {

    private final TenantService tenantService;
    private final EmployeeRepository employeeRepository;
    private final CompanyAbsenceSettingsRepository settingsRepository;
    private final AbsenceBalanceRepository absenceBalanceRepository;
    private final BalanceTransactionRepository transactionRepository;

    @Transactional
    public InitializeAnnualBalanceResponse execute(InitializeAnnualBalanceRequest request) {
        CompanyEntity company = tenantService.getCurrentCompanyByTenant();
        Integer targetYear = request.targetYear();

        List<EmployeeEntity> employees;
        if (request.employeeId() != null) {
            EmployeeEntity employee = employeeRepository.findByIdAndCompanyId(request.employeeId(), company.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
            employees = Collections.singletonList(employee);
        } else {
            employees = employeeRepository.findAllByCompanyId(company.getId());
        }

        CompanyAbsenceSettingsEntity setting = settingsRepository
                .findByCompanyIdAndAbsenceTypeId(company.getId(), request.absenceTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Configuração não encontrada para o tipo de ausência fornecido."));

        int initializedCount = 0;

        for (EmployeeEntity employee : employees) {
                // Check if balance already exists for target year
                boolean exists = absenceBalanceRepository
                        .findByEmployeeIdAndYearAndAbsenceTypeIdAndCompanyId(
                                employee.getId(), targetYear, setting.getAbsenceType().getId(), company.getId())
                        .isPresent();

                if (exists) {
                    continue; // Skip if already initialized
                }

                // Get previous year balance
                AbsenceBalanceEntity prevBalance = absenceBalanceRepository
                        .findByEmployeeIdAndYearAndAbsenceTypeIdAndCompanyId(
                                employee.getId(), targetYear - 1, setting.getAbsenceType().getId(), company.getId())
                        .orElse(null);

                BigDecimal carryover = BigDecimal.ZERO;
                if (prevBalance != null) {
                    // remainingDays = totalEntitled - usedDays
                    BigDecimal totalEntitled = prevBalance.getTotalEntitled() != null ? prevBalance.getTotalEntitled()
                            : BigDecimal.ZERO;
                    BigDecimal usedDays = prevBalance.getUsedDays() != null ? prevBalance.getUsedDays()
                            : BigDecimal.ZERO;
                    carryover = totalEntitled.subtract(usedDays);
                    if (carryover.compareTo(BigDecimal.ZERO) < 0) {
                        carryover = BigDecimal.ZERO; // Prevent negative carryover
                    }
                }

                BigDecimal accrual = setting.getMaxDaysPerYear() != null ? setting.getMaxDaysPerYear()
                        : BigDecimal.ZERO;

                // Create new balance
                AbsenceBalanceEntity newBalance = AbsenceBalanceEntity.builder()
                        .company(company)
                        .employee(employee)
                        .absenceType(setting.getAbsenceType())
                        .year(targetYear)
                        .totalEntitled(carryover.add(accrual))
                        .usedDays(BigDecimal.ZERO)
                        .pendingDays(BigDecimal.ZERO)
                        .build();

                absenceBalanceRepository.save(newBalance);

                // Create carryover transaction if > 0
                if (carryover.compareTo(BigDecimal.ZERO) > 0) {
                    BalanceTransactionEntity carryoverTransaction = BalanceTransactionEntity.builder()
                            .company(company)
                            .employee(employee)
                            .absenceType(setting.getAbsenceType())
                            .year(targetYear)
                            .amount(carryover)
                            .transactionType(TransactionType.CARRYOVER)
                            .description("Saldo transitado de " + (targetYear - 1))
                            .build();
                    transactionRepository.save(carryoverTransaction);
                }

                // Create accrual transaction if > 0
                if (accrual.compareTo(BigDecimal.ZERO) > 0) {
                    BalanceTransactionEntity accrualTransaction = BalanceTransactionEntity.builder()
                            .company(company)
                            .employee(employee)
                            .absenceType(setting.getAbsenceType())
                            .year(targetYear)
                            .amount(accrual)
                            .transactionType(TransactionType.ACCRUAL)
                            .description("Atribuição anual (" + targetYear + ")")
                            .build();
                    transactionRepository.save(accrualTransaction);
                }

            initializedCount++;
        }

        String message = initializedCount > 0
                ? "Foram inicializados saldos para " + initializedCount + " registos."
                : "Nenhum saldo foi inicializado (já existentes ou sem funcionários).";

        return new InitializeAnnualBalanceResponse(message, initializedCount);
    }
}
