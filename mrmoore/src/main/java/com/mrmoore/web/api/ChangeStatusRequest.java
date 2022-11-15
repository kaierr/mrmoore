package com.mrmoore.web.api;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ChangeStatusRequest {
    private Long id;
    private Boolean active;
}
