package no.tidly.modules.organization.mapper;

import org.springframework.stereotype.Component;

import no.tidly.modules.organization.domain.DepartmentManagerHistoryEntity;
import no.tidly.modules.organization.dto.DepartmentManagerHistoryResponse;

@Component
public class DepartmentManagerHistoryMapper {

    public DepartmentManagerHistoryResponse toResponse(DepartmentManagerHistoryEntity entity) {
        return new DepartmentManagerHistoryResponse(
                entity.getId(),
                entity.getDepartment().getId(),
                entity.getDepartment().getName(),
                entity.getManager().getId(),
                entity.getManager().getName(),
                entity.getStartDate(),
                entity.getEndDate());
    }
}
