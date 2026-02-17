package reactivechallenge.pragma.api;

import reactivechallenge.pragma.model.BootcampModel;
import reactor.core.publisher.Mono;

public interface IRegisterBootcampServicePort {
    Mono<BootcampModel> registerBootcamp(BootcampModel bootcampModel);
}
