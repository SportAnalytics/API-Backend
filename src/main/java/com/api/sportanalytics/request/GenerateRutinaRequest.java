package com.api.sportanalytics.request;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@NoArgsConstructor
public class GenerateRutinaRequest {

    private String inputs;
    private Long id;

}

