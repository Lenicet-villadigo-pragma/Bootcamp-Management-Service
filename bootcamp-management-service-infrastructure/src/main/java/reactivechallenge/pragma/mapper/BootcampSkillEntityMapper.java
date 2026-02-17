package reactivechallenge.pragma.mapper;

import org.springframework.stereotype.Component;
import reactivechallenge.pragma.model.BootcampModel;
import reactivechallenge.pragma.out.entity.BootcampSkillEntity;

@Component
public class BootcampSkillEntityMapper {
    public BootcampSkillEntity toEntity(BootcampModel model, Long skillId) {
        if (model == null || skillId == null) {
            return null;
        }
        return new BootcampSkillEntity(
                model.id(),
                skillId
        );
    }
}
