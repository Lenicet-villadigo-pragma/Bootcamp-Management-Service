package reactivechallenge.pragma.usecase;

import reactivechallenge.pragma.api.IRetrieveBootcampServicePort;
import reactivechallenge.pragma.model.BootcampModel;
import reactivechallenge.pragma.model.criteria.PaginationResult;
import reactivechallenge.pragma.model.criteria.SortField;
import reactivechallenge.pragma.model.criteria.SortOrder;
import reactivechallenge.pragma.spi.IBootcampRepositoryPort;
import reactivechallenge.pragma.spi.ISkillServicePort;
import reactor.core.publisher.Mono;

public class RetrieveBootcampUseCase implements IRetrieveBootcampServicePort {
    private final IBootcampRepositoryPort bootcampRepositoryPort;
    private final ISkillServicePort skillServicePort;

    public RetrieveBootcampUseCase(IBootcampRepositoryPort bootcampRepositoryPort
    , ISkillServicePort skillServicePort) {
        this.bootcampRepositoryPort = bootcampRepositoryPort;
        this.skillServicePort = skillServicePort;
    }

    @Override
    public Mono<PaginationResult<BootcampModel>> retrieveBootcamps(SortField sortField, SortOrder sortOrder, Integer pageNumber, Integer pageSize) {
        return Mono.zip(
                bootcampRepositoryPort.getBootcamps(
                                sortField == null? SortField.NAME:sortField
                                , sortOrder ==null?SortOrder.ASC:sortOrder
                                , pageNumber,pageSize)
                        .concatMap(bootcampModel -> skillServicePort.getSkillsByIds(bootcampModel.getSkillIdsAsString())
                                .collectList()
                                .map(skillExternalModelList ->
                                     new BootcampModel(bootcampModel.id(), bootcampModel.name(), bootcampModel.description()
                                    ,bootcampModel.startDate(), bootcampModel.estimatedTime(), skillExternalModelList)
                                )
                        )
                        .collectList()
                , bootcampRepositoryPort.countBootcamps()
        ).map(tuple -> new PaginationResult<>(tuple.getT1(), tuple.getT2()));
    }
}
