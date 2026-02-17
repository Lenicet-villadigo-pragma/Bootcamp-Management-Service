package reactivechallenge.pragma.out.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactivechallenge.pragma.out.entity.BootcampEntity;

public interface IBootcampRepository extends R2dbcRepository<BootcampEntity, Long> {
}
