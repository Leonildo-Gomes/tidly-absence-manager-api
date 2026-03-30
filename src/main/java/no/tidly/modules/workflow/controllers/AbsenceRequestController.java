package no.tidly.modules.workflow.controllers;

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
import no.tidly.modules.workflow.dto.AbsenceRequestRequest;
import no.tidly.modules.workflow.dto.AbsenceRequestResponse;
import no.tidly.modules.workflow.usecase.absencerequest.CreateAbsenceRequestUseCase;
import no.tidly.modules.workflow.usecase.absencerequest.DeleteAbsenceRequestUseCase;
import no.tidly.modules.workflow.usecase.absencerequest.GetAbsenceRequestByIdUseCase;
import no.tidly.modules.workflow.usecase.absencerequest.GetAbsenceRequestsByEmployeeAndYearUseCase;
import no.tidly.modules.workflow.usecase.absencerequest.UpdateAbsenceRequestUseCase;

@RestController
@RequestMapping("/api/v1/absence-requests")
@RequiredArgsConstructor
@Tag(name = "Absence Requests", description = "Absence request management")
public class AbsenceRequestController {

    private final CreateAbsenceRequestUseCase createUseCase;
    private final GetAbsenceRequestByIdUseCase getByIdUseCase;
    private final GetAbsenceRequestsByEmployeeAndYearUseCase getByEmployeeAndYearUseCase;
    private final UpdateAbsenceRequestUseCase updateUseCase;
    private final DeleteAbsenceRequestUseCase deleteUseCase;

    @Operation(summary = "Create an absence request", description = "Creates a new absence request.")
    @PostMapping
    public ResponseEntity<AbsenceRequestResponse> create(@RequestBody @Valid AbsenceRequestRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.createUseCase.execute(request));
    }

    @Operation(summary = "Get absence request by ID", description = "Retrieves an absence request by its unique identifier.")
    @GetMapping("/{id}")
    public ResponseEntity<AbsenceRequestResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(this.getByIdUseCase.execute(id));
    }

    @Operation(summary = "Get absence requests by employee and year", description = "Returns all absence requests for a given employee in a specific year.")
    @GetMapping("/employee/year/{year}")
    public ResponseEntity<List<AbsenceRequestResponse>> getByEmployeeAndYear(@PathVariable Integer year) {
        return ResponseEntity.ok(this.getByEmployeeAndYearUseCase.execute(year));
    }

    @Operation(summary = "Update an absence request", description = "Updates an existing absence request with the provided details.")
    @PutMapping("/{id}")
    public ResponseEntity<AbsenceRequestResponse> update(@PathVariable UUID id,
            @RequestBody @Valid AbsenceRequestRequest request) {
        return ResponseEntity.ok(this.updateUseCase.execute(id, request));
    }

    @Operation(summary = "Delete an absence request", description = "Deletes an absence request by its unique identifier.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        this.deleteUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
