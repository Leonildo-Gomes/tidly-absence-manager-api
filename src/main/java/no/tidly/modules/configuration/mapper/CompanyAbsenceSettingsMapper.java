package no.tidly.modules.configuration.mapper;

import org.springframework.stereotype.Component;

import no.tidly.modules.configuration.domain.CompanyAbsenceSettingsEntity;
import no.tidly.modules.configuration.dto.CompanyAbsenceSettingsResponse;

@Component
public class CompanyAbsenceSettingsMapper {

    public CompanyAbsenceSettingsResponse toResponse(CompanyAbsenceSettingsEntity entity) {
        return new CompanyAbsenceSettingsResponse(
                entity.getId(),
                entity.getCompany().getId(),
                entity.getAbsenceType().getId(),
                entity.getAbsenceType().getName(),
                entity.getDepartmentId(),
                entity.getMaxDaysPerYear(),
                entity.getMinNoticeDays(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
