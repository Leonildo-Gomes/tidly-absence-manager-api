package no.tidly.modules.organization.dto;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record DepartmentManagerHistoryRequest(
        @NotNull(message = "Department ID is required")
        UUID departmentId,
        
        @NotNull(message = "Manager ID is required")
        UUID managerId,
        
        @NotNull(message = "Start date is required")
        LocalDate startDate,
        
        LocalDate endDate
) {}
