package no.tidly.modules.organization.usecase.employee;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.tidly.modules.organization.dto.EmployeeResponse;
import no.tidly.modules.organization.mapper.EmployeeMapper;
import no.tidly.modules.organization.service.TenantService;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetMyProfileUseCase {

    private final TenantService tenantService;
    private final EmployeeMapper mapper;

    public EmployeeResponse execute() {
        var employee = tenantService.getCurrentEmployee();
        log.info("Fetching profile for userId={}", employee.getUserId());
        return mapper.toResponse(employee);
    }
}
