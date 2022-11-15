package com.mrmoore.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mrmoore.model.domain.GroupDO;
import com.mrmoore.model.domain.StatusChangeDO;
import com.mrmoore.model.domain.VisitorDO;
import com.mrmoore.persistence.RepoService;
import com.sun.istack.NotNull;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VisitorService {

    private final RepoService repoService;

    public VisitorService(RepoService repoService) {
        this.repoService = repoService;
    }

    @NonNull
    @Transactional(readOnly = true)
    public VisitorDO getVisitorInfo(Long id) {
        return repoService.getVisitorById(id);
    }

    @NonNull
    @Transactional(readOnly = true)
    public List<GroupDO> getVisitInfoList() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, -1);
        return repoService.getGroupsAfterTime(c.getTime());
    }

    @NonNull
    @Transactional
    public GroupDO createNewGroup(@NotNull String comment, Map<VisitorType, Integer> visitors) {
        List<VisitorDO> visitorsList = new ArrayList<>();
        for (Entry<VisitorType, Integer> entry:visitors.entrySet()){
            for (int i = 0; i < entry.getValue(); i++) {
                VisitorDO visitor = new VisitorDO();
                visitor.setVisitorType(entry.getKey());
                visitor.setStatusChanges(List.of(new StatusChangeDO(true, UtilsService.getCurrentTime())));
                visitorsList.add(visitor);
            }
        }

        GroupDO group = new GroupDO();
        group.setComment(comment);
        group.setVisitorList(visitorsList);


        return repoService.createNewGroup(group);
    }

    @NonNull
    @Transactional(readOnly = true)
    public GroupDO getGroupInfo(@NotNull Long groupId) {
        return repoService.getGroupById(groupId);
    }

    public GroupDO changeGroupStatus(Long id, Boolean status) {
        GroupDO group = repoService.getGroupById(id);
        group.getVisitorList().forEach(l -> changeVisitorStatus(l.getId(), status));
        return group;
    }

    @NonNull
    @Transactional
    public VisitorDO changeVisitorStatus(Long id, Boolean status) {
        VisitorDO visit = repoService.getVisitorById(id);
        if (visit.getCurrentStatus().getActive().equals(status))
            return visit;

        StatusChangeDO newStatusDO = new StatusChangeDO();
        newStatusDO.setLocalDateTime(UtilsService.getCurrentTime());
        newStatusDO.setActive(status);

        repoService.createNewVisitorStatusChange(newStatusDO, id);

        return repoService.getVisitorById(id);
    }


}
