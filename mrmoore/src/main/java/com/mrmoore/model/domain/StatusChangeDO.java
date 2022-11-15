package com.mrmoore.model.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatusChangeDO implements Comparable<StatusChangeDO> {
    private Boolean active;
    private LocalDateTime localDateTime;

    @Override
    public int compareTo(StatusChangeDO anotherStatus) {
        return localDateTime.compareTo(anotherStatus.getLocalDateTime());
    }
}
