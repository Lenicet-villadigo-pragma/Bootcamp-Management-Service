package reactivechallenge.pragma.input.dto;

import reactivechallenge.pragma.model.BootcampModel;

import java.util.List;

public record ListAllResponseDto (
        Long id,
        String name,
        List<ListAllSkillResponseDto> skills
){
    public static ListAllResponseDto fromModel(BootcampModel model) {
        return new ListAllResponseDto(model.id(), model.name()
                ,model.skillExternalModels().stream()
                .map(skillExternalModel ->
                        new ListAllSkillResponseDto(skillExternalModel.id(), skillExternalModel.name(),
                        skillExternalModel.technologyIds().stream()
                                .map(ListAllTechsResponseDto::fromModel)
                                .toList())
                ).toList()
        );
    }
}
