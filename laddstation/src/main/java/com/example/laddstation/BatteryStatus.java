package com.example.laddstation;
import lombok.Getter;

@Getter
public record BatteryStatus(double currentCharge, double maxCapacity, boolean isCharging) {
    // Constructor
}
