package reactivechallenge.pragma.input.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import reactivechallenge.pragma.model.BootcampModel;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public record CreateBootcampRequestDto (
        @NotNull @NotBlank @Size(max = 50, message = "El nombre debe tener máximo 50 caracteres", min = 1) String name,
        @Size(max = 90, message = "la descripcion debe tener máximo 90 caracteres", min = 1) String description,
        @NotNull LocalDateTime startDate,
        @NotNull @Min(1L) Long estimatedTimeInHours,
        @NotNull @NotBlank.List({}) List<Long> skillsIds
){
    public BootcampModel toModel() {
        return new BootcampModel(null, this.name, this.description, this.startDate
                , Duration.ofHours(this.estimatedTimeInHours), this.skillsIds);
    }
}
