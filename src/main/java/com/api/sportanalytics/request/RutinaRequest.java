package com.api.sportanalytics.request;

import java.util.Map;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
@Setter
@Getter
@NoArgsConstructor
public class RutinaRequest {
    private String inputs;
    private Map<String, Object> parameters;
    
    // Getter y Setter
}
