package com.mrmoore.model;

import java.time.LocalDateTime;
import java.util.List;

import com.mrmoore.config.PriceDistribution;
import com.mrmoore.config.PriceDistribution.PriceDistributionEntry;
import com.mrmoore.model.domain.VisitorDO;
import org.springframework.stereotype.Service;

@Service
public class PriceService {
    private final PriceDistribution priceProperty;
    private final UtilsService utilsService;

    public PriceService(PriceDistribution priceProperty, UtilsService utilsService) {
        this.priceProperty = priceProperty;
        this.utilsService = utilsService;
    }

    public Long calculatePrice(VisitorDO visitor) {
        Long resultPrice;
        switch (visitor.getVisitorType()) {
            case UNLIMITED -> resultPrice = calculatePriceForUnlimited(visitor.getStartDate());
            default ->
                    resultPrice = Math.min(calculatePriceForTimeInterval(visitor.getVisitorType(), visitor.getTotalTimeInMinutes()),
                            getMaxPriceForDay(visitor.getStartDate()));
        }
        return resultPrice;
    }

    public Long getMaxPriceForDay(LocalDateTime dateTime) {
        if (utilsService.isHolidayOrWeekend(dateTime)) return (long) priceProperty.getWeekendLimit();
        else return (long) priceProperty.getWorkdayLimit();
    }

    private Long calculatePriceForUnlimited(LocalDateTime dateTime) {
        return getMaxPriceForDay(dateTime);
    }

    private Long calculatePriceForTimeInterval(VisitorType visitorType, long timeInMinutes) {
        List<PriceDistributionEntry> priceDistribution =
                priceProperty.getVisitors().stream().filter(l -> l.getType().equals(visitorType))
                        .findFirst().get().getPrices();

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
}
