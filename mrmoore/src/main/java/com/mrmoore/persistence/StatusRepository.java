package com.mrmoore.persistence;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.mrmoore.persistence.entity.StatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<StatusEntity, Long> {

    Optional<List<StatusEntity>> findByTimestampGreaterThan(Date date);
}
