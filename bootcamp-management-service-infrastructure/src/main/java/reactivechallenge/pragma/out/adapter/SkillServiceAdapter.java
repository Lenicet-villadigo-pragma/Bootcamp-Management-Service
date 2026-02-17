package reactivechallenge.pragma.out.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactivechallenge.pragma.exception.InconsistencyDataException;
import reactivechallenge.pragma.spi.ISkillServicePort;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Component
@Slf4j
public class SkillServiceAdapter implements ISkillServicePort {

    private final WebClient webClient;

    public SkillServiceAdapter(WebClient.Builder webClientBuilder
            , @Value("${parameterized.services.skill.base-url}") String baseUrl
            , @Value("${parameterized.flag.webclient-builder-debug}") Boolean webClientBuilderDebug) {
        this.webClient = webClientBuilder.baseUrl(baseUrl)
                .codecs(configurer -> configurer.defaultCodecs().enableLoggingRequestDetails(webClientBuilderDebug))
                .build();
    }

    @Override
    public Mono<Boolean> existsById(List<String> skillIds) {
        String skillsIdsAsString = String.join(", ", skillIds);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/exists")
                        .queryParam("skillIds", skillsIdsAsString)
                        .build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .timeout(Duration.ofSeconds(5))
                .doOnError(e ->
                        log.error("Error al verificar la existencia de las capacidades con ids {}: {}"
                                , skillsIdsAsString, e.getMessage()))
                .onErrorResume(e ->
                        Mono.error(new InconsistencyDataException(
                                String.format("Error al verificar la existencia de las capacidades con ids %s, mensaje: %s"
                                        , skillsIdsAsString, e.getMessage())))
                );
    }
}
