package no.tidly.modules.configuration.usecase.companyabsencesettings;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.core.security.SecurityContextService;
import no.tidly.modules.configuration.domain.CompanyAbsenceSettingsEntity;
import no.tidly.modules.configuration.dto.CompanyAbsenceSettingsRequest;
import no.tidly.modules.configuration.dto.CompanyAbsenceSettingsResponse;
import no.tidly.modules.configuration.mapper.CompanyAbsenceSettingsMapper;
import no.tidly.modules.configuration.repository.CompanyAbsenceSettingsRepository;

@Service
@RequiredArgsConstructor
public class CreateCompanyAbsenceSettingsUseCase {

    private final CompanyAbsenceSettingsRepository repository;
    private final CompanyAbsenceSettingsMapper mapper;
    private final SecurityContextService securityContextService;

    @Transactional
    public CompanyAbsenceSettingsResponse execute(CompanyAbsenceSettingsRequest request) {
        String activeClerkOrgId = securityContextService.getCurrentOrganizationId();
        if (activeClerkOrgId == null) {
            throw new ResourceNotFoundException("Company not found");
        }
        CompanyAbsenceSettingsEntity entity = CompanyAbsenceSettingsEntity.builder()
                .companyId(request.companyId())
                .absenceTypeId(request.absenceTypeId())
                .departmentId(request.departmentId())
                .maxDaysPerYear(request.maxDaysPerYear())
                .minNoticeDays(request.minNoticeDays() != null ? request.minNoticeDays() : 0)
                .build();

        CompanyAbsenceSettingsEntity savedEntity = repository.save(entity);
        return mapper.toResponse(savedEntity);
    }

}
