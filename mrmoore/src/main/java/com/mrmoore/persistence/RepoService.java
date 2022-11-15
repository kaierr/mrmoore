package com.mrmoore.persistence;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.mrmoore.model.domain.GroupDO;
import com.mrmoore.model.domain.StatusChangeDO;
import com.mrmoore.model.domain.VisitorDO;
import com.mrmoore.persistence.entity.GroupEntity;
import com.mrmoore.persistence.entity.StatusEntity;
import com.mrmoore.persistence.entity.VisitorEntity;
import com.mrmoore.persistence.mapper.GroupMapper;
import com.mrmoore.persistence.mapper.StatusMapper;
import com.mrmoore.persistence.mapper.VisitorMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RepoService {

    private final VisitorRepository visitorRepository;
    private final GroupRepository groupRepository;
    private final StatusRepository statusRepository;
    private final VisitorMapper visitorMapper;
    private final GroupMapper groupMapper;
    private final StatusMapper statusMapper;

    public RepoService(VisitorRepository visitorRepository, GroupRepository groupRepository,
                       StatusRepository statusRepository, VisitorMapper visitorMapper,
                       GroupMapper groupMapper, StatusMapper statusMapper) {
        this.visitorRepository = visitorRepository;
        this.groupRepository = groupRepository;
        this.visitorMapper = visitorMapper;
        this.statusRepository = statusRepository;
        this.groupMapper = groupMapper;
        this.statusMapper = statusMapper;
    }

    public VisitorDO getVisitorById(final long id) {
        return visitorRepository.findById(id)
                .map(visitorMapper::mapToObject)
                .orElseThrow();
    }

    public GroupDO getGroupById(final long id) {
        return groupRepository.findById(id)
                .map(groupMapper::mapToObject)
                .orElseThrow();
    }

    public GroupDO createNewGroup(GroupDO group) {
        GroupEntity groupEntity = groupMapper.mapToEntity(group);
        GroupEntity savedGroupEntity = groupRepository.saveAndFlush(groupEntity);
        return groupMapper.mapToObject(savedGroupEntity);
    }

    public VisitorDO createNewVisitor(VisitorDO visit, Long groupId) {
        VisitorEntity visitorEntity = visitorMapper.mapToEntity(visit, groupId);
        VisitorEntity savedVisitorEntity = visitorRepository.saveAndFlush(visitorEntity);
        return visitorMapper.mapToObject(savedVisitorEntity);
    }

    public StatusChangeDO createNewVisitorStatusChange(StatusChangeDO statusChangeDO, Long visitId) {
        StatusEntity statusEntity = statusMapper.mapToEntity(statusChangeDO, visitId);
        StatusEntity savedStatusEntity = statusRepository.saveAndFlush(statusEntity);
        return statusMapper.mapToObject(savedStatusEntity);
    }

    public List<GroupDO> getGroupsAfterTime(Date date) {
        return statusRepository.findByTimestampGreaterThan(date)
                .orElseThrow()
                .stream()
                .map(l -> groupMapper.mapToObject(l.getVisitorEntity().getGroupEntity()))
                .toList();
    }
}
