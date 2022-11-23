package com.mrmoore.model.domain;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.mrmoore.config.Constants;
import com.mrmoore.config.PriceDistribution;
import com.mrmoore.config.PriceDistribution.PriceDistributionEntry;
import com.mrmoore.config.SpringContext;
import com.mrmoore.model.PriceService;
import com.mrmoore.model.VisitorType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VisitorDO {
    private Long id;
    private VisitorType visitorType;
    private String name;
    private List<StatusChangeDO> statusChanges;

    public Boolean isActive() {
        return getStatusChanges().get(getStatusChanges().size()-1).getActive();
    }

    public Long getPrice() {
        PriceService priceService = SpringContext.getBean(PriceService.class);
        return priceService.calculatePrice(this);
    }



    public StatusChangeDO getCurrentStatus() {
        return statusChanges.get(statusChanges.size() - 1);
    }

    public LocalDateTime getStartDate() {
        return statusChanges.get(0).getLocalDateTime();
    }

    public Long getTotalTimeInMinutes() {
        long timeDiff = concatenateStatusChangeTime(0L, statusChanges.get(0));
        for (int i = 1; i < statusChanges.size(); i++) {
            timeDiff = concatenateStatusChangeTime(timeDiff, statusChanges.get(i));
        }
        if (getCurrentStatus().getActive().equals(true)) {
            timeDiff += Instant.now().toEpochMilli();
        }
        return timeDiff / 60000;
    }

    private Long concatenateStatusChangeTime(Long timeDiff, StatusChangeDO statusChange) {
        long newTimeAdjustment = statusChange.getLocalDateTime().atZone(Constants.ZONE_ID).toInstant().toEpochMilli();
        if (statusChange.getActive().equals(true))
            timeDiff -= newTimeAdjustment;
        else
            timeDiff += newTimeAdjustment;
        return timeDiff;
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
