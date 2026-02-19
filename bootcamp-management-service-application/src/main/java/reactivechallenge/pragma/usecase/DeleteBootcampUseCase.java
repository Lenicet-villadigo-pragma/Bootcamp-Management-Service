package reactivechallenge.pragma.usecase;

import lombok.extern.slf4j.Slf4j;
import reactivechallenge.pragma.api.IDeleteBootcampServicePort;
import reactivechallenge.pragma.spi.IBootcampRepositoryPort;
import reactivechallenge.pragma.spi.ISkillServicePort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
public class DeleteBootcampUseCase implements IDeleteBootcampServicePort {
    private final IBootcampRepositoryPort bootcampRepositoryPort;
    private final ISkillServicePort skillServicePort;

    public DeleteBootcampUseCase(IBootcampRepositoryPort bootcampRepositoryPort, ISkillServicePort skillServicePort) {
        this.bootcampRepositoryPort = bootcampRepositoryPort;
        this.skillServicePort = skillServicePort;
    }

    @Override
    public Mono<Void> deleteBootcampsByIds(List<Long> ids) {
        if(ids==null){
            return Mono.empty();
        }
        return Flux.fromIterable(ids)
                .flatMap(bootcampRepositoryPort::getSkillsIdsByBootcampId)
                .collectList()
                .map(skillsIds -> skillsIds.stream().distinct().toList())
                .flatMap(affectedSkillIds ->
                        // 2. OPERACIÓN LOCAL ATÓMICA
                        // Procesamos cada capacidad para borrar sus relaciones y luego las capacidades mismas
                        Flux.fromIterable(ids)
                                .flatMap(bootcampId ->
                                        bootcampRepositoryPort.deleteBootcampSkillRelation(bootcampId)
                                                .thenReturn(ids)
                                )
                                .flatMap(bootcampRepositoryPort::deleteBootcampsByIds)
                                .then() // Esperamos a que todas las capacidades locales se borren

                                // 3. LIMPIEZA EXTERNA (Transparente y posterior al éxito local)
                                .then(
                                        Flux.fromIterable(affectedSkillIds)
                                                .flatMap(skillId ->
                                                        bootcampRepositoryPort.getTotalSkillRelationWithBootcamps(skillId)
                                                                .filter(totalRelations -> totalRelations == 0)
                                                                .flatMap(totalSkillsWithoutRelations -> skillServicePort.deleteSkillById(String.valueOf(skillId)))
                                                                .onErrorResume(e -> {
                                                                    log.error("No se pudo eliminar la capacidad sin relacion con bootcamps, error {}", e.getMessage());
                                                                    return Mono.empty();
                                                                })
                                                )
                                                .then()
                                )
                );
    }

    @Override
    public List<Long> verifyBootcampIds(String bootcampIdsAsString){
        Optional<String> skillIdsAsStringOpt = Optional.ofNullable(bootcampIdsAsString);
        List<String> skillIds = skillIdsAsStringOpt
                .map(idList -> Arrays.stream(idList.split(","))
                        .map(idString -> idString.replaceAll("[\"/\\\\]", "").trim())
                        .toList())
                .orElse(Collections.emptyList());

        if(skillIds.isEmpty()){
            throw new IllegalArgumentException("No se proporcionaron IDs de bootcamps. Asegúrate de incluir el " +
                    "parámetro 'bootcampsIds' con al menos un ID.");
        }
        if(skillIds.stream().anyMatch(id -> !id.matches("\\d+"))){
            throw new IllegalArgumentException("Formato de IDs inválido. Todos los IDs deben ser números.");
        }

        return skillIds.stream().map(Long::valueOf).toList();
    }
}
