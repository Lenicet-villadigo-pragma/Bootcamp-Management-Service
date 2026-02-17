package reactivechallenge.pragma.out.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("bootcamp")
public record BootcampEntity(
        @Id @Column("id") Long id,
        @Column("name") String name,
        @Column("description") String description,
        @Column("start_on_date") LocalDateTime startDay,
        @Column("estimated_time") Long estimatedTime,
        @Column("total_skills") Integer totalSkills
) {}
