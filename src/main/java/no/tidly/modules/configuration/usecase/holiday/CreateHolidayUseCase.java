package no.tidly.modules.configuration.usecase.holiday;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.core.security.SecurityContextService;
import no.tidly.modules.configuration.domain.HolidayEntity;
import no.tidly.modules.configuration.dto.HolidayRequest;
import no.tidly.modules.configuration.dto.HolidayResponse;
import no.tidly.modules.configuration.mapper.HolidayMapper;
import no.tidly.modules.configuration.repository.HolidayRepository;
import no.tidly.modules.organization.domain.CompanyEntity;
import no.tidly.modules.organization.repository.CompanyRepository;

@Service
@RequiredArgsConstructor
public class CreateHolidayUseCase {

    private final HolidayRepository repository;
    private final HolidayMapper mapper;
    private final SecurityContextService securityContextService;
    private final CompanyRepository companyRepository;

    @Transactional
    public HolidayResponse execute(HolidayRequest request) {
        String activeClerkOrgId = securityContextService.getCurrentOrganizationId();
        if (activeClerkOrgId == null) {
            throw new ResourceNotFoundException("Company not found");
        }
        CompanyEntity company = this.companyRepository.findByClerkOrgId(activeClerkOrgId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        HolidayEntity entity = mapper.toEntity(request, company.getId());
        HolidayEntity savedEntity = repository.save(entity);
        return mapper.toResponse(savedEntity);
    }
}
