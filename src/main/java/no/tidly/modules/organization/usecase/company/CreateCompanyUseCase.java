package no.tidly.modules.organization.usecase.company;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.OrgNumberFoundException;
import no.tidly.modules.organization.domain.CompanyEntity;
import no.tidly.modules.organization.dto.CompanyRequest;
import no.tidly.modules.organization.dto.CompanyResponse;
import no.tidly.modules.organization.mapper.CompanyMapper;
import no.tidly.modules.organization.repository.CompanyRepository;

@Service
@RequiredArgsConstructor
public class CreateCompanyUseCase {

    private final CompanyRepository companyRepository;
    private final CompanyMapper mapper;

    public CompanyResponse execute(CompanyRequest request) {
        System.out.println(" Criando company:" + request);
        this.companyRepository.findByOrgNumber(request.organizationNumber())
                .ifPresent(company -> {
                    throw new OrgNumberFoundException(request.organizationNumber());
                });
        if (request.name() == null) {
            throw new IllegalArgumentException("Company name cannot be null");
        }
        if (request.organizationNumber() == null) {
            throw new IllegalArgumentException("Organization number cannot be null");
        }

        var company = CompanyEntity.builder()
                .name(request.name())
                .orgNumber(request.organizationNumber())
                .clerkOrgId(request.clerkOrgId())
                .build();
        var savedEntity = this.companyRepository.save(company);
        return this.mapper.toResponse(savedEntity);
    }
}
