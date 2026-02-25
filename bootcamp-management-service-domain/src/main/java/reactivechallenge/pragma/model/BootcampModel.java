package reactivechallenge.pragma.model;

import reactivechallenge.pragma.exception.BusinessDomainException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public record BootcampModel (
    Long id,
    String name,
    String description,
    LocalDateTime startDate,
    Duration estimatedTime,
    List<SkillExternalModel> skillExternalModels
){

    public List<String> getSkillIdsAsString() {
        if(skillExternalModels==null){
            return List.of();
        }

        return skillExternalModels.stream()
                .map(SkillExternalModel::id)
                .map(String::valueOf)
                .toList();
    }
}
