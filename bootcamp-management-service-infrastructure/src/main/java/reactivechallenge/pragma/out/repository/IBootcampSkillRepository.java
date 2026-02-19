package reactivechallenge.pragma.out.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactivechallenge.pragma.out.entity.BootcampSkillEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IBootcampSkillRepository  extends ReactiveCrudRepository<BootcampSkillEntity, Long> {
    Flux<BootcampSkillEntity> findAllByBootcampId(Long bootcampId);
    Mono<Long> countBySkillId(Long skillId);
    Mono<Void> deleteByBootcampId(Long bootcampId);
}
