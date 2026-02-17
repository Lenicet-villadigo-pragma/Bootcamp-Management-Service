package reactivechallenge.pragma.model;

import reactivechallenge.pragma.exception.BusinessDomainException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public record BootcampModel (
    Long id,
    String name,
    String description,
    LocalDateTime startDate,
    Duration estimatedTime,
    List<Long> skillsIds
){

    public BootcampModel{
        validateName(name);
        validateSkills(skillsIds);
        validateStartDay(startDate);
        validateEstimatedTime(estimatedTime);
    }

    private  void validateName(String name) {
        String nameValidated = name == null ? "" : name.trim();
        if (nameValidated.isBlank()) {
            throw new BusinessDomainException("El nombre del bootcamp no puede estar vacío");
        }
    }

    private void validateSkills(List<Long> skillsIds) {
        if (skillsIds == null || skillsIds.isEmpty() || skillsIds.size() > 4) {
            throw new BusinessDomainException("La lista de capacidades no puede ser nula, vacía o " +
                    "contener más de 4.");
        }

        // Validar que no haya id de skill duplicados
        long uniqueSkillsCount = skillsIds.stream()
                .distinct()
                .count();
        if (uniqueSkillsCount < skillsIds.size()) {
            throw new BusinessDomainException("La lista de capacidades no puede contener elementos duplicados");
        }
    }

    public List<String> getSkillIdsAsString() {
        return skillsIds.stream()
                .map(String::valueOf)
                .toList();
    }

    private void validateStartDay(LocalDateTime startDate){
        if(startDate==null){
            throw new BusinessDomainException("Debe ingresar una fecha de inicio válida.");
        }
    }

    private void validateEstimatedTime(Duration estimatedTime){
        if(estimatedTime==null || estimatedTime.toHours() == 0){
            throw new BusinessDomainException("EL bootcamp debe tener duración de al menos 1 hora");
        }
    }
}
