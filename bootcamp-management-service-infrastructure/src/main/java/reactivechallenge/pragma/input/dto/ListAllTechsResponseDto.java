package reactivechallenge.pragma.input.dto;

import reactivechallenge.pragma.model.TechExternalModel;

public record ListAllTechsResponseDto (
        Long id,
        String name
){
    public static ListAllTechsResponseDto fromModel(TechExternalModel model) {
        return new ListAllTechsResponseDto(model.id(), model.name());
    }
}
