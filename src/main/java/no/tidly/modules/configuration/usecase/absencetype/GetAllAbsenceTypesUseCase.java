package no.tidly.modules.configuration.usecase.absencetype;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import no.tidly.modules.configuration.dto.AbsenceTypeResponse;
import no.tidly.modules.configuration.mapper.AbsenceTypeMapper;
import no.tidly.modules.configuration.repository.AbsenceTypeRepository;

@Service
@RequiredArgsConstructor
public class GetAllAbsenceTypesUseCase {

    private final AbsenceTypeRepository absenceTypeRepository;
    private final AbsenceTypeMapper mapper;

    @Transactional(readOnly = true)
    public List<AbsenceTypeResponse> execute() {
        return absenceTypeRepository.findAll().stream()
                .map(mapper::toResponse).toList();
    }
}
