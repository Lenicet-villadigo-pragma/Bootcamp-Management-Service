package reactivechallenge.pragma.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.AssertionErrors;
import reactivechallenge.pragma.exception.BusinessDomainException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class BootcampModelTest {

    @Test
    void createBootcampModelSuccessful(){
        // Arrange
        BootcampModel bootcampModel = null;
        List<SkillExternalModel> skillsIds = List.of(new SkillExternalModel(1L,"", new ArrayList<>()));

        // Act
        try {
            bootcampModel = new BootcampModel(null,"name", "description", LocalDateTime.now().plusHours(1)
                    , Duration.ofHours(100), skillsIds);
        }catch (Exception ex){
            System.out.println("error en el test"+ ex);
        }

        //Assert
        AssertionErrors.assertNotNull("Se espera que bootcamp model sea creado", bootcampModel);
    }

    @Test
    void createBootcampModelFailsBecauseEmptyName(){
        // Arrange
        BootcampModel bootcampModel = null;
        BusinessDomainException businessDomainException = null;
        List<SkillExternalModel> skillsIds = List.of(new SkillExternalModel(1L,"", new ArrayList<>()));

        // Act
        try {
            bootcampModel = new BootcampModel(null,"", "description", LocalDateTime.now().plusHours(1)
                    , Duration.ofHours(100), skillsIds);
        }catch (Exception ex){
            businessDomainException = (BusinessDomainException) ex;
        }

        //Assert
        AssertionErrors.assertNull("Se espera que Bootcamp model no sea creado por nombre vacío o nulo", bootcampModel);
        AssertionErrors.assertNotNull("Se espera que BusinessDomainException sea lanzado", businessDomainException);
        Assertions.assertEquals("El nombre del bootcamp no puede estar vacío", businessDomainException.getMessage());
    }

    @Test
    void createBootcampModelFailsBecauseNullSkills(){
        // Arrange
        BootcampModel bootcampModel = null;
        BusinessDomainException businessDomainException = null;

        // Act
        try {
            bootcampModel = new BootcampModel(null,"name", "description", LocalDateTime.now().plusHours(1)
                    , Duration.ofHours(100), null);
        }catch (Exception ex){
            businessDomainException = (BusinessDomainException) ex;
        }

        //Assert
        AssertionErrors.assertNull("Se espera que Bootcamp model no sea creado por nombre vacío o nulo", bootcampModel);
        AssertionErrors.assertNotNull("Se espera que BusinessDomainException sea lanzado", businessDomainException);
        Assertions.assertEquals("La lista de capacidades no puede ser nula, vacía o contener más de 4."
                , businessDomainException.getMessage());
    }

    @Test
    void createBootcampModelReturnsSkillsIdsAsString(){
        // Arrange
        List<SkillExternalModel> skillsIds = List.of(
                new SkillExternalModel(1L,"", new ArrayList<>()),
                new SkillExternalModel(2L,"", new ArrayList<>()),
                new SkillExternalModel(3L,"", new ArrayList<>())
        );
        BootcampModel bootcampModel = new BootcampModel(null,"name", "description"
                , LocalDateTime.now().plusHours(1), Duration.ofHours(100), skillsIds);
        List<String> techIds = null;

        // Act
        techIds = bootcampModel.getSkillIdsAsString();

        //Assert
        AssertionErrors.assertNotNull("skill ids recibidos como string", techIds);
        Assertions.assertEquals("1", techIds.getFirst());
        Assertions.assertEquals("2", techIds.get(1));
        Assertions.assertEquals("3", techIds.getLast());
    }

    @Test
    void createBootcampModelFailsBecauseInvalidStartDay(){
        // Arrange
        BootcampModel bootcampModel = null;
        BusinessDomainException businessDomainException = null;

        // Act
        try {
            bootcampModel = new BootcampModel(null,"name", "description"
                    , null, Duration.ofHours(100), List.of(new SkillExternalModel(1L,"", new ArrayList<>())));
        }catch (Exception ex){
            businessDomainException = (BusinessDomainException) ex;
        }

        //Assert
        AssertionErrors.assertNull("Se espera que Bootcamp model no sea creado por nombre vacío o nulo", bootcampModel);
        AssertionErrors.assertNotNull("Se espera que BusinessDomainException sea lanzado", businessDomainException);
        Assertions.assertEquals("Debe ingresar una fecha de inicio válida."
                , businessDomainException.getMessage());
    }

    @Test
    void createBootcampModelFailsBecauseInvalidDuration(){
        // Arrange
        BootcampModel bootcampModel = null;
        BusinessDomainException businessDomainException = null;

        // Act
        try {
            bootcampModel = new BootcampModel(null,"name", "description"
                    , LocalDateTime.now().plusHours(1), Duration.ofHours(0), List.of(new SkillExternalModel(1L,"", new ArrayList<>())));
        }catch (Exception ex){
            businessDomainException = (BusinessDomainException) ex;
        }

        //Assert
        AssertionErrors.assertNull("Se espera que Bootcamp model no sea creado por nombre vacío o nulo", bootcampModel);
        AssertionErrors.assertNotNull("Se espera que BusinessDomainException sea lanzado", businessDomainException);
        Assertions.assertEquals("EL bootcamp debe tener duración de al menos 1 hora"
                , businessDomainException.getMessage());
    }
}
