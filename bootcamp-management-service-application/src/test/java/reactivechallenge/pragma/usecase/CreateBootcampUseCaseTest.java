package reactivechallenge.pragma.usecase;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactivechallenge.pragma.exception.InconsistencyDataException;
import reactivechallenge.pragma.model.BootcampModel;
import reactivechallenge.pragma.model.SkillExternalModel;
import reactivechallenge.pragma.spi.IBootcampRepositoryPort;
import reactivechallenge.pragma.spi.ISkillServicePort;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class CreateBootcampUseCaseTest {
    @Mock
    IBootcampRepositoryPort bootcampRepositoryPort;

    @Mock
    ISkillServicePort skillServicePort;

    @InjectMocks
    CreateBootcampUseCase createBootcampUseCase;

    @Test
    @DisplayName("Create technology successfully")
    void createBootcampSuccess() {
        // Arrange
        List<SkillExternalModel> skillsIds = List.of(
                new SkillExternalModel(1L,"", new ArrayList<>()),
                new SkillExternalModel(2L,"", new ArrayList<>()),
                new SkillExternalModel(3L,"", new ArrayList<>())
        );
        BootcampModel inputModel = new BootcampModel(null,"name", "description",
                LocalDateTime.now().plusHours(1), Duration.ofHours(100), skillsIds);
        BootcampModel returnedModel = new BootcampModel(null,"name", "description",
                LocalDateTime.now(), Duration.ofHours(100), skillsIds);

        Mockito.when(skillServicePort.existsById(Mockito.anyList())).thenReturn(Mono.just(true));
        Mockito.when(bootcampRepositoryPort.save(Mockito.any(BootcampModel.class))).thenReturn(Mono.just(returnedModel));

        // Act
        Mono<BootcampModel> result = createBootcampUseCase.registerBootcamp(inputModel);

        // Assert
        StepVerifier.create(result)
                .expectNext(returnedModel)
                .verifyComplete();

        Mockito.verify(skillServicePort).existsById(Mockito.anyList());
        Mockito.verify(bootcampRepositoryPort).save(inputModel);
    }

    @Test
    @DisplayName("Create skill Throw InconsistencyDataException because tech ids does not exists")
    void createBootcampThrowInconsistencyDataException() {
        // Arrange
        List<SkillExternalModel> skillsIds = List.of(
                new SkillExternalModel(1L,"", new ArrayList<>()),
                new SkillExternalModel(2L,"", new ArrayList<>()),
                new SkillExternalModel(3L,"", new ArrayList<>())
        );
        BootcampModel inputModel = new BootcampModel(null,"name", "description",
                LocalDateTime.now().plusHours(1), Duration.ofHours(100), skillsIds);

        Mockito.when(skillServicePort.existsById(Mockito.anyList())).thenReturn(Mono.just(false));

        // Act
        Mono<BootcampModel> result = createBootcampUseCase.registerBootcamp(inputModel);

        // Assert
        StepVerifier.create(result).expectErrorSatisfies(throwable -> {
            Assertions.assertThat(throwable).isInstanceOf(InconsistencyDataException.class);
            Assertions.assertThat(throwable.getMessage()).isEqualTo("Alguna de las capacidades con ids: [1, 2, 3] no existen");
        }).verify();

        Mockito.verify(skillServicePort).existsById(Mockito.anyList());
        Mockito.verify(bootcampRepositoryPort, Mockito.never()).save(inputModel);
    }

    @Test
    @DisplayName("Create skill Throw InconsistencyDataException because startDate is invalid")
    void createBootcampFailsBecauseInvalidStartDate() {
        // Arrange
        List<SkillExternalModel> skillsIds = List.of(
                new SkillExternalModel(1L,"", new ArrayList<>()),
                new SkillExternalModel(2L,"", new ArrayList<>()),
                new SkillExternalModel(3L,"", new ArrayList<>())
        );
        BootcampModel inputModel = new BootcampModel(null,"name", "description",
                LocalDateTime.now().minusDays(1), Duration.ofHours(100), skillsIds);

        Mockito.when(skillServicePort.existsById(Mockito.anyList())).thenReturn(Mono.just(true));

        // Act
        Mono<BootcampModel> result = createBootcampUseCase.registerBootcamp(inputModel);

        // Assert
        StepVerifier.create(result).expectErrorSatisfies(throwable -> {
            Assertions.assertThat(throwable).isInstanceOf(InconsistencyDataException.class);
            Assertions.assertThat(throwable.getMessage()).isEqualTo("Debe ingresar una fecha de inicio válida.");
        }).verify();

        Mockito.verify(skillServicePort).existsById(Mockito.anyList());
        Mockito.verify(bootcampRepositoryPort, Mockito.never()).save(inputModel);
    }
}
