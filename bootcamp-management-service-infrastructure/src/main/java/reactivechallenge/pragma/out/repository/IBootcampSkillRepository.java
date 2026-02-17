package reactivechallenge.pragma.out.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactivechallenge.pragma.out.entity.BootcampSkillEntity;

public interface IBootcampSkillRepository  extends ReactiveCrudRepository<BootcampSkillEntity, Long> {
}
