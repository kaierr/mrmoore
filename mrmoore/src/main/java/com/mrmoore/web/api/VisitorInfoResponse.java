package com.mrmoore.web.api;

import java.util.ArrayList;
import java.util.List;

import com.mrmoore.model.VisitorType;
import com.mrmoore.model.domain.VisitorDO;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class VisitorInfoResponse {
    private Long id;
    private String name;
    private VisitorType visitorType;
    private List<ChangeStatusInResponse> statusChanges;
    private Long totalTime;
    private Long price;
    private Boolean isActive;

    public VisitorInfoResponse(VisitorDO visitorDO) {
        this.id = visitorDO.getId();
        this.name = visitorDO.getName();
        this.visitorType = visitorDO.getVisitorType();
        this.statusChanges = new ArrayList<>();
        visitorDO.getStatusChanges().forEach(l -> this.statusChanges.add(new ChangeStatusInResponse(l)));
        this.totalTime = visitorDO.getTotalTimeInMinutes();
        this.price = visitorDO.getPrice();
        this.isActive = visitorDO.isActive();
    }
}
