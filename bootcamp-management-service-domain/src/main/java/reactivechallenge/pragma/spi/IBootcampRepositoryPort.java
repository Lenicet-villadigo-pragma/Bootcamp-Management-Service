package reactivechallenge.pragma.spi;

import reactivechallenge.pragma.model.BootcampModel;
import reactor.core.publisher.Mono;

public interface IBootcampRepositoryPort {
    Mono<BootcampModel> save(BootcampModel bootcampModel);
}
