package no.tidly.modules.configuration.usecase.companyabsencesettings;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.core.shared.Utils;
import no.tidly.modules.configuration.domain.CompanyAbsenceSettingsEntity;
import no.tidly.modules.configuration.dto.CompanyAbsenceSettingsRequest;
import no.tidly.modules.configuration.dto.CompanyAbsenceSettingsResponse;
import no.tidly.modules.configuration.mapper.CompanyAbsenceSettingsMapper;
import no.tidly.modules.configuration.repository.CompanyAbsenceSettingsRepository;

@Service
@RequiredArgsConstructor
public class UpdateCompanyAbsenceSettingsUseCase {

    private final CompanyAbsenceSettingsRepository repository;
    private final CompanyAbsenceSettingsMapper mapper;

    @Transactional
    public CompanyAbsenceSettingsResponse execute(UUID id, CompanyAbsenceSettingsRequest request) {
        CompanyAbsenceSettingsEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CompanyAbsenceSettings not found with id: " + id));

        Utils.copyNonNullProperties(request, entity);

        CompanyAbsenceSettingsEntity updatedEntity = repository.save(entity);
        return mapper.toResponse(updatedEntity);
    }
}
