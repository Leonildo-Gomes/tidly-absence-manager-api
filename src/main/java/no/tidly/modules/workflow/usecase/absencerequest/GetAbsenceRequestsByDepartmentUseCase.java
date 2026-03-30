package no.tidly.modules.workflow.usecase.absencerequest;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import no.tidly.modules.workflow.dto.AbsenceRequestResponse;
import no.tidly.modules.workflow.mapper.AbsenceRequestMapper;
import no.tidly.modules.workflow.repository.AbsenceRequestRepository;

@Service
@RequiredArgsConstructor
public class GetAbsenceRequestsByDepartmentUseCase {

    private final AbsenceRequestRepository repository;
    private final AbsenceRequestMapper mapper;

    @Transactional(readOnly = true)
    public List<AbsenceRequestResponse> execute(UUID departmentId, Integer year) {
        var entities = repository.findAllByDepartmentIdAndYear(departmentId, year);
        return entities.stream()
                .map(mapper::toResponse)
                .toList();
    }
}
