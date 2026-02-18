package reactivechallenge.pragma.out.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactivechallenge.pragma.mapper.BootcampEntityMapper;
import reactivechallenge.pragma.mapper.BootcampSkillEntityMapper;
import reactivechallenge.pragma.mapper.DatabaseErrorMapper;
import reactivechallenge.pragma.model.BootcampModel;
import reactivechallenge.pragma.out.entity.BootcampEntity;
import reactivechallenge.pragma.out.entity.BootcampSkillEntity;
import reactivechallenge.pragma.out.repository.IBootcampRepository;
import reactivechallenge.pragma.out.repository.IBootcampSkillRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BootcampRepositoryImplTest {

    @Mock
    IBootcampRepository bootcampRepository;

    @Mock
    IBootcampSkillRepository bootcampSkillRepository;

    BootcampEntityMapper bEntityMapper;
    BootcampSkillEntityMapper bsEntityMapper;
    DatabaseErrorMapper databaseErrorMapper;

    private BootcampRepositoryImpl bootcampRepositoryImpl;

    @BeforeEach
    void setup() {
        databaseErrorMapper = new DatabaseErrorMapper();
        bEntityMapper = new BootcampEntityMapper();
        bsEntityMapper = new BootcampSkillEntityMapper();
        bootcampRepositoryImpl = new BootcampRepositoryImpl(bootcampRepository, bEntityMapper, bsEntityMapper
        , bootcampSkillRepository, databaseErrorMapper);
    }

    @Test
    @DisplayName("Save bootcamp successfully")
    void saveBootcampSuccess() {
        // Arrange
        LocalDateTime fecha = LocalDateTime.parse("2026-02-17T10:30:00");
        List<Long> skillsIds = List.of(1L);
        BootcampModel bootcampModelToBeSaved = new BootcampModel(null,"name", "description"
        , LocalDateTime.now().plusHours(1L), Duration.ofHours(2L), skillsIds);
        BootcampModel bootcampModelSaved = new BootcampModel(1L, "name",  "description"
                , fecha, Duration.ofHours(2L), skillsIds);
        BootcampEntity bootcampEntitySaved = new BootcampEntity(1L, "name", "description"
        , fecha,2L,3);
        List<BootcampSkillEntity> bootcampSkillEntityList = List.of(
          new BootcampSkillEntity(1L,1L)
        );

        when(bootcampRepository.save(any(BootcampEntity.class))).thenReturn(Mono.just(bootcampEntitySaved));
        when(bootcampSkillRepository.saveAll(anyList())).thenReturn(Flux.fromIterable(bootcampSkillEntityList));

        // Act
        Mono<BootcampModel> result = bootcampRepositoryImpl.save(bootcampModelToBeSaved);

        //Assert
        StepVerifier.create(result)
                .expectNext(bootcampModelSaved)
                .verifyComplete();
        verify(bootcampRepository).save(any(BootcampEntity.class));
        verify(bootcampSkillRepository).saveAll(anyList());
    }
}
