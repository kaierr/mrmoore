package com.mrmoore.web;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import java.util.List;

import com.mrmoore.model.VisitorService;
import com.mrmoore.model.domain.GroupDO;
import com.mrmoore.web.api.ChangeStatusRequest;
import com.mrmoore.web.api.GroupInfoResponse;
import com.mrmoore.web.api.NewGroupRequest;
import com.mrmoore.web.api.VisitorInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/v1")
@RequiredArgsConstructor
public class VisitController {
    @Autowired
    private VisitorService visitService;


    @PostMapping(value ="/group", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public GroupInfoResponse createNewVisit(@RequestBody NewGroupRequest request) {
        GroupDO group = visitService.createNewGroup(request.getComment(), request.getVisitors());
        return new GroupInfoResponse(group);
    }

    @GetMapping(value = "/group/{groupId}", produces = APPLICATION_JSON_VALUE)
    public GroupInfoResponse getGroupInfo(@PathVariable Long groupId){
        return new GroupInfoResponse(visitService.getGroupInfo(groupId));
    }

    @GetMapping(value = "/visitor/{visitorId}", produces = APPLICATION_JSON_VALUE)
    public VisitorInfoResponse getVisitorInfo(@PathVariable Long visitorId) {
        return new VisitorInfoResponse(visitService.getVisitorInfo(visitorId));
    }

    @GetMapping(value = "/list", produces = APPLICATION_JSON_VALUE)
    public List<GroupInfoResponse> getAllVisits() {
        return visitService.getVisitInfoList().stream().map(GroupInfoResponse::new).toList();
    }

    @PostMapping(value = "/status/group", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public GroupInfoResponse changeGroupStatus(@RequestBody ChangeStatusRequest request) {
        return new GroupInfoResponse(visitService.changeGroupStatus(request.getId(), request.getActive()));
    }

    @PostMapping(value = "/status/visitor", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public VisitorInfoResponse changeVisitorStatus(@RequestBody ChangeStatusRequest request) {
        return new VisitorInfoResponse(visitService.changeVisitorStatus(request.getId(), request.getActive()));
    }
}
