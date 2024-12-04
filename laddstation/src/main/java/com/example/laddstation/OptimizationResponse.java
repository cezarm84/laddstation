package com.example.laddstation;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OptimizationResponse {
    // Getters and setters
    private String optimizationMode;
    private int[] optimizedHours;

    // Constructor
    public OptimizationResponse(String optimizationMode, int[] optimizedHours) {
        this.optimizationMode = optimizationMode;
        this.optimizedHours = optimizedHours;
    }

}
