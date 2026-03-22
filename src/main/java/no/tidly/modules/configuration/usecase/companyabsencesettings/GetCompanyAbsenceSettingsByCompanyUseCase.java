package no.tidly.modules.configuration.usecase.companyabsencesettings;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.core.security.SecurityContextService;
import no.tidly.modules.configuration.dto.CompanyAbsenceSettingsResponse;
import no.tidly.modules.configuration.mapper.CompanyAbsenceSettingsMapper;
import no.tidly.modules.configuration.repository.CompanyAbsenceSettingsRepository;
import no.tidly.modules.organization.domain.CompanyEntity;
import no.tidly.modules.organization.repository.CompanyRepository;

@Service
@RequiredArgsConstructor
public class GetCompanyAbsenceSettingsByCompanyUseCase {

    private final CompanyAbsenceSettingsRepository repository;
    private final CompanyRepository companyRepository;
    private final SecurityContextService securityContextService;
    private final CompanyAbsenceSettingsMapper mapper;

    @Transactional(readOnly = true)
    public List<CompanyAbsenceSettingsResponse> execute() {
        String activeClerkOrgId = securityContextService.getCurrentOrganizationId();
        if (activeClerkOrgId == null) {
            return List.of();
        }
        CompanyEntity company = this.companyRepository.findByClerkOrgId(activeClerkOrgId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
        return repository.findByCompanyId(company.getId()).stream()
                .map(mapper::toResponse)
                .toList();
    }

}
