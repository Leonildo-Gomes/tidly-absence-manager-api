package no.tidly.modules.organization.usecase.team;

import org.springframework.stereotype.Service;

import no.tidly.core.exceptions.ResourceNotFoundException;
import no.tidly.modules.organization.domain.DepartmentEntity;
import no.tidly.modules.organization.domain.TeamEntity;
import no.tidly.modules.organization.dto.TeamRequest;
import no.tidly.modules.organization.dto.TeamResponse;
import no.tidly.modules.organization.mapper.TeamMapper;
import no.tidly.modules.organization.repository.DepartmentRepository;
import no.tidly.modules.organization.repository.TeamRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateTeamUseCase {

    private final TeamRepository teamRepository;
    private final DepartmentRepository departmentRepository;
    private final TeamMapper teamMapper;

    public TeamResponse execute(TeamRequest request) {
        DepartmentEntity department = this.departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        var team = TeamEntity.builder()
                .name(request.name())
                .department(department)
                .code(request.code())
                .build();
        var savedEntity = this.teamRepository.save(team);
        return this.teamMapper.toResponse(savedEntity);
    }
}
