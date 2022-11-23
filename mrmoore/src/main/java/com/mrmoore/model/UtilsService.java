package com.mrmoore.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import com.mrmoore.config.Constants;
import com.mrmoore.config.HolidaysList;
import com.mrmoore.config.PriceDistribution;
import org.springframework.stereotype.Service;

@Service
public class UtilsService {

    public final PriceDistribution priceDistribution;
    private final HolidaysList holidaysList;

    public UtilsService(PriceDistribution priceDistribution, HolidaysList holidaysList) {
        this.priceDistribution = priceDistribution;
        this.holidaysList = holidaysList;
    }

    public static LocalDateTime getCurrentTime() {
        Instant instant = Instant.now();
        return LocalDateTime.ofInstant(instant, Constants.ZONE_ID);
    }

    public Boolean isHolidayOrWeekend(LocalDateTime dateTime) {
        Date date = Date.from(dateTime.atZone(Constants.ZONE_ID).toInstant());
        return isHolidayOrWeekend(date);
    }
    public Boolean isHolidayOrWeekend(Date date) {
        return isHoliday(date)
                || isWeekend(date);
    }

    private Boolean isHoliday(Date date) {
        return holidaysList.getHolidayDates().contains(date);
    }

    private Boolean isWeekend(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
                || (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
    }
}
