package no.tidly.modules.workflow.usecase.absenceapproval;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.modules.organization.domain.EmployeeEntity;
import no.tidly.modules.organization.repository.EmployeeRepository;
import no.tidly.modules.organization.service.TenantService;
import no.tidly.modules.workflow.domain.AbsenceApprovalEntity;
import no.tidly.modules.workflow.domain.AbsenceRequestEntity;
import no.tidly.modules.workflow.dto.AbsenceApprovalRequest;
import no.tidly.modules.workflow.dto.AbsenceApprovalResponse;
import no.tidly.modules.workflow.mapper.AbsenceApprovalMapper;
import no.tidly.modules.workflow.repository.AbsenceApprovalRepository;
import no.tidly.modules.workflow.repository.AbsenceRequestRepository;

@Service
@RequiredArgsConstructor
public class CreateAbsenceApprovalUseCase {

        private final AbsenceApprovalRepository repository;
        private final AbsenceRequestRepository absenceRequestRepository;
        private final EmployeeRepository employeeRepository;
        private final AbsenceApprovalMapper mapper;
        private final TenantService tenantService;

        @Transactional
        public AbsenceApprovalResponse execute(AbsenceApprovalRequest request) {
                var company = this.tenantService.getCurrentCompanyByTenant();
                AbsenceRequestEntity absenceRequest = absenceRequestRepository
                                .findByIdAndCompanyId(request.absenceRequestId(), company.getId())
                                .orElseThrow(() -> new ResourceNotFoundException("Absence request not found"));
                EmployeeEntity approver = employeeRepository.findByIdAndCompanyId(request.approverId(), company.getId())
                                .orElseThrow(() -> new ResourceNotFoundException("Approver not found"));

                AbsenceApprovalEntity entity = AbsenceApprovalEntity.builder()
                                .absenceRequest(absenceRequest)
                                .approver(approver)
                                .status(request.status())
                                .notes(request.notes())
                                .build();
                AbsenceApprovalEntity savedEntity = repository.save(entity);
                return mapper.toResponse(savedEntity);
        }
}
