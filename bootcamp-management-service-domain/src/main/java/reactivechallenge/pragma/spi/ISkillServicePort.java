package reactivechallenge.pragma.spi;

import reactor.core.publisher.Mono;

import java.util.List;

public interface ISkillServicePort {
    Mono<Boolean> existsById(List<String> skillIds);
}
