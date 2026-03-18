package no.tidly.modules.organization.usecase.employee;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.tidly.modules.organization.dto.EmployeeJobTitleResponse;
import no.tidly.modules.organization.mapper.EmployeeJobTitleMapper;
import no.tidly.modules.organization.repository.EmployeeJobTitleRepository;

@Service
@RequiredArgsConstructor
public class GetEmployeeJobTitleHistoryUseCase {

    private final EmployeeJobTitleRepository repository;
    private final EmployeeJobTitleMapper mapper;

    public List<EmployeeJobTitleResponse> execute(UUID employeeId) {
        return repository.findByEmployeeId(employeeId).stream()
                .map(mapper::toResponse)
                .toList();
    }
}
