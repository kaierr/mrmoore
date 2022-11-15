package com.mrmoore.web.api;

import java.util.List;

import com.mrmoore.model.domain.GroupDO;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GroupInfoResponse {
    private Long id;
    private String comment;
    private Boolean isActive;
    private List<VisitorInfoResponse> visitList;
    private Long price;

    public GroupInfoResponse(GroupDO group) {
        this.id = group.getId();
        this.comment = group.getComment();
        this.visitList = group.getVisitorList().stream().map(VisitorInfoResponse::new).toList();
        this.isActive = group.isActive();
        this.price = group.getPrice();
    }
}
