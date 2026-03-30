package no.tidly.modules.organization.usecase.department;

import java.util.List;

import org.springframework.stereotype.Service;

import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.core.security.SecurityContextService;
import no.tidly.modules.organization.domain.CompanyEntity;
import no.tidly.modules.organization.dto.DepartmentResponse;
import no.tidly.modules.organization.mapper.DepartmentMapper;
import no.tidly.modules.organization.repository.CompanyRepository;
import no.tidly.modules.organization.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetAllDepartmentsUseCase {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final SecurityContextService securityContextService;
    private final CompanyRepository companyRepository;

    public List<DepartmentResponse> execute() {
        String activeClerkOrgId = securityContextService.getCurrentOrganizationId();
        if (activeClerkOrgId == null) {
            return List.of();
        }
        CompanyEntity company = this.companyRepository.findByClerkOrgId(activeClerkOrgId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        return this.departmentRepository.findAllByCompanyId(company.getId()).stream()
                .map(this.departmentMapper::toResponse)
                .toList();
    }
}
