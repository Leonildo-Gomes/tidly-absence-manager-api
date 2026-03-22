package no.tidly.modules.configuration.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CompanyAbsenceSettingsResponse(
                UUID id,
                UUID companyId,
                UUID absenceTypeId,
                String absenceTypeName,
                UUID departmentId,
                BigDecimal maxDaysPerYear,
                Integer minNoticeDays,
                LocalDateTime createdAt,
                LocalDateTime updatedAt) {
}
