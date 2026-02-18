package reactivechallenge.pragma.out.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactivechallenge.pragma.out.entity.BootcampEntity;
import reactor.core.publisher.Flux;

public interface IBootcampRepository extends R2dbcRepository<BootcampEntity, Long> {
    Flux<BootcampEntity> findAllBy(Pageable pageable);
}
