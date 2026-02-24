package reactivechallenge.pragma.input.dto;

import reactivechallenge.pragma.model.BootcampModel;

import java.time.Duration;
import java.time.LocalDateTime;

public record BootcampDto(
        Long id,
        String name,
        String description,
        LocalDateTime startDate,
        Duration estimatedTime
) {
    public static BootcampDto fromModel(BootcampModel model) {
        return new BootcampDto(model.id(), model.name(), model.description()
        , model.startDate(), model.estimatedTime());
    }
}
