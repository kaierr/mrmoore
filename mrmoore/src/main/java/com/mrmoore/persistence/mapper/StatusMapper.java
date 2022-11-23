package com.mrmoore.persistence.mapper;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.mrmoore.config.Constants;
import com.mrmoore.model.domain.StatusChangeDO;
import com.mrmoore.persistence.entity.StatusEntity;
import com.mrmoore.persistence.entity.VisitorEntity;
import com.sun.istack.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.MANDATORY, readOnly = true)
public class StatusMapper {

    @PersistenceContext
    private EntityManager entityManager;

    public StatusChangeDO mapToObject(@NotNull StatusEntity statusEntity) {
        StatusChangeDO statusChange = new StatusChangeDO();
        statusChange.setActive(statusEntity.getActive());
        statusChange.setLocalDateTime(LocalDateTime.ofInstant(statusEntity.getTimestamp().toInstant(),
                Constants.ZONE_ID));
        return statusChange;
    }

    public List<StatusChangeDO> mapToObjects(Set<StatusEntity> statusEntitySet) {
        if (statusEntitySet.isEmpty()) {
            return Collections.emptyList();
        }
        return statusEntitySet.stream().map(this::mapToObject).toList();
    }

    public StatusEntity mapToEntity(StatusChangeDO statusChange, Long visitId) {
        VisitorEntity visitorEntity = entityManager.getReference(VisitorEntity.class, visitId);
        StatusEntity statusEntity = new StatusEntity();
        statusEntity.setVisitorEntity(visitorEntity);
        statusEntity.setActive(statusChange.getActive());
        statusEntity.setTimestamp(Timestamp.valueOf(statusChange.getLocalDateTime()));
        return statusEntity;
    }

    public StatusEntity mapToEntity(StatusChangeDO statusChange, VisitorEntity newVisitorEntity) {
        StatusEntity statusEntity = new StatusEntity();
        statusEntity.setVisitorEntity(newVisitorEntity);
        statusEntity.setActive(statusChange.getActive());
        statusEntity.setTimestamp(Timestamp.valueOf(statusChange.getLocalDateTime()));
        return statusEntity;
    }

    public Set<StatusEntity> mapToEntities(List<StatusChangeDO> statusChange, Long visitId) {
        if (statusChange.isEmpty()) return Collections.emptySet();
        return statusChange.stream().map(l -> mapToEntity(l, visitId)).collect(Collectors.toSet());
    }

    public Set<StatusEntity> mapToEntities(List<StatusChangeDO> statusChange, VisitorEntity newVisitorEntity) {
        if (statusChange.isEmpty()) return Collections.emptySet();
        return statusChange.stream().map(l -> mapToEntity(l, newVisitorEntity)).collect(Collectors.toSet());
    }
}
