package no.tidly.modules.configuration.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record AbsenceBalanceRequest(
                @NotNull UUID employeeId,
                @NotNull UUID absenceTypeId,
                @NotNull Integer year,
                @NotNull @DecimalMin("0.0") BigDecimal totalEntitled,
                @DecimalMin("0.0") BigDecimal usedDays,
                @DecimalMin("0.0") BigDecimal pendingDays) {
}
