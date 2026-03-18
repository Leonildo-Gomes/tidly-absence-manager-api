package no.tidly.modules.organization.dto;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record AssignLeaderRequest(
        @NotNull(message = "Leader ID is required") UUID leaderId,
        @NotNull(message = "Start date is required") LocalDate startDate) {
}
