package reactivechallenge.pragma.model.criteria;

import java.util.List;

public record PaginationResult<T>(
        List<T> items,
        long total
) {}
