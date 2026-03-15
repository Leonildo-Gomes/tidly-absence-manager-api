package no.tidly.modules.configuration.dto;

import java.time.LocalDate;
import java.util.UUID;

import no.tidly.modules.configuration.domain.enums.HolidayType;

public record UpdateHolidayRequest(
        UUID companyId,
        LocalDate date,
        String name,
        HolidayType type,
        Boolean isRecurring,
        Boolean isActive) {
}
