package reactivechallenge.pragma.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public record BootcampModel (
    Long id,
    String name,
    String description,
    LocalDateTime startDate,
    Duration estimatedTime,
    List<Long> skillsIds
){

    public List<String> getSkillIdsAsString() {
        if(skillsIds==null){
            return List.of();
        }

        return skillsIds.stream()
                .map(String::valueOf)
                .toList();
    }
}
