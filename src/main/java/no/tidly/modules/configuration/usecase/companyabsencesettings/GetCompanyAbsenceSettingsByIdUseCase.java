package no.tidly.modules.configuration.usecase.companyabsencesettings;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.modules.configuration.domain.CompanyAbsenceSettingsEntity;
import no.tidly.modules.configuration.dto.CompanyAbsenceSettingsResponse;
import no.tidly.modules.configuration.mapper.CompanyAbsenceSettingsMapper;
import no.tidly.modules.configuration.repository.CompanyAbsenceSettingsRepository;

@Service
@RequiredArgsConstructor
public class GetCompanyAbsenceSettingsByIdUseCase {

    private final CompanyAbsenceSettingsRepository repository;
    private final CompanyAbsenceSettingsMapper mapper;

    @Transactional(readOnly = true)
    public CompanyAbsenceSettingsResponse execute(UUID id) {
        CompanyAbsenceSettingsEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CompanyAbsenceSettings not found with id: " + id));

        return mapper.toResponse(entity);
    }

}
