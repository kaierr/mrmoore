package com.mrmoore.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import com.mrmoore.config.Constants;
import com.mrmoore.config.PriceDistribution;
import com.mrmoore.config.PriceDistribution.PriceDistributionEntry;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class UtilsService {

    public final PriceDistribution priceDistribution;

    public UtilsService(PriceDistribution priceDistribution) {
        this.priceDistribution = priceDistribution;
    }

    public static LocalDateTime getCurrentTime() {
        Instant instant = Instant.now();
        return LocalDateTime.ofInstant(instant, Constants.ZONE_ID);
    }


}
