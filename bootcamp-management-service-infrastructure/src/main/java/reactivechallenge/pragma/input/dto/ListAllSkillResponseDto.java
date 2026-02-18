package reactivechallenge.pragma.input.dto;

import reactivechallenge.pragma.model.SkillExternalModel;

import java.util.List;

public record ListAllSkillResponseDto(
        Long id,
        String name,
        List<ListAllTechsResponseDto> techs
) {
    public static ListAllSkillResponseDto fromModel(SkillExternalModel model) {
        return new ListAllSkillResponseDto(model.id(), model.name()
                , model.technologyIds().stream().map(ListAllTechsResponseDto::fromModel).toList());
    }
}
