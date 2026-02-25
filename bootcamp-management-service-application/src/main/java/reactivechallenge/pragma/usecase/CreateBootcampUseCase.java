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
        return validateBootcamp(bootcampModel)
                .then(
                        skillServicePort.existsById(bootcampModel.getSkillIdsAsString())
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
                            })
                );
    }

    private Mono<Void> validateBootcamp(BootcampModel bootcampModel) {
        if (bootcampModel == null) {
            return Mono.error(new BusinessDomainException("El bootcamp no puede ser nulo"));
        }
        if (bootcampModel.name() == null || bootcampModel.name().trim().isEmpty()) {
            return Mono.error(new BusinessDomainException("El nombre del bootcamp no puede estar vacío"));
        }
        if (bootcampModel.skillsIds() == null || bootcampModel.skillsIds().isEmpty() || bootcampModel.skillsIds().size() > 4) {
            return Mono.error(new BusinessDomainException("La lista de capacidades no puede ser nula, vacía o contener más de 4."));
        }
        long uniqueSkillsCount = bootcampModel.skillsIds().stream()
                .distinct()
                .count();
        if (uniqueSkillsCount < bootcampModel.skillsIds().size()) {
            return Mono.error(new BusinessDomainException("La lista de capacidades no puede contener elementos duplicados"));
        }
        if(bootcampModel.startDate()==null || bootcampModel.startDate().isBefore(LocalDateTime.now())) {
            return Mono.error(new InconsistencyDataException("Debe ingresar una fecha de inicio válida."));
        }
        if(bootcampModel.estimatedTime()==null || bootcampModel.estimatedTime().toHours() == 0){
            return Mono.error(new BusinessDomainException("EL bootcamp debe tener duración de al menos 1 hora"));
        }

        return Mono.empty();
    }
}
