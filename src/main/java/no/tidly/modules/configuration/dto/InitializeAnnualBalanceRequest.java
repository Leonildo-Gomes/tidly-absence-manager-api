package no.tidly.modules.configuration.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record InitializeAnnualBalanceRequest(
        @NotNull Integer targetYear,
        @NotNull UUID absenceTypeId,
        UUID employeeId) {
}
