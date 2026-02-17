package reactivechallenge.pragma.out.adapter;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactivechallenge.pragma.exception.InconsistencyDataException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

class SkillServiceAdapterTest {

    private static MockWebServer mockWebServer;
    private SkillServiceAdapter skillServiceAdapter;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
        skillServiceAdapter = new SkillServiceAdapter(WebClient.builder(), baseUrl, true);
    }

    @Test
    void existsById_shouldReturnTrue_whenSkillsExist() {
        // Arrange
        mockWebServer.enqueue(new MockResponse()
                .setBody("true")
                .addHeader("Content-Type", "application/json"));
        List<String> skillsIds = Arrays.asList("1", "2", "3");

        // Act
        Mono<Boolean> result = skillServiceAdapter.existsById(skillsIds);

        // Assert
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void existsById_shouldReturnFalse_whenSkillsDoNotExist() {
        // Arrange
        mockWebServer.enqueue(new MockResponse()
                .setBody("false")
                .addHeader("Content-Type", "application/json"));
        List<String> skillsIds = Arrays.asList("1", "2", "3");

        // Act
        Mono<Boolean> result = skillServiceAdapter.existsById(skillsIds);

        // Assert
        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    void existsById_shouldThrowInconsistencyDataException_onTimeout() {
        // Arrange
        mockWebServer.enqueue(new MockResponse()
                .setBody("true")
                .addHeader("Content-Type", "application/json")
                .setBodyDelay(6, TimeUnit.SECONDS)); // Delay longer than timeout
        List<String> skillsIds = Arrays.asList("1", "2", "3");

        // Act
        Mono<Boolean> result = skillServiceAdapter.existsById(skillsIds);

        // Assert
        StepVerifier.create(result)
                .expectError(InconsistencyDataException.class)
                .verify();
    }

    @Test
    void existsById_shouldThrowInconsistencyDataException_on5xxError() {
        // Arrange
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));
        List<String> skillsIds = Arrays.asList("1", "2", "3");

        // Act
        Mono<Boolean> result = skillServiceAdapter.existsById(skillsIds);

        // Act & Assert
        StepVerifier.create(result)
                .expectError(InconsistencyDataException.class)
                .verify();
    }
    
}
