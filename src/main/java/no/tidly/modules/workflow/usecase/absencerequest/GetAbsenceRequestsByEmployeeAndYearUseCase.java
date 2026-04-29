package no.tidly.modules.workflow.usecase.absencerequest;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import no.tidly.modules.organization.service.TenantService;
import no.tidly.modules.workflow.dto.AbsenceRequestResponse;
import no.tidly.modules.workflow.mapper.AbsenceRequestMapper;
import no.tidly.modules.workflow.repository.AbsenceRequestRepository;

@Service
@RequiredArgsConstructor
public class GetAbsenceRequestsByEmployeeAndYearUseCase {

    private final AbsenceRequestRepository repository;
    private final AbsenceRequestMapper mapper;
    private final TenantService tenantService;

    @Transactional(readOnly = true)
    public List<AbsenceRequestResponse> execute(Integer year) {
        var employee = tenantService.getCurrentEmployee();
        var entities = repository.findByEmployeeIdAndYearAndCompanyId(
                employee.getId(), year, employee.getCompany().getId());
        return entities.stream()
                .map(mapper::toResponse)
                .toList();
    }
}
