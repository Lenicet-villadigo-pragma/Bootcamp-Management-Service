package reactivechallenge.pragma.input.router;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactivechallenge.pragma.input.dto.CreateBootcampRequestDto;
import reactivechallenge.pragma.input.dto.CreateBootcampResponseDto;
import reactivechallenge.pragma.input.dto.ListAllResponseDto;
import reactivechallenge.pragma.input.handler.BootcampHandler;
import reactivechallenge.pragma.model.criteria.ExistsResponseDto;
import reactivechallenge.pragma.model.criteria.SortField;
import reactivechallenge.pragma.model.criteria.SortOrder;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class BootcampRouter {
    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/create",
                    produces = {
                            MediaType.APPLICATION_JSON_VALUE
                    },
                    method = RequestMethod.POST,
                    beanClass = BootcampHandler.class,
                    beanMethod = "createBootcamp",
                    operation = @Operation(
                            operationId = "createBootcamp",
                            summary = "Crear un nuevo bootcamp",
                            description = "Crea un nuevo registro para bootcamp en el sistema con la información proporcionada.",
                            tags = {"Gestión de Bootcamps"},
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Bootcamp created successfully",
                                            content = @Content(schema = @Schema(implementation = CreateBootcampResponseDto.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Invalid Input"
                                    )
                            },
                            requestBody = @RequestBody(
                                    content = @Content(schema = @Schema(implementation = CreateBootcampRequestDto.class))
                            )
                    )
            )
            ,@RouterOperation(
            path = "/retrieve",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            },
            method = RequestMethod.GET,
            beanClass = BootcampHandler.class,
            beanMethod = "listBootcamps",
            operation = @Operation(
                    operationId = "listBootcamps",
                    summary = "Listar bootcamps existentes",
                    description = "Se obtienen los bootcamps con sus respectivas capacidades, " +
                            "se puede ordenar ascendente (asc) o descendente (desc) ya sea por nombre o " +
                            "cantidad de capacidades asociadas",
                    tags = {"Gestión de Bootcamps"},
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "Ok",
                                    content = @Content(schema = @Schema(implementation = ListAllResponseDto.class))
                            ),
                            @ApiResponse(
                                    responseCode = "400",
                                    description = "Invalid Input"
                            )
                    },
                    parameters = {
                            @Parameter(in = ParameterIn.QUERY, name = "sortField",
                                    schema = @Schema(implementation = SortField.class),
                                    description = "campo opcional para ordenar, valores: name o total_capacidades",
                                    required = false)
                            ,@Parameter(in = ParameterIn.QUERY, name = "sortOrder",
                                    schema = @Schema(implementation = SortOrder.class),
                                    description = "Sentido de ordenación, opcional. valores: asc o desc",
                                    required = false)
                            ,@Parameter(in = ParameterIn.QUERY, name = "pageNumber",
                                    schema = @Schema(implementation = Integer.class),
                                    description = "Número de página, opcional", required = false)
                            ,@Parameter(in = ParameterIn.QUERY, name = "pageSize",
                                    schema = @Schema(implementation = Integer.class),
                                    description = "Cantidad de registros por página, opcional", required = false)

                    })
            )
            ,@RouterOperation(
            path = "/deleteByIds",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            },
            method = RequestMethod.DELETE,
            beanClass = BootcampHandler.class,
            beanMethod = "deleteBootcampsByIds",
            operation = @Operation(
                    operationId = "deleteBootcampsByIds",
                    summary = "Eliminar bootcamps por id",
                    description = "Se eliminan los bootcamps con sus respectivas capacidades.",
                    tags = {"Gestión de Bootcamps"},
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "Ok",
                                    content = @Content(schema = @Schema(implementation = String.class))
                            ),
                            @ApiResponse(
                                    responseCode = "500",
                                    description = "Error interno"
                            )
                    },
                    parameters = {
                            @Parameter(in = ParameterIn.QUERY, name = "bootcampsIds",
                                    schema = @Schema(implementation = Integer.class),
                                    description = "Id para eliminar bootcamps, se puede enviar varios separados por coma.",
                                    required = true)
                    })
            )
            ,@RouterOperation(
                path = "/existsByIds",
                produces = {
                    MediaType.APPLICATION_JSON_VALUE
                },
                method = RequestMethod.GET,
                beanClass = BootcampHandler.class,
                beanMethod = "existsBootcampsByIds",
                operation = @Operation(
                    operationId = "existsBootcampsByIds",
                    summary = "Verificar existencia de bootcamps por ids",
                    description = "Se verifica si existe el registro y se devuelve true o false por cada id enviado.",
                    tags = {"Gestión de Bootcamps"},
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "Ok",
                                    content = @Content(schema = @Schema(implementation = ExistsResponseDto.class))
                            )
                    },
                    parameters = {
                            @Parameter(in = ParameterIn.QUERY, name = "bootcampsIds",
                                    schema = @Schema(implementation = String.class),
                                    description = "Id para verificar existencia de bootcamps, se puede enviar varios separados por coma.",
                                    required = true)
                    }
                )
            )

    })
    public RouterFunction<ServerResponse> bootcampRoutes(BootcampHandler bootcampHandler) {
        return route(POST("/create").and(accept(MediaType.APPLICATION_JSON)), bootcampHandler::createBootcamp)
                .andRoute(GET("/retrieve"), bootcampHandler::listBootcamps)
                .andRoute(DELETE("/deleteByIds"), bootcampHandler::deleteBootcampsByIds)
                .andRoute(GET("/existsByIds"), bootcampHandler::existsBootcampsByIds);
    }
}
