package com.mrmoore.web.api;

import java.time.LocalDateTime;

import com.mrmoore.model.domain.StatusChangeDO;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ChangeStatusInResponse {
    private Boolean active;
    private LocalDateTime time;

    public ChangeStatusInResponse(StatusChangeDO statusChangeDO) {
        this.active = statusChangeDO.getActive();
        this.time = statusChangeDO.getLocalDateTime();
    }
}
