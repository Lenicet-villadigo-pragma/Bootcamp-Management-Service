package reactivechallenge.pragma.input.dto;

import java.util.List;

public record PaginatedDto<T>(
        List<T> content,
        long totalElements,
        int pageNumber,
        int pageSize
) {}
