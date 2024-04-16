package com.api.sportanalytics.request;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@NoArgsConstructor
public class RutinaParameter {
    private Integer max_new_tokens;
    private Double temperature;

    // Getter y Setter
}