package edu.itba.class10.infrastructure.persistence.relational;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SingleConversionJpaRepository extends JpaRepository<SingleConversionSqlEntity, Long> {
}
