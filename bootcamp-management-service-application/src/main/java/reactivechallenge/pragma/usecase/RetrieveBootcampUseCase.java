package reactivechallenge.pragma.usecase;

import reactivechallenge.pragma.api.IRetrieveBootcampServicePort;
import reactivechallenge.pragma.model.BootcampModel;
import reactivechallenge.pragma.model.criteria.PaginationResult;
import reactivechallenge.pragma.model.criteria.SortField;
import reactivechallenge.pragma.model.criteria.SortOrder;
import reactivechallenge.pragma.spi.IBootcampRepositoryPort;
import reactor.core.publisher.Mono;

public class RetrieveBootcampUseCase implements IRetrieveBootcampServicePort {
    private final IBootcampRepositoryPort bootcampRepositoryPort;

    public RetrieveBootcampUseCase(IBootcampRepositoryPort bootcampRepositoryPort) {
        this.bootcampRepositoryPort = bootcampRepositoryPort;
    }

    @Override
    public Mono<PaginationResult<BootcampModel>> retrieveBootcamps(SortField sortField, SortOrder sortOrder, Integer pageNumber, Integer pageSize) {
        return Mono.zip(
                bootcampRepositoryPort.getBootcamps(
                                sortField == null? SortField.NAME:sortField
                                , sortOrder ==null?SortOrder.ASC:sortOrder
                                , pageNumber,pageSize)
                        .collectList()
                , bootcampRepositoryPort.countBootcamps()
        ).map(tuple -> new PaginationResult<>(tuple.getT1(), tuple.getT2()));
    }
}
