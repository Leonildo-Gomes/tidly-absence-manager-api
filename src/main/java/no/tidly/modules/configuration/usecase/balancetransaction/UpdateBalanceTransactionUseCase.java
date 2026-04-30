package no.tidly.modules.configuration.usecase.balancetransaction;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.core.shared.Utils;
import no.tidly.modules.configuration.domain.BalanceTransactionEntity;
import no.tidly.modules.configuration.dto.BalanceTransactionRequest;
import no.tidly.modules.configuration.dto.BalanceTransactionResponse;
import no.tidly.modules.configuration.mapper.BalanceTransactionMapper;
import no.tidly.modules.configuration.repository.BalanceTransactionRepository;
import no.tidly.modules.organization.service.TenantService;

@Service
@RequiredArgsConstructor
public class UpdateBalanceTransactionUseCase {

    private final BalanceTransactionRepository repository;
    private final BalanceTransactionMapper mapper;
    private final TenantService tenantService;

    @Transactional
    public BalanceTransactionResponse execute(UUID id, BalanceTransactionRequest request) {
        var company = this.tenantService.getCurrentCompanyByTenant();
        BalanceTransactionEntity entity = repository.findByIdAndCompanyId(id, company.getId())
                .orElseThrow(() -> new ResourceNotFoundException("BalanceTransaction not found with id: " + id));

        Utils.copyNonNullProperties(request, entity);

        BalanceTransactionEntity updatedEntity = repository.save(entity);
        return mapper.toResponse(updatedEntity);
    }
}
