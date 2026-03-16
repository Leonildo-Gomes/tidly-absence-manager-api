package no.tidly.modules.organization.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import no.tidly.modules.organization.dto.AssignManagerRequest;
import no.tidly.modules.organization.dto.DepartmentManagerHistoryResponse;
import no.tidly.modules.organization.dto.DepartmentRequest;
import no.tidly.modules.organization.dto.DepartmentResponse;
import no.tidly.modules.organization.dto.TeamResponse;
import no.tidly.modules.organization.usecase.department.AssignDepartmentManagerUseCase;
import no.tidly.modules.organization.usecase.department.CreateDepartmentUseCase;
import no.tidly.modules.organization.usecase.department.DeleteDepartmentUseCase;
import no.tidly.modules.organization.usecase.department.GetAllDepartmentsUseCase;
import no.tidly.modules.organization.usecase.department.GetDepartmentByIdUseCase;
import no.tidly.modules.organization.usecase.department.GetDepartmentManagerHistoryUseCase;
import no.tidly.modules.organization.usecase.department.UpdateDepartmentUseCase;
import no.tidly.modules.organization.usecase.team.GetTeamsByDepartmentIdUseCase;

@RestController
@RequestMapping("/api/v1/departments")
@Tag(name = "Departments", description = "Department management")
@RequiredArgsConstructor
public class DepartmentController {

    private final CreateDepartmentUseCase createDepartmentUseCase;
    private final GetDepartmentByIdUseCase getDepartmentByIdUseCase;
    private final GetAllDepartmentsUseCase getAllDepartmentsUseCase;
    private final UpdateDepartmentUseCase updateDepartmentUseCase;
    private final DeleteDepartmentUseCase deleteDepartmentUseCase;
    private final AssignDepartmentManagerUseCase assignDepartmentManagerUseCase;
    private final GetDepartmentManagerHistoryUseCase getDepartmentManagerHistoryUseCase;
    private final GetTeamsByDepartmentIdUseCase getTeamsByDepartmentIdUseCase;

    @Operation(summary = "Create a new department", description = "Creates a new department with the provided details.")
    @PostMapping
    public ResponseEntity<DepartmentResponse> create(@Valid @RequestBody DepartmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.createDepartmentUseCase.execute(request));
    }

    @Operation(summary = "Get department by ID", description = "Retrieves a department by its unique identifier.")
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(this.getDepartmentByIdUseCase.execute(id));
    }

    @Operation(summary = "Get all departments by company ID", description = "Retrieves a list of all departments for a specific company.")
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<DepartmentResponse>> getAllByCompanyId() {
        return ResponseEntity.ok(this.getAllDepartmentsUseCase.execute());
    }

    @Operation(summary = "Update a department", description = "Updates an existing department with the provided details.")
    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponse> update(@PathVariable UUID id,
            @Valid @RequestBody DepartmentRequest request) {
        return ResponseEntity.ok(this.updateDepartmentUseCase.execute(id, request));
    }

    @Operation(summary = "Delete a department", description = "Deletes a department by its unique identifier.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        this.deleteDepartmentUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Assign a department manager", description = "Assigns a new manager to the department.")
    @PostMapping("/{id}/manager")
    public ResponseEntity<DepartmentManagerHistoryResponse> assignManager(@PathVariable UUID id,
            @Valid @RequestBody AssignManagerRequest request) {
        var history = this.assignDepartmentManagerUseCase.execute(id, request);
        return ResponseEntity.ok(history);
    }

    @Operation(summary = "Get department manager history", description = "Retrieves the history of managers for a specific department.")
    @GetMapping("/{id}/history")
    public ResponseEntity<List<DepartmentManagerHistoryResponse>> getHistory(@PathVariable UUID id) {
        var history = this.getDepartmentManagerHistoryUseCase.execute(id);
        var response = history.stream()
                .map(h -> new DepartmentManagerHistoryResponse(
                        h.getId(),
                        h.getDepartment().getId(),
                        h.getDepartment().getName(),
                        h.getManager().getId(),
                        h.getManager().getName(),
                        h.getStartDate(),
                        h.getEndDate()))
                .toList();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all teams by department ID", description = "Retrieves a list of all teams belonging to a specific department.")
    @GetMapping("/{id}/teams")
    public ResponseEntity<List<TeamResponse>> getTeamsByDepartmentId(@PathVariable UUID id) {
        return ResponseEntity.ok(this.getTeamsByDepartmentIdUseCase.execute(id));
    }
}
