package no.tidly.modules.configuration.dto;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import no.tidly.modules.configuration.domain.enums.HolidayType;

public record CreateHolidayRequest(
                UUID companyId,
                @NotNull LocalDate date,
                @NotNull @Size(max = 100) String name,
                HolidayType type,
                @NotNull Boolean isRecurring,
                @NotNull Boolean isActive) {
}
