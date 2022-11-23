package com.mrmoore.persistence.mapper;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.mrmoore.model.domain.VisitorDO;
import com.mrmoore.persistence.entity.GroupEntity;
import com.mrmoore.persistence.entity.VisitorEntity;
import com.sun.istack.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.MANDATORY, readOnly = true)
public class VisitorMapper {

    private final StatusMapper statusMapper;

    @PersistenceContext
    private EntityManager entityManager;

    public VisitorMapper(StatusMapper statusMapper) {
        this.statusMapper = statusMapper;
    }

    public VisitorDO mapToObject(@NotNull VisitorEntity visitorEntity) {
        VisitorDO visit = new VisitorDO();
        visit.setId(visitorEntity.getId());
        visit.setName(visitorEntity.getName());
        visit.setVisitorType(visitorEntity.getType());
        visit.setStatusChanges(statusMapper.mapToObjects(visitorEntity.getStatusEntitySet()).stream().sorted().toList());
        return visit;
    }


    public VisitorEntity mapToEntity(@NotNull VisitorDO visitor, Long groupId) {
        GroupEntity groupEntity = entityManager.getReference(GroupEntity.class, groupId);
        VisitorEntity visitorEntity = new VisitorEntity();
        visitorEntity.setName(visitor.getName());
        visitorEntity.setGroupEntity(groupEntity);
        visitorEntity.setType(visitor.getVisitorType());
        visitorEntity.setStatusEntitySet(statusMapper.mapToEntities(visitor.getStatusChanges(), visitorEntity));
        return visitorEntity;
    }

    public VisitorEntity mapToEntity(@NotNull VisitorDO visitor, GroupEntity groupEntity) {
        VisitorEntity visitorEntity = new VisitorEntity();
        visitorEntity.setName(visitor.getName());
        visitorEntity.setGroupEntity(groupEntity);
        visitorEntity.setType(visitor.getVisitorType());
        visitorEntity.setStatusEntitySet(statusMapper.mapToEntities(visitor.getStatusChanges(), visitorEntity));
        return visitorEntity;
    }

    public List<VisitorDO> mapToObjects(Set<VisitorEntity> visitorEntitySet) {
        if (visitorEntitySet.isEmpty())
            return Collections.emptyList();
        return visitorEntitySet.stream().map(this::mapToObject).toList();
    }

    public Set<VisitorEntity> mapToEntities(List<VisitorDO> visitors, GroupEntity newGroupEntity) {
        if (visitors.isEmpty())
            return Collections.emptySet();
        return visitors.stream().map(l -> mapToEntity(l, newGroupEntity)).collect(Collectors.toSet());
    }
}
