package reactivechallenge.pragma.mapper;

import org.springframework.stereotype.Component;
import reactivechallenge.pragma.model.BootcampModel;
import reactivechallenge.pragma.out.entity.BootcampEntity;

import java.time.Duration;
import java.util.List;

@Component
public class BootcampEntityMapper {
    public BootcampModel toModel(BootcampEntity entity, List<Long> technologyExternalModelList) {
        if (entity == null) {
            return null;
        }
        return new BootcampModel(
                entity.id(),
                entity.name(),
                entity.description(),
                entity.startDay(),
                Duration.ofHours(entity.estimatedTime()),
                technologyExternalModelList
        );
    }

    public BootcampEntity toEntity(BootcampModel model) {
        if (model == null) {
            return null;
        }
        return new BootcampEntity(
                model.id(),
                model.name(),
                model.description(),
                model.startDate(),
                model.estimatedTime().toHours(),
                model.skillsIds().size()
        );
    }
}
