package no.tidly.modules.organization.controllers;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
import no.tidly.modules.organization.usecase.employeejobtitle.AssignJobTitleUseCase;
import no.tidly.modules.organization.usecase.employeejobtitle.GetEmployeeJobTitleHistoryUseCase;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Employee Job Titles", description = "Employee job title management")
public class EmployeeJobTitleController {

    private final AssignJobTitleUseCase assignJobTitleUseCase;
    private final GetEmployeeJobTitleHistoryUseCase getEmployeeJobTitleHistoryUseCase;

    @Operation(summary = "Assign a job title", description = "Assigns a job title to an employee.")
    @PostMapping("/employees/{employeeId}/job-titles")
    public ResponseEntity<EmployeeJobTitleResponse> assign(@PathVariable UUID employeeId,
            @Valid @RequestBody EmployeeJobTitleRequest request) {
        if (!employeeId.equals(request.employeeId())) {
            return ResponseEntity.badRequest().build();
        }

        var response = assignJobTitleUseCase.execute(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "Get employee job title history", description = "Retrieves the job title history for a specific employee.")
    @GetMapping("/employees/{employeeId}/job-titles/history")
    public ResponseEntity<List<EmployeeJobTitleResponse>> getHistory(@PathVariable UUID employeeId) {
        return ResponseEntity.ok(getEmployeeJobTitleHistoryUseCase.execute(employeeId));
    }
}
