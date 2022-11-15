package com.mrmoore.model.domain;

import java.time.Instant;
import java.util.List;

import com.mrmoore.config.Constants;
import com.mrmoore.config.PriceDistribution;
import com.mrmoore.config.PriceDistribution.PriceDistributionEntry;
import com.mrmoore.config.SpringContext;
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
        PriceDistribution priceProperty = SpringContext.getBean(PriceDistribution.class);
        List<PriceDistributionEntry> priceDistribution =
                priceProperty.getVisitors().stream().filter(l -> l.getType().equals(getVisitorType()))
                        .findFirst().get().getPrices();

        long timeInMinutes = getTotalTimeInMinutes();
        return calculatePriceForTime(priceDistribution, timeInMinutes);
    }

    private long calculatePriceForTime(List<PriceDistributionEntry> priceDistribution, long timeInMinutes) {
        long totalPrice = 0L;
        long passedCalculationTime = 0L;
        for (int i = 0; i < priceDistribution.size() - 1; i++) {
            long periodDuration = Math.min(Math.max(0, timeInMinutes - passedCalculationTime),
                    priceDistribution.get(i).getDuration());
            totalPrice += periodDuration * priceDistribution.get(i).getPrice();
            passedCalculationTime += priceDistribution.get(i).getDuration();
        }
        totalPrice =
                totalPrice + Math.max(0, timeInMinutes - passedCalculationTime) * priceDistribution.get(priceDistribution.size() - 1).getPrice();
        return totalPrice;
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

    public StatusChangeDO getCurrentStatus() {
        return statusChanges.get(statusChanges.size() - 1);
    }

    private Long concatenateStatusChangeTime(Long timeDiff, StatusChangeDO statusChange) {
        long newTimeAdjustment = statusChange.getLocalDateTime().atZone(Constants.ZONE_ID).toInstant().toEpochMilli();
        if (statusChange.getActive().equals(true))
            timeDiff -= newTimeAdjustment;
        else
            timeDiff += newTimeAdjustment;
        return timeDiff;
    }
}
