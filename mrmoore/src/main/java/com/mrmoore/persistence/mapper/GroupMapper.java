package com.mrmoore.persistence.mapper;

import com.mrmoore.model.domain.GroupDO;
import com.mrmoore.persistence.entity.GroupEntity;
import com.sun.istack.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.MANDATORY, readOnly = true)
public class GroupMapper {
    private final VisitorMapper visitorMapper;

    public GroupMapper(VisitorMapper visitorMapper) {
        this.visitorMapper = visitorMapper;
    }

    public GroupDO mapToObject(@NotNull GroupEntity groupEntity) {
        GroupDO group = new GroupDO();
        group.setId(groupEntity.getId());
        group.setComment(groupEntity.getComment());
        group.setVisitorList(visitorMapper.mapToObjects(groupEntity.getVisitors()));
        return group;
    }

    public GroupEntity mapToEntity(@NotNull GroupDO group) {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setId(group.getId());
        groupEntity.setComment(group.getComment());
        groupEntity.setVisitors(visitorMapper.mapToEntities(group.getVisitorList(), groupEntity));
        return groupEntity;
    }
}
