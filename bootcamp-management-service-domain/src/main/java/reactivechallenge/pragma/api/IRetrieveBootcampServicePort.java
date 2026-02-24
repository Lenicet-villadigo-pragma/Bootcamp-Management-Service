package reactivechallenge.pragma.api;

import reactivechallenge.pragma.model.BootcampModel;
import reactivechallenge.pragma.model.criteria.ExistsResponseDto;
import reactivechallenge.pragma.model.criteria.PaginationResult;
import reactivechallenge.pragma.model.criteria.SortField;
import reactivechallenge.pragma.model.criteria.SortOrder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IRetrieveBootcampServicePort {
    Mono<PaginationResult<BootcampModel>> retrieveBootcamps(SortField sortField, SortOrder sortOrder
            , Integer pageNumber, Integer pageSize);
    Flux<ExistsResponseDto> existsBootcampsByIds(List<Long> bootcampIds);
}
