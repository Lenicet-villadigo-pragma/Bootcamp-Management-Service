package reactivechallenge.pragma.model;

import java.util.List;

public record SkillExternalModel(
        Long id,
        String name,
        List<TechExternalModel> technologyIds
) {}
