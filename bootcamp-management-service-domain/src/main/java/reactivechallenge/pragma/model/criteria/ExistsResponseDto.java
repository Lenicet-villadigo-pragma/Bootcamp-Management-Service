package reactivechallenge.pragma.model.criteria;

public record ExistsResponseDto(
        long id,
        boolean exits,
        String message
) {}