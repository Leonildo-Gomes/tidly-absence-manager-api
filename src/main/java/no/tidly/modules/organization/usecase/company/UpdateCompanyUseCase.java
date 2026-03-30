package no.tidly.modules.organization.usecase.company;

import java.util.UUID;

import org.springframework.stereotype.Service;

import no.tidly.core.exceptions.ForbiddenAccessException;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.core.security.SecurityContextService;
import no.tidly.core.shared.Utils;
import no.tidly.modules.organization.dto.CompanyResponse;
import no.tidly.modules.organization.dto.UpdateCompanyRequest;
import no.tidly.modules.organization.mapper.CompanyMapper;
import no.tidly.modules.organization.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateCompanyUseCase {

    private final CompanyRepository companyRepository;
    private final CompanyMapper mapper;
    private final SecurityContextService securityContextService;

    public CompanyResponse execute(UUID id, UpdateCompanyRequest request) {
        String activeClerkOrgId = securityContextService.getCurrentOrganizationId();

        var company = this.companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        if (activeClerkOrgId == null || !activeClerkOrgId.equals(company.getClerkOrgId())) {
            throw new ForbiddenAccessException("O utilizador não tem permissão para atualizar esta empresa.");
        }
        System.out.println("request:"+request);

        Utils.copyNonNullProperties(request, company);
        var updatedEntity = this.companyRepository.save(company);
        return this.mapper.toResponse(updatedEntity);
    }
}
