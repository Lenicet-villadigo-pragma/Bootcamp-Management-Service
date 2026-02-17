package reactivechallenge.pragma.input.dto;

import reactivechallenge.pragma.model.BootcampModel;

public record CreateBootcampResponseDto (
        Long id,
        String name
){
    public static CreateBootcampResponseDto fromModel(BootcampModel model) {
        return new CreateBootcampResponseDto(model.id(), model.name());
    }
}
