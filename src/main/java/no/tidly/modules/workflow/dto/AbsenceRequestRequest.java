package no.tidly.modules.workflow.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record AbsenceRequestRequest(
                @NotNull UUID absenceTypeId,
                @NotNull Integer year,
                @NotNull LocalDate startDate,
                @NotNull LocalDate endDate,
                @NotNull BigDecimal totalDays,
                String comment,
                String attachmentPath) {
}
