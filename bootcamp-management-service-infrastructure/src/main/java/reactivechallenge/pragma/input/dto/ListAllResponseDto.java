package reactivechallenge.pragma.input.dto;

import java.util.List;

public record ListAllResponseDto (
        Long id,
        String name,
        List<ListAllSkillResponseDto> skills
){}
