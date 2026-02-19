package reactivechallenge.pragma.out.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import reactivechallenge.pragma.exception.BusinessDomainException;
import reactivechallenge.pragma.mapper.BootcampEntityMapper;
import reactivechallenge.pragma.mapper.BootcampSkillEntityMapper;
import reactivechallenge.pragma.mapper.DatabaseErrorMapper;
import reactivechallenge.pragma.model.BootcampModel;
import reactivechallenge.pragma.model.SkillExternalModel;
import reactivechallenge.pragma.model.criteria.SortField;
import reactivechallenge.pragma.model.criteria.SortOrder;
import reactivechallenge.pragma.out.entity.BootcampSkillEntity;
import reactivechallenge.pragma.out.repository.IBootcampRepository;
import reactivechallenge.pragma.out.repository.IBootcampSkillRepository;
import reactivechallenge.pragma.spi.IBootcampRepositoryPort;
import reactor.core.publisher.Flux;
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

    @Override
    public Flux<BootcampModel> getBootcamps(SortField sortField, SortOrder sortOrder, Integer pageNumber, Integer pageSize) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder.getName()), sortField.getFieldName());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        return bootcampRepository.findAllBy(pageable).concatMap(bootcampEntity ->
                getSkillsByBootcampId(bootcampEntity.id()).collectList()
                        .flatMap(skillExternalModels ->{
                            if (skillExternalModels.isEmpty()) {
                                return Mono.error(new BusinessDomainException("Bootcamp sin capacidades"));
                            }
                            return Mono.just(bEntityMapper.toModel(bootcampEntity, skillExternalModels.stream()
                                    .map(SkillExternalModel::id).toList()));
                        }).onErrorResume(error -> {
                            log.warn("Omitiendo skill {} por error: {}", bootcampEntity.id(), error.getMessage());
                            return Mono.empty();
                        })
        );
    }

    @Override
    public Mono<Long> countBootcamps() {
        return bootcampRepository.count();
    }

    @Override
    public Mono<Void> deleteBootcampsByIds(List<Long> ids) {
        return bootcampRepository.deleteAllById(ids)
                .onErrorMap(databaseErrorMapper::map);
    }

    @Override
    public Flux<Long> getSkillsIdsByBootcampId(Long bootcampId) {
        return bootcampSkillRepository.findAllByBootcampId(bootcampId)
                .map(BootcampSkillEntity::skillId);
    }

    @Override
    public Mono<Void> deleteBootcampSkillRelation(Long bootcampId) {
        return bootcampSkillRepository.deleteByBootcampId(bootcampId)
                .onErrorMap(databaseErrorMapper::map);
    }

    @Override
    public Mono<Long> getTotalSkillRelationWithBootcamps(Long skillId) {
        return bootcampSkillRepository.countBySkillId(skillId);
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

    private Flux<SkillExternalModel> getSkillsByBootcampId(Long bootcampId){
        return bootcampSkillRepository.findAllByBootcampId(bootcampId)
                .map(bsEntityMapper::toSkillExternalModel);
    }
}
