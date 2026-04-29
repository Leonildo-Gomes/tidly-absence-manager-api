package no.tidly.modules.configuration.usecase.companyabsencesettings;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.modules.configuration.domain.AbsenceTypeEntity;
import no.tidly.modules.configuration.domain.CompanyAbsenceSettingsEntity;
import no.tidly.modules.configuration.dto.CompanyAbsenceSettingsRequest;
import no.tidly.modules.configuration.dto.CompanyAbsenceSettingsResponse;
import no.tidly.modules.configuration.mapper.CompanyAbsenceSettingsMapper;
import no.tidly.modules.configuration.repository.AbsenceTypeRepository;
import no.tidly.modules.configuration.repository.CompanyAbsenceSettingsRepository;
import no.tidly.modules.organization.domain.CompanyEntity;
import no.tidly.modules.organization.service.TenantService;

@Service
@RequiredArgsConstructor
public class CreateCompanyAbsenceSettingsUseCase {

    private final CompanyAbsenceSettingsRepository repository;
    private final CompanyAbsenceSettingsMapper mapper;
    private final AbsenceTypeRepository absenceTypeRepository;
    private final TenantService tenantService;

    @Transactional
    public CompanyAbsenceSettingsResponse execute(CompanyAbsenceSettingsRequest request) {
        CompanyEntity company = this.tenantService.getCurrentCompanyByTenant();

        AbsenceTypeEntity absenceType = absenceTypeRepository.findById(request.absenceTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Absence type not found"));

        CompanyAbsenceSettingsEntity entity = CompanyAbsenceSettingsEntity.builder()
                .company(company)
                .absenceType(absenceType)
                .departmentId(request.departmentId())
                .maxDaysPerYear(request.maxDaysPerYear())
                .minNoticeDays(request.minNoticeDays() != null ? request.minNoticeDays() : 0)
                .build();

        CompanyAbsenceSettingsEntity savedEntity = repository.save(entity);
        return mapper.toResponse(savedEntity);
    }

}
