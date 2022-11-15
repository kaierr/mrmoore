package com.mrmoore.web.api;

import java.util.Map;

import com.mrmoore.model.VisitorType;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class NewGroupRequest {
    private String comment;
    private Map<VisitorType, Integer> visitors;
}
