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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import no.tidly.modules.configuration.dto.AbsenceBalanceRequest;
import no.tidly.modules.configuration.dto.AbsenceBalanceResponse;
import no.tidly.modules.configuration.usecase.absencebalance.CreateAbsenceBalanceUseCase;
import no.tidly.modules.configuration.usecase.absencebalance.DeleteAbsenceBalanceUseCase;
import no.tidly.modules.configuration.usecase.absencebalance.GetAbsenceBalanceByIdUseCase;
import no.tidly.modules.configuration.usecase.absencebalance.GetAbsenceBalancesByEmployeeUseCase;
import no.tidly.modules.configuration.usecase.absencebalance.InitializeAnnualBalanceUseCase;
import no.tidly.modules.configuration.usecase.absencebalance.UpdateAbsenceBalanceUseCase;
import no.tidly.modules.configuration.dto.InitializeAnnualBalanceRequest;
import no.tidly.modules.configuration.dto.InitializeAnnualBalanceResponse;

@RestController
@RequestMapping("/api/v1/absence-balances")
@RequiredArgsConstructor
@Tag(name = "Absence Balances", description = "Absence balance management")
public class AbsenceBalanceController {

    private final CreateAbsenceBalanceUseCase createAbsenceBalanceUseCase;
    private final GetAbsenceBalanceByIdUseCase getAbsenceBalanceByIdUseCase;
    private final GetAbsenceBalancesByEmployeeUseCase getAbsenceBalancesByEmployeeUseCase;
    private final UpdateAbsenceBalanceUseCase updateAbsenceBalanceUseCase;
    private final DeleteAbsenceBalanceUseCase deleteAbsenceBalanceUseCase;
    private final InitializeAnnualBalanceUseCase initializeAnnualBalanceUseCase;

    @Operation(summary = "Create an absence balance", description = "Creates a new absence balance for an employee.")
    @PostMapping
    public ResponseEntity<AbsenceBalanceResponse> create(@Valid @RequestBody AbsenceBalanceRequest request) {
        AbsenceBalanceResponse response = createAbsenceBalanceUseCase.execute(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Initialize annual balances", description = "Initializes the absence balances for a specific year, calculating carryover from the previous year.")
    @PostMapping("/initialize")
    public ResponseEntity<InitializeAnnualBalanceResponse> initializeAnnualBalances(
            @Valid @RequestBody InitializeAnnualBalanceRequest request) {
        return ResponseEntity.ok(initializeAnnualBalanceUseCase.execute(request));
    }

    @Operation(summary = "Get absence balance by ID", description = "Retrieves an absence balance by its unique identifier.")
    @GetMapping("/{id}")
    public ResponseEntity<AbsenceBalanceResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(getAbsenceBalanceByIdUseCase.execute(id));
    }

    @Operation(summary = "Get absence balances by employee", description = "Retrieves a list of absence balances for a specific employee.")
    @GetMapping
    public ResponseEntity<List<AbsenceBalanceResponse>> getByEmployee(@RequestParam UUID employeeId) {
        return ResponseEntity.ok(getAbsenceBalancesByEmployeeUseCase.execute(employeeId));
    }

    @Operation(summary = "Update an absence balance", description = "Updates an existing absence balance with the provided details.")
    @PutMapping("/{id}")
    public ResponseEntity<AbsenceBalanceResponse> update(@PathVariable UUID id,
            @Valid @RequestBody AbsenceBalanceRequest request) {
        return ResponseEntity.ok(updateAbsenceBalanceUseCase.execute(id, request));
    }

    @Operation(summary = "Delete an absence balance", description = "Deletes an absence balance by its unique identifier.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteAbsenceBalanceUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
