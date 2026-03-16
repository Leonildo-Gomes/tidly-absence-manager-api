package no.tidly.modules.organization.controllers;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import no.tidly.modules.organization.dto.DepartmentManagerHistoryRequest;
import no.tidly.modules.organization.dto.DepartmentManagerHistoryResponse;
import no.tidly.modules.organization.dto.UpdateDepartmentManagerHistoryRequest;
import no.tidly.modules.organization.usecase.department.CreateDepartmentManagerHistoryUseCase;
import no.tidly.modules.organization.usecase.department.DeleteDepartmentManagerHistoryUseCase;
import no.tidly.modules.organization.usecase.department.UpdateDepartmentManagerHistoryUseCase;

@RestController
@RequestMapping("/api/v1/department-manager-histories")
@Tag(name = "Department Manager History", description = "CRUD for advanced department manager historical records")
public class DepartmentManagerHistoryController {

    private final CreateDepartmentManagerHistoryUseCase createUseCase;
    private final UpdateDepartmentManagerHistoryUseCase updateUseCase;
    private final DeleteDepartmentManagerHistoryUseCase deleteUseCase;

    public DepartmentManagerHistoryController(CreateDepartmentManagerHistoryUseCase createUseCase,
            UpdateDepartmentManagerHistoryUseCase updateUseCase,
            DeleteDepartmentManagerHistoryUseCase deleteUseCase) {
        this.createUseCase = createUseCase;
        this.updateUseCase = updateUseCase;
        this.deleteUseCase = deleteUseCase;
    }

    @Operation(summary = "Create department manager history manually", description = "Creates a new history record for an arbitrary time period.")
    @PostMapping
    public ResponseEntity<DepartmentManagerHistoryResponse> create(@Valid @RequestBody DepartmentManagerHistoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.createUseCase.execute(request));
    }

    @Operation(summary = "Update a department manager history", description = "Updates an existing manager history record.")
    @PutMapping("/{id}")
    public ResponseEntity<DepartmentManagerHistoryResponse> update(@PathVariable UUID id,
            @Valid @RequestBody UpdateDepartmentManagerHistoryRequest request) {
        return ResponseEntity.ok(this.updateUseCase.execute(id, request));
    }

    @Operation(summary = "Delete a department manager history", description = "Deletes a manager history record permanently.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        this.deleteUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
