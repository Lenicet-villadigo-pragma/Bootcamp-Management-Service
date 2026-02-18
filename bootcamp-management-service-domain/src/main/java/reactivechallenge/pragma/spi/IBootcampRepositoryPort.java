package reactivechallenge.pragma.spi;

import reactivechallenge.pragma.model.BootcampModel;
import reactivechallenge.pragma.model.criteria.SortField;
import reactivechallenge.pragma.model.criteria.SortOrder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IBootcampRepositoryPort {
    Mono<BootcampModel> save(BootcampModel bootcampModel);
    Flux<BootcampModel> getBootcamps(SortField sortField, SortOrder sortOrder
            , Integer pageNumber, Integer pageSize);
    Mono<Long> countBootcamps();
}
