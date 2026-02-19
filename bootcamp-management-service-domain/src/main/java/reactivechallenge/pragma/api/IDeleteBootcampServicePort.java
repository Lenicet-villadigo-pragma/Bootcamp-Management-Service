package reactivechallenge.pragma.api;

import reactor.core.publisher.Mono;

import java.util.List;

public interface IDeleteBootcampServicePort {
    Mono<Void> deleteBootcampsByIds(List<Long> ids);
    List<Long> verifyBootcampIds(String bootcampIdsAsString);
}
