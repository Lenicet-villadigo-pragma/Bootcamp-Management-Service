package reactivechallenge.pragma.out.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactivechallenge.pragma.mapper.BootcampEntityMapper;
import reactivechallenge.pragma.mapper.BootcampSkillEntityMapper;
import reactivechallenge.pragma.mapper.DatabaseErrorMapper;
import reactivechallenge.pragma.model.BootcampModel;
import reactivechallenge.pragma.out.entity.BootcampSkillEntity;
import reactivechallenge.pragma.out.repository.IBootcampRepository;
import reactivechallenge.pragma.out.repository.IBootcampSkillRepository;
import reactivechallenge.pragma.spi.IBootcampRepositoryPort;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
public class BootcampRepositoryImpl implements IBootcampRepositoryPort {

    private final IBootcampRepository bootcampRepository;
    private final IBootcampSkillRepository bootcampSkillRepository;
    private final BootcampEntityMapper bEntityMapper;
    private final BootcampSkillEntityMapper bsEntityMapper;
    private final DatabaseErrorMapper databaseErrorMapper;

    public BootcampRepositoryImpl(IBootcampRepository bootcampRepository, BootcampEntityMapper bEntityMapper
            , BootcampSkillEntityMapper bsEntityMapper, IBootcampSkillRepository bootcampSkillRepository
            , DatabaseErrorMapper databaseErrorMapper) {
        this.bootcampRepository = bootcampRepository;
        this.bEntityMapper = bEntityMapper;
        this.bsEntityMapper = bsEntityMapper;
        this.bootcampSkillRepository = bootcampSkillRepository;
        this.databaseErrorMapper = databaseErrorMapper;
    }

    @Override
    public Mono<BootcampModel> save(BootcampModel bootcampModel) {
        return bootcampRepository.save(bEntityMapper.toEntity(bootcampModel))
                .flatMap(savedBootcampEntity -> saveBootcampSkillRelation(bEntityMapper.toModel(savedBootcampEntity,
                        bootcampModel.skillsIds())))
                .onErrorMap(databaseErrorMapper::map);
    }

    private Mono<BootcampModel> saveBootcampSkillRelation(BootcampModel bootcampModel) {
        List<Long> skillsIds = bootcampModel.skillsIds();

        if (skillsIds == null || skillsIds.isEmpty()) {
            return Mono.just(bootcampModel);
        }

        List<BootcampSkillEntity> bootcampSkillEntities = skillsIds.stream()
                .map(skillId -> bsEntityMapper.toEntity(bootcampModel, skillId))
                .toList();

        return bootcampSkillRepository.saveAll(bootcampSkillEntities)
                .then(Mono.just(bootcampModel));
    }
}
