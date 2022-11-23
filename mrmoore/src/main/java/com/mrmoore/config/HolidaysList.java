package com.mrmoore.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@PropertySource(value = "classpath:holidays.yml", factory = YamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "holidays-list")
public class HolidaysList {
    private List<String> dates;

    public List<Date> getHolidayDates() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        List<Date> result;
        result = dates.stream().map(l -> {
            try {
                return formatter.parse(l);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }).toList();
        return result;
    }
}
