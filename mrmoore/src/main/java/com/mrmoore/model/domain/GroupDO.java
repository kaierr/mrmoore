package com.mrmoore.model.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupDO {
    private Long id;
    private String comment;
    private List<VisitorDO> visitorList;

    public Long getPrice() {
        return getVisitorList()
                .stream()
                .map(VisitorDO::getPrice)
                .mapToLong(Long::longValue)
                .sum();
    }

    public Boolean isActive() {
        return visitorList.stream().allMatch(l-> l.isActive().equals(false));
    }
}
