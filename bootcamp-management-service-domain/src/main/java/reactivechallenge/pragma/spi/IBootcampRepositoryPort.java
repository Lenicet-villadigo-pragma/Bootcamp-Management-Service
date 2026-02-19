package reactivechallenge.pragma.spi;

import reactivechallenge.pragma.model.BootcampModel;
import reactivechallenge.pragma.model.criteria.SortField;
import reactivechallenge.pragma.model.criteria.SortOrder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IBootcampRepositoryPort {
    Mono<BootcampModel> save(BootcampModel bootcampModel);
    Flux<BootcampModel> getBootcamps(SortField sortField, SortOrder sortOrder
            , Integer pageNumber, Integer pageSize);
    Mono<Long> countBootcamps();
    Mono<Void> deleteBootcampsByIds(List<Long> ids);
    Flux<Long> getSkillsIdsByBootcampId(Long bootcampId);
    Mono<Void> deleteBootcampSkillRelation(Long bootcampId);
    Mono<Long> getTotalSkillRelationWithBootcamps(Long skillId);
}
