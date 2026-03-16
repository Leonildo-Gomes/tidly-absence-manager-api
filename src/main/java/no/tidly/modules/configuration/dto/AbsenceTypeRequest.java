package no.tidly.modules.configuration.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AbsenceTypeRequest(
                @NotBlank(message = "Name is required") @Size(max = 100, message = "Name must be less than 100 characters") String name,
                String description,
                @NotNull(message = "requiresAttachment is required") Boolean requiresAttachment,
                @Size(max = 50, message = "Code must be less than 50 characters") String code,
                @NotNull(message = "color is required") @Size(max = 7, message = "Color must be less than 7 characters") String color,
                @NotNull(message = "isActive is required") Boolean isActive) {
}
