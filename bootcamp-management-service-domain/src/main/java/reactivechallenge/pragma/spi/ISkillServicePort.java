package reactivechallenge.pragma.spi;

import reactivechallenge.pragma.model.SkillExternalModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ISkillServicePort {
    Mono<Boolean> existsById(List<String> skillIds);
    Flux<SkillExternalModel> getSkillsByIds (List<String> skillIds);
    Mono<Void> deleteSkillById(String skillId);
}
