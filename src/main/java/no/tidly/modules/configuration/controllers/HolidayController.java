package no.tidly.modules.configuration.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import no.tidly.modules.configuration.dto.HolidayRequest;
import no.tidly.modules.configuration.dto.HolidayResponse;
import no.tidly.modules.configuration.dto.UpdateHolidayRequest;
import no.tidly.modules.configuration.usecase.holiday.CreateHolidayUseCase;
import no.tidly.modules.configuration.usecase.holiday.DeleteHolidayUseCase;
import no.tidly.modules.configuration.usecase.holiday.GetAllHolidaysUseCase;
import no.tidly.modules.configuration.usecase.holiday.GetHolidayByIdUseCase;
import no.tidly.modules.configuration.usecase.holiday.UpdateHolidayUseCase;

@RestController
@RequestMapping("/api/v1/holidays")
@RequiredArgsConstructor
@Tag(name = "Holidays", description = "Holiday management")
public class HolidayController {

    private final CreateHolidayUseCase createHolidayUseCase;
    private final GetHolidayByIdUseCase getHolidayByIdUseCase;
    private final GetAllHolidaysUseCase getAllHolidaysUseCase;
    private final UpdateHolidayUseCase updateHolidayUseCase;
    private final DeleteHolidayUseCase deleteHolidayUseCase;

    @Operation(summary = "Create a new holiday", description = "Creates a new holiday with the provided details.")
    @PostMapping
    public ResponseEntity<HolidayResponse> createHoliday(@Valid @RequestBody HolidayRequest request) {
        HolidayResponse response = createHolidayUseCase.execute(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get holiday by ID", description = "Retrieves a holiday by its unique identifier.")
    @GetMapping("/{id}")
    public ResponseEntity<HolidayResponse> getHolidayById(@PathVariable UUID id) {
        HolidayResponse response = getHolidayByIdUseCase.execute(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all holidays", description = "Retrieves a list of all holidays, optionally filtered by company ID.")
    @GetMapping
    public ResponseEntity<List<HolidayResponse>> getAllHolidays() {
        List<HolidayResponse> response = getAllHolidaysUseCase.execute();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update a holiday", description = "Updates an existing holiday with the provided details.")
    @PutMapping("/{id}")
    public ResponseEntity<HolidayResponse> updateHoliday(
            @PathVariable UUID id,
            @RequestBody UpdateHolidayRequest request) {
        HolidayResponse response = updateHolidayUseCase.execute(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a holiday", description = "Deletes a holiday by its unique identifier.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHoliday(@PathVariable UUID id) {
        deleteHolidayUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
