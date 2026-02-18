package reactivechallenge.pragma.input.router;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactivechallenge.pragma.input.dto.CreateBootcampResponseDto;
import reactivechallenge.pragma.input.dto.ListAllResponseDto;
import reactivechallenge.pragma.input.dto.PaginatedDto;
import reactivechallenge.pragma.input.handler.BootcampHandler;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BootcampRouterTest {
    @Mock
    private BootcampHandler bootcampHandlerMock;

    @InjectMocks
    private BootcampRouter bootcampRouter;

    @Test
    void createBootcampRouterSuccess(){
        // Arrange
        CreateBootcampResponseDto responseDto = new CreateBootcampResponseDto(1L,"Java");

        when(bootcampHandlerMock.createBootcamp(any(ServerRequest.class))).thenReturn(
                ServerResponse.created(java.net.URI.create("/create"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(responseDto), CreateBootcampResponseDto.class)
        );

        WebTestClient webTestClient = WebTestClient
                .bindToRouterFunction(bootcampRouter.bootcampRoutes(bootcampHandlerMock))
                .build();

        // Act & Assert
        webTestClient.post()
                .uri("/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(responseDto)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(CreateBootcampResponseDto.class)
                .isEqualTo(responseDto);
    }

    @Test
    @DisplayName("Router routes GET /retrieve to handler")
    void listSkillRouteTest() {
        // Arrange
        List<ListAllResponseDto> listAllResponseDtos = List.of(new ListAllResponseDto(null, null, null));
        PaginatedDto<ListAllResponseDto> paginatedDto = new PaginatedDto<>(listAllResponseDtos, 1L, 0, 10);

        when(bootcampHandlerMock.listBootcamps(any(ServerRequest.class))).thenReturn(
                ServerResponse.ok().body(Mono.just(paginatedDto), PaginatedDto.class)
        );

        WebTestClient webTestClient = WebTestClient
                .bindToRouterFunction(bootcampRouter.bootcampRoutes(bootcampHandlerMock))
                .build();

        // Act & Assert
        webTestClient.get()
                .uri("/retrieve?sortField={sortField}&sortOrder={sortOrder}&pageNumber={pageNumber}&pageSize={pageNumber}"
                        ,"name", "asc", "10", "0")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(ListAllResponseDto.class)
                .isEqualTo(listAllResponseDtos);
    }
}
