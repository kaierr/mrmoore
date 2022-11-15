package com.mrmoore.config;

import java.util.List;

import com.mrmoore.model.VisitorType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "price-distribution")
public class PriceDistribution {

    @Value("${price-distribution.event-price}")
    private int eventPrice;
    @Value("${price-distribution.weekend-limit}")
    private int weekendLimit;
    @Value("${price-distribution.workday-limit}")
    private int workdayLimit;
    private List<Visitor> visitors;

    @Getter
    @Setter
    public static class Visitor {
        private VisitorType type;
        private List<PriceDistributionEntry> prices;
    }

    @Getter
    @Setter
    public static class PriceDistributionEntry {
        private int duration;
        private int price;
    }
}
