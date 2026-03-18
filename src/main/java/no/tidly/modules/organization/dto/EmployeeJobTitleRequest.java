package no.tidly.modules.organization.dto;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record EmployeeJobTitleRequest(
                @NotNull UUID jobTitleId,
                @NotNull LocalDate startDate,
                LocalDate endDate) {
}
