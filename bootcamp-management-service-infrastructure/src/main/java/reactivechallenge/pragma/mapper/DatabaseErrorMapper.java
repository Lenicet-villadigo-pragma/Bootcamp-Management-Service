package reactivechallenge.pragma.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactivechallenge.pragma.exception.BusinessDomainException;
import reactivechallenge.pragma.exception.GenericDatabaseException;

@Component
@Slf4j
public class DatabaseErrorMapper {

    public Throwable map(Throwable e) {
        if (e instanceof org.springframework.dao.DataIntegrityViolationException) {

            if (e.getMessage().contains("bootcamp_unique")) {
                return new BusinessDomainException("Ya existe un bootcamp con ese nombre registrado.");
            }

            return new BusinessDomainException("Error de integridad: verifica los datos enviados.");
        }
        log.error("Error desde bd: {}", e.getMessage());
        return new GenericDatabaseException("Error inesperado en la base de datos");
    }
}
