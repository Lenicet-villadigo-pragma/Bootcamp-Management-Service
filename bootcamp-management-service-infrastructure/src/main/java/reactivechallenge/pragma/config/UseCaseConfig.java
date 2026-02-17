package reactivechallenge.pragma.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactivechallenge.pragma.api.IRegisterBootcampServicePort;
import reactivechallenge.pragma.spi.IBootcampRepositoryPort;
import reactivechallenge.pragma.spi.ISkillServicePort;
import reactivechallenge.pragma.usecase.CreateBootcampUseCase;

@Configuration
public class UseCaseConfig {

    @Bean
    public IRegisterBootcampServicePort creatorRegisterBootcampServicePort(IBootcampRepositoryPort bootcampRepositoryPort,
                                                                           ISkillServicePort skillServicePort){
        return new CreateBootcampUseCase(bootcampRepositoryPort, skillServicePort);
    }
}
