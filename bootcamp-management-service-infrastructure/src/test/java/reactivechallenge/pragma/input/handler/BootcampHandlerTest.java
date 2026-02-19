package reactivechallenge.pragma.input.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactivechallenge.pragma.api.IDeleteBootcampServicePort;
import reactivechallenge.pragma.api.IRegisterBootcampServicePort;
import reactivechallenge.pragma.api.IRetrieveBootcampServicePort;
import reactivechallenge.pragma.input.dto.CreateBootcampRequestDto;
import reactivechallenge.pragma.model.BootcampModel;
import reactivechallenge.pragma.spi.ISkillServicePort;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class BootcampHandlerTest {
    @Mock
    IRegisterBootcampServicePort registerBootcampServicePortMock;
    @Mock
    IRetrieveBootcampServicePort retrieveBootcampServicePortMock;
    @Mock
    ISkillServicePort skillServicePortMock;
    @Mock
    IDeleteBootcampServicePort deleteBootcampServicePort;
    @Mock
    TransactionalOperator transactionalOperator;

    private BootcampHandler bootcampHandler;

    @BeforeEach
    void setUp() {
        bootcampHandler = new BootcampHandler(registerBootcampServicePortMock, retrieveBootcampServicePortMock,
                skillServicePortMock,0,10, deleteBootcampServicePort
        , transactionalOperator);
    }

    @Test
    @DisplayName("Create bootcamp succeeds when dto is valid")
    void createBootcampSuccess() {
        // Arrange
        List<Long> skillsIds = List.of(1L);
        CreateBootcampRequestDto validDto = new CreateBootcampRequestDto("b1", "Valid description",
                LocalDateTime.now().plusHours(1), 2L, skillsIds);
        ServerRequest request = mock(ServerRequest.class);

        given(request.bodyToMono(CreateBootcampRequestDto.class)).willReturn(Mono.just(validDto));
        given(registerBootcampServicePortMock.registerBootcamp(any(BootcampModel.class)))
                .willReturn(Mono.just(new BootcampModel(1L, "b1", "Valid description",
                        LocalDateTime.now().plusHours(1), Duration.ofHours(2L), skillsIds)));

        // Act
        Mono<ServerResponse> responseMono = bootcampHandler.createBootcamp(request);

        // Assert
        StepVerifier.create(responseMono)
                .expectNextMatches(serverResponse -> serverResponse.statusCode() == HttpStatus.CREATED)
                .verifyComplete();
    }
}
