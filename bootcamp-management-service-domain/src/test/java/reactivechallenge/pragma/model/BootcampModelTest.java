package reactivechallenge.pragma.model;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.AssertionErrors;

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
}
