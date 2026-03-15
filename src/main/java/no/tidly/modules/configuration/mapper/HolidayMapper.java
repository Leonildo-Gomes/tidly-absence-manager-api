package no.tidly.modules.configuration.mapper;

import org.springframework.stereotype.Component;

import no.tidly.modules.configuration.domain.HolidayEntity;
import no.tidly.modules.configuration.dto.CreateHolidayRequest;
import no.tidly.modules.configuration.dto.HolidayResponse;
import no.tidly.modules.configuration.domain.enums.HolidayType;

@Component
public class HolidayMapper {

    public HolidayEntity toEntity(CreateHolidayRequest request) {
        if (request == null) {
            return null;
        }
        return HolidayEntity.builder()
                .companyId(request.companyId())
                .date(request.date())
                .name(request.name())
                .type(request.type() != null ? request.type() : HolidayType.PUBLIC)
                .isRecurring(request.isRecurring() != null ? request.isRecurring() : false)
                .isActive(request.isActive() != null ? request.isActive() : true)
                .build();
    }

    public HolidayResponse toResponse(HolidayEntity entity) {
        if (entity == null) {
            return null;
        }
        return new HolidayResponse(
                entity.getId(),
                entity.getCompanyId(),
                entity.getDate(),
                entity.getName(),
                entity.getType(),
                entity.getIsRecurring(),
                entity.getIsActive());
    }
}
