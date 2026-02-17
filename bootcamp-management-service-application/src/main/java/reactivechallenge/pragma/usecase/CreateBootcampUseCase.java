package reactivechallenge.pragma.usecase;

import lombok.extern.slf4j.Slf4j;
import reactivechallenge.pragma.api.IRegisterBootcampServicePort;
import reactivechallenge.pragma.exception.BusinessDomainException;
import reactivechallenge.pragma.exception.InconsistencyDataException;
import reactivechallenge.pragma.model.BootcampModel;
import reactivechallenge.pragma.spi.IBootcampRepositoryPort;
import reactivechallenge.pragma.spi.ISkillServicePort;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
public class CreateBootcampUseCase implements IRegisterBootcampServicePort {

    private final IBootcampRepositoryPort bootcampRepository;
    private final ISkillServicePort skillServicePort;

    public CreateBootcampUseCase(IBootcampRepositoryPort bootcampRepository, ISkillServicePort skillServicePort) {
        this.bootcampRepository = bootcampRepository;
        this.skillServicePort = skillServicePort;
    }

    @Override
    public Mono<BootcampModel> registerBootcamp(BootcampModel bootcampModel) {
        return skillServicePort.existsById(bootcampModel.getSkillIdsAsString())
                .doOnNext(exists -> log.info("Exiten las capacidades enviadas? {}", exists))
                .flatMap(exists -> {
                    if (Boolean.FALSE.equals(exists)) {
                        log.error("Alguna de las capacidades con ids: {} no existen", bootcampModel.getSkillIdsAsString());
                        return Mono.error(new InconsistencyDataException(
                                String.format("Alguna de las capacidades con ids: %s no existen", bootcampModel.getSkillIdsAsString())
                        ));
                    }

                    if(bootcampModel.startDate()==null || bootcampModel.startDate().isBefore(LocalDateTime.now())) {
                      return Mono.error(new InconsistencyDataException("Debe ingresar una fecha de inicio válida."));
                    }
                    return bootcampRepository.save(bootcampModel);
                });
    }
}
