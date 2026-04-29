package no.tidly.modules.configuration.usecase.holiday;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import no.tidly.modules.configuration.dto.HolidayResponse;
import no.tidly.modules.configuration.mapper.HolidayMapper;
import no.tidly.modules.configuration.repository.HolidayRepository;
import no.tidly.modules.organization.domain.CompanyEntity;
import no.tidly.modules.organization.service.TenantService;

@Service
@RequiredArgsConstructor
public class GetAllHolidaysUseCase {

    private final HolidayRepository repository;
    private final HolidayMapper mapper;
    private final TenantService tenantService;

    @Transactional(readOnly = true)
    public List<HolidayResponse> execute() {
        CompanyEntity company = this.tenantService.getCurrentCompanyByTenant();
        return repository.findAllByCompanyIdOrGlobal(company.getId()).stream()
                .map(mapper::toResponse)
                .toList();
    }
}
