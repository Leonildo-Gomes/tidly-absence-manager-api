package no.tidly.modules.workflow.usecase.absencerequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.core.security.SecurityContextService;
import no.tidly.modules.configuration.domain.AbsenceTypeEntity;
import no.tidly.modules.configuration.repository.AbsenceTypeRepository;
import no.tidly.modules.organization.domain.EmployeeEntity;
import no.tidly.modules.organization.repository.EmployeeRepository;
import no.tidly.modules.workflow.domain.AbsenceRequestEntity;
import no.tidly.modules.workflow.dto.AbsenceRequestRequest;
import no.tidly.modules.workflow.dto.AbsenceRequestResponse;
import no.tidly.modules.workflow.mapper.AbsenceRequestMapper;
import no.tidly.modules.workflow.repository.AbsenceRequestRepository;

@Service
@RequiredArgsConstructor
public class CreateAbsenceRequestUseCase {

        private final AbsenceRequestRepository repository;
        private final AbsenceRequestMapper mapper;
        private final EmployeeRepository employeeRepository;
        private final AbsenceTypeRepository absenceTypeRepository;
        private final SecurityContextService securityContextService;

        @Transactional
        public AbsenceRequestResponse execute(AbsenceRequestRequest request) {
                String clerkUserId = this.securityContextService.getCurrentUserId();
                if (clerkUserId == null || clerkUserId.isBlank()) {
                        throw new ResourceNotFoundException("Clerk user not found");
                }

                EmployeeEntity employee = this.employeeRepository.findByUserId(clerkUserId)
                                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
                AbsenceTypeEntity absenceType = this.absenceTypeRepository.findById(request.absenceTypeId())
                                .orElseThrow(() -> new ResourceNotFoundException("Absence type not found"));

                AbsenceRequestEntity entity = AbsenceRequestEntity.builder()
                                .employee(employee)
                                .absenceType(absenceType)
                                .year(request.year())
                                .startDate(request.startDate())
                                .endDate(request.endDate())
                                .totalDays(request.totalDays())
                                .comment(request.comment())
                                .attachmentPath(request.attachmentPath())
                                .build();
                AbsenceRequestEntity savedEntity = repository.save(entity);
                return mapper.toResponse(savedEntity);
        }
}
