package com.example.laddstation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChargingResponse {

    @JsonProperty("currentCharge")
    private double currentCharge;

    @JsonProperty("maxCapacity")
    private double maxCapacity;

    @JsonProperty("isCharging")
    private boolean isCharging;

    @JsonProperty("remainingTime")
    private double remainingTime;

    @JsonProperty("energyPrices")
    private double[] energyPrices;

    @JsonProperty("baseload")
    private double[] baseload;

    // Constructor
    public ChargingResponse(
            @JsonProperty("currentCharge") double currentCharge,
            @JsonProperty("maxCapacity") double maxCapacity,
            @JsonProperty("isCharging") boolean isCharging,
            @JsonProperty("remainingTime") double remainingTime,
            @JsonProperty("energyPrices") double[] energyPrices,
            @JsonProperty("baseload") double[] baseload, Object o) {
        this.currentCharge = currentCharge;
        this.maxCapacity = maxCapacity;
        this.isCharging = isCharging;
        this.remainingTime = remainingTime;
        this.energyPrices = energyPrices;
        this.baseload = baseload;
    }
}
