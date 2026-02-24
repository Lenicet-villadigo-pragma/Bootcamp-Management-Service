package reactivechallenge.pragma.input.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactivechallenge.pragma.api.IDeleteBootcampServicePort;
import reactivechallenge.pragma.api.IRegisterBootcampServicePort;
import reactivechallenge.pragma.api.IRetrieveBootcampServicePort;
import reactivechallenge.pragma.input.dto.*;
import reactivechallenge.pragma.model.criteria.SortField;
import reactivechallenge.pragma.model.criteria.SortOrder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class BootcampHandler {
    private final IRegisterBootcampServicePort registerBootcampServicePort;
    private final IRetrieveBootcampServicePort retrieveBootcampServicePort;
    private final int defaultPageNumber;
    private final int defaultPageSize;
    private final IDeleteBootcampServicePort deleteBootcampServicePort;
    private final TransactionalOperator transactionalOperator;


    public BootcampHandler(IRegisterBootcampServicePort registerBootcampServicePort
    , IRetrieveBootcampServicePort retrieveBootcampServicePort
    , @Value("${parameterized.pagination.default-page}") int defaultPageNumber
    , @Value("${parameterized.pagination.default-size}") int defaultPageSize
    , IDeleteBootcampServicePort deleteBootcampServicePort,TransactionalOperator transactionalOperator){
        this.registerBootcampServicePort = registerBootcampServicePort;
        this.retrieveBootcampServicePort = retrieveBootcampServicePort;
        this.defaultPageNumber = defaultPageNumber;
        this.defaultPageSize = defaultPageSize;
        this.deleteBootcampServicePort = deleteBootcampServicePort;
        this.transactionalOperator = transactionalOperator;
    }

    public Mono<ServerResponse> createBootcamp(ServerRequest request) {
        return request.bodyToMono(CreateBootcampRequestDto.class)
                .map(CreateBootcampRequestDto::toModel)
                .flatMap(registerBootcampServicePort::registerBootcamp)
                .map(CreateBootcampResponseDto::fromModel)
                .flatMap(bootcampDto -> ServerResponse.created(request.uri())
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(bootcampDto));
    }

    public Mono<ServerResponse> listBootcamps(ServerRequest request){
        SortField skillSortField = getSKillSortField(request.queryParam("sortField").orElse(null));
        SortOrder skillSortOrder = getSKillSortOrder(request.queryParam("sortOrder").orElse(null));
        Integer pageNumber = getValidateNumber(request.queryParam("pageNumber").orElse(null), defaultPageNumber);
        Integer pageSize = getValidateNumber(request.queryParam("pageSize").orElse(null), defaultPageSize);


        return retrieveBootcampServicePort
                .retrieveBootcamps(skillSortField, skillSortOrder, pageNumber, pageSize)
                .flatMap(paginationResultModel ->
                        Flux.fromIterable(paginationResultModel.items())
                                .map(ListAllResponseDto::fromModel)
                                .collectList()
                                .flatMap(listAllResponseDto ->{
                                    PaginatedDto<ListAllResponseDto> response = new PaginatedDto<>(
                                            listAllResponseDto, paginationResultModel.total(),pageNumber, pageSize
                                    );

                                    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(response);
                                })
                );
    }

    private SortField getSKillSortField (String valueForSortField) {
        SortField sortField = SortField.fromString(valueForSortField);
        log.info("el valor para ordenación encontrado es {}", sortField.getFieldName());
        return sortField;
    }

    private SortOrder getSKillSortOrder (String valueForSortOrder) {
        SortOrder sortOrder = SortOrder.fromString(valueForSortOrder);
        log.info("el sentido para ordenación encontrado es {}", sortOrder.getName());
        return sortOrder;
    }

    private Integer getValidateNumber(String numberAsString, int defaultValue){
        int number = defaultValue;
        if(numberAsString!=null){
            try {
                number = Integer.parseInt(numberAsString.toLowerCase().replaceAll("\\s+", " ").trim());
            } catch (Exception e) {
                log.error("el valor enviado {} no es válido como numero natural", numberAsString);
            }
        }
        return number;
    }

    public Mono<ServerResponse> deleteBootcampsByIds(ServerRequest serverRequest){
        List<Long> bootcampsIds =getIdsFromString(serverRequest);

        return deleteBootcampServicePort.deleteBootcampsByIds(bootcampsIds)
                .as(transactionalOperator::transactional)
                .then(ServerResponse.ok().bodyValue("Bootcamps eliminados"))
                .doOnError(e -> log.error("Transacción abortada: {}", e.getMessage()))
                .onErrorComplete();
    }

    public Mono<ServerResponse> existsBootcampsByIds(ServerRequest serverRequest){
        List<Long> bootcampsIds = getIdsFromString(serverRequest);

        return retrieveBootcampServicePort.existsBootcampsByIds(bootcampsIds)
                .collectList()
                .flatMap(existsResponseDtoList -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_NDJSON)
                        .bodyValue(existsResponseDtoList))
                    .onErrorComplete();
    }

    private List<Long> getIdsFromString(ServerRequest serverRequest){
        Optional<String> stringBootcampsIds =  serverRequest.queryParam("bootcampsIds");
        return deleteBootcampServicePort.verifyBootcampIds(stringBootcampsIds.orElse(null));
    }
}
