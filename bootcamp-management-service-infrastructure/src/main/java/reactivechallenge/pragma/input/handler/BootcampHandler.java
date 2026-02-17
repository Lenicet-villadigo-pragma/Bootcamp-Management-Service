package reactivechallenge.pragma.input.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactivechallenge.pragma.api.IRegisterBootcampServicePort;
import reactivechallenge.pragma.input.dto.CreateBootcampRequestDto;
import reactivechallenge.pragma.input.dto.CreateBootcampResponseDto;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class BootcampHandler {
    private final IRegisterBootcampServicePort registerBootcampServicePort;

    public BootcampHandler(IRegisterBootcampServicePort registerBootcampServicePort){
        this.registerBootcampServicePort = registerBootcampServicePort;
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
}
