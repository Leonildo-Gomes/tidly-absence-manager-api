package no.tidly.modules.organization.controllers;

import java.net.URI;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import no.tidly.modules.organization.dto.EmployeeJobTitleRequest;
import no.tidly.modules.organization.dto.EmployeeJobTitleResponse;
import no.tidly.modules.organization.dto.EmployeeRequest;
import no.tidly.modules.organization.dto.EmployeeResponse;
import no.tidly.modules.organization.usecase.employee.AssignJobTitleUseCase;
import no.tidly.modules.organization.usecase.employee.CreateEmployeeUseCase;
import no.tidly.modules.organization.usecase.employee.DeleteEmployeeUseCase;
import no.tidly.modules.organization.usecase.employee.GetAllEmployeesUseCase;
import no.tidly.modules.organization.usecase.employee.GetEmployeeByIdUseCase;
import no.tidly.modules.organization.usecase.employee.GetEmployeeJobTitleHistoryUseCase;
import no.tidly.modules.organization.usecase.employee.UpdateEmployeeUseCase;

@RestController
@RequestMapping("/api/v1/employees")
@Tag(name = "Employees", description = "Employee management")
@RequiredArgsConstructor
public class EmployeeController {

    private final CreateEmployeeUseCase createEmployeeUseCase;
    private final GetEmployeeByIdUseCase getEmployeeByIdUseCase;
    private final GetAllEmployeesUseCase getAllEmployeesUseCase;
    private final UpdateEmployeeUseCase updateEmployeeUseCase;
    private final DeleteEmployeeUseCase deleteEmployeeUseCase;
    private final AssignJobTitleUseCase assignJobTitleUseCase;
    private final GetEmployeeJobTitleHistoryUseCase getEmployeeJobTitleHistoryUseCase;

    @Operation(summary = "Create a new employee", description = "Creates a new employee with the provided details.")
    @PostMapping
    public ResponseEntity<EmployeeResponse> create(@Valid @RequestBody EmployeeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.createEmployeeUseCase.execute(request));
    }

    @Operation(summary = "Get employee by ID", description = "Retrieves an employee by their unique identifier.")
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(this.getEmployeeByIdUseCase.execute(id));
    }

    @Operation(summary = "Get all employees", description = "Retrieves a list of all employees.")
    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAll() {
        return ResponseEntity.ok(this.getAllEmployeesUseCase.execute());
    }

    @Operation(summary = "Update an employee", description = "Updates an existing employee with the provided details.")
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> update(@PathVariable UUID id,
            @Valid @RequestBody EmployeeRequest request) {
        return ResponseEntity.ok(this.updateEmployeeUseCase.execute(id, request));
    }

    @Operation(summary = "Delete an employee", description = "Deletes an employee by their unique identifier.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        this.deleteEmployeeUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Assign a job title", description = "Assigns a job title to an employee.")
    @PostMapping("/{employeeId}/job-titles")
    public ResponseEntity<EmployeeJobTitleResponse> assign(@PathVariable UUID employeeId,
            @Valid @RequestBody EmployeeJobTitleRequest request) {

        var response = assignJobTitleUseCase.execute(employeeId, request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "Get employee job title history", description = "Retrieves the job title history for a specific employee.")
    @GetMapping("/{employeeId}/job-titles/history")
    public ResponseEntity<List<EmployeeJobTitleResponse>> getHistory(@PathVariable UUID employeeId) {
        return ResponseEntity.ok(getEmployeeJobTitleHistoryUseCase.execute(employeeId));
    }
}
