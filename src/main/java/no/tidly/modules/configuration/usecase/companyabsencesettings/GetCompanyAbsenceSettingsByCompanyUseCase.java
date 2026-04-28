package no.tidly.modules.configuration.usecase.companyabsencesettings;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import no.tidly.modules.configuration.dto.CompanyAbsenceSettingsResponse;
import no.tidly.modules.configuration.mapper.CompanyAbsenceSettingsMapper;
import no.tidly.modules.configuration.repository.CompanyAbsenceSettingsRepository;
import no.tidly.modules.organization.domain.CompanyEntity;
import no.tidly.modules.organization.service.TenantService;

@Service
@RequiredArgsConstructor
public class GetCompanyAbsenceSettingsByCompanyUseCase {

    private final CompanyAbsenceSettingsRepository repository;
    private final TenantService tenantService;
    private final CompanyAbsenceSettingsMapper mapper;

    @Transactional(readOnly = true)
    public List<CompanyAbsenceSettingsResponse> execute() {
        CompanyEntity company = this.tenantService.getCurrentCompanyByTenant();
        return repository.findAllByCompanyId(company.getId()).stream()
                .map(mapper::toResponse)
                .toList();
    }

}
