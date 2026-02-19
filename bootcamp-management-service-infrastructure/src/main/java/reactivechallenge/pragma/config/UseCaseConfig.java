package reactivechallenge.pragma.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactivechallenge.pragma.api.IDeleteBootcampServicePort;
import reactivechallenge.pragma.api.IRegisterBootcampServicePort;
import reactivechallenge.pragma.api.IRetrieveBootcampServicePort;
import reactivechallenge.pragma.spi.IBootcampRepositoryPort;
import reactivechallenge.pragma.spi.ISkillServicePort;
import reactivechallenge.pragma.usecase.CreateBootcampUseCase;
import reactivechallenge.pragma.usecase.DeleteBootcampUseCase;
import reactivechallenge.pragma.usecase.RetrieveBootcampUseCase;

@Configuration
public class UseCaseConfig {

    @Bean
    public IRegisterBootcampServicePort creatorRegisterBootcampServicePort(IBootcampRepositoryPort bootcampRepositoryPort,
                                                                           ISkillServicePort skillServicePort){
        return new CreateBootcampUseCase(bootcampRepositoryPort, skillServicePort);
    }

    @Bean
    public IRetrieveBootcampServicePort creatorRetrieveBootcampServicePort(IBootcampRepositoryPort bootcampRepositoryPort){
        return  new RetrieveBootcampUseCase(bootcampRepositoryPort);
    }

    @Bean
    public IDeleteBootcampServicePort creatorDeleteBootcampServicePort(IBootcampRepositoryPort bootcampRepositoryPort
    , ISkillServicePort skillServicePort){
        return  new DeleteBootcampUseCase(bootcampRepositoryPort, skillServicePort);
    }
}
