package com.mrmoore.persistence;


import com.mrmoore.persistence.entity.VisitorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitorRepository extends JpaRepository<VisitorEntity, Long> {


}
