package no.tidly.modules.configuration.usecase.holiday;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.core.security.SecurityContextService;
import no.tidly.modules.configuration.dto.HolidayResponse;
import no.tidly.modules.configuration.mapper.HolidayMapper;
import no.tidly.modules.configuration.repository.HolidayRepository;
import no.tidly.modules.organization.domain.CompanyEntity;
import no.tidly.modules.organization.repository.CompanyRepository;

@Service
@RequiredArgsConstructor
public class GetAllHolidaysUseCase {

    private final HolidayRepository repository;
    private final HolidayMapper mapper;
    private final SecurityContextService securityContextService;
    private final CompanyRepository companyRepository;

    @Transactional(readOnly = true)
    public List<HolidayResponse> execute() {
        String activeClerkOrgId = securityContextService.getCurrentOrganizationId();
        if (activeClerkOrgId == null) {
            return List.of();
        }
        CompanyEntity company = this.companyRepository.findByClerkOrgId(activeClerkOrgId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
        return repository.findAllByCompanyId(company.getId()).stream()
                .map(mapper::toResponse)
                .toList();
    }
}
