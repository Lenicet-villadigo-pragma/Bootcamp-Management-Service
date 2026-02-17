package reactivechallenge.pragma.input.router;

import io.swagger.v3.oas.annotations.Operation;
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
import reactivechallenge.pragma.input.handler.BootcampHandler;

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

    })
    public RouterFunction<ServerResponse> bootcampRoutes(BootcampHandler bootcampHandler) {
        return route(POST("/create").and(accept(MediaType.APPLICATION_JSON)), bootcampHandler::createBootcamp);
    }
}
