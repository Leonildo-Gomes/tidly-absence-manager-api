package no.tidly.modules.organization.usecase.company;

import java.util.UUID;

import org.springframework.stereotype.Service;

import no.tidly.core.exceptions.ForbiddenAccessException;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.core.security.SecurityContextService;
import no.tidly.modules.organization.domain.CompanyEntity;
import no.tidly.modules.organization.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteCompanyUseCase {

    private final CompanyRepository repository;
    private final SecurityContextService securityContextService;

    public void execute(UUID id) {
        String activeClerkOrgId = securityContextService.getCurrentOrganizationId();

        CompanyEntity company = this.repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        if (activeClerkOrgId == null || !activeClerkOrgId.equals(company.getClerkOrgId())) {
            throw new ForbiddenAccessException("O utilizador não tem permissão para eliminar esta empresa.");
        }

        company.setIsActive(false);

        this.repository.save(company);
    }
}
