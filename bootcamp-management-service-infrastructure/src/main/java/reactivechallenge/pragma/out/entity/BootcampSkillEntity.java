package reactivechallenge.pragma.out.entity;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("bootcamp_skill")
public record BootcampSkillEntity (
    @Column("bootcamp_id") Long bootcampId,
    @Column("skill_id") Long skillId
){}
