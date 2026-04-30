package no.tidly.modules.configuration.usecase.balancetransaction;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.modules.configuration.repository.BalanceTransactionRepository;
import no.tidly.modules.organization.service.TenantService;

@Service
@RequiredArgsConstructor
public class DeleteBalanceTransactionUseCase {

    private final BalanceTransactionRepository repository;
    private final TenantService tenantService;

    @Transactional
    public void execute(UUID id) {
        var company = this.tenantService.getCurrentCompanyByTenant();
        if (!repository.existsByIdAndCompanyId(id, company.getId())) {
            throw new ResourceNotFoundException("BalanceTransaction not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
