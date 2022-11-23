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
        return visitorList.stream().allMatch(l -> l.isActive().equals(false));
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GroupDO c)) {
            return false;
        }
        return getId().equals(c.getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
