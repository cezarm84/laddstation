package com.example.laddstation;

import org.springframework.stereotype.Service;
import java.util.stream.IntStream;

@Service
public class ChargingService {

    private double currentCharge = 20.0;  // Starting charge (20%)
    private final double maxCapacity = 100.0; // Max capacity (100%)
    private boolean isCharging = false;
    private final double chargingPower = 7.4;  // Charging power (kW)
    private final double threshold = 11.0;  // Max allowable load (kW)

    // Battery status
    public BatteryStatus getBatteryStatus() {
        return new BatteryStatus(currentCharge, maxCapacity, isCharging);
    }

    // Start charging
    public ChargingResponse startCharging() {
        if (isCharging) {
            return new ChargingResponse(
                    (int) Math.round(currentCharge),
                    maxCapacity,
                    isCharging,
                    0,
                    new double[]{},
                    new double[]{},
                    "Charging in progress"
            );
        }

        if (currentCharge < 80.0) {
            isCharging = true;
            double randomChargeIncrement = Math.random() * chargingPower;  //  random charge increment
            currentCharge += randomChargeIncrement;

            if (currentCharge > 80.0) {
                currentCharge = 80.0;
            }

            double chargeNeeded = 80.0 - currentCharge;
            double timeRequired = chargeNeeded / chargingPower;
            int remainingTime = (int) Math.round(timeRequired);

            int currentHour = (int) (Math.random() * 12); // Simulating current hour

            double[] hourlyPrices = getEnergyPrices();
            double[] hourlyLoad = getBaseLoad();

            double currentPrice = hourlyPrices[currentHour % 12];
            double currentBaseload = hourlyLoad[currentHour % 12];

            return new ChargingResponse(
                    (int) Math.round(currentCharge),
                    maxCapacity,
                    isCharging,
                    remainingTime,
                    new double[]{currentPrice},
                    new double[]{currentBaseload},
                    null
            );
        } else {
            return new ChargingResponse(currentCharge, maxCapacity, isCharging, 0, new double[]{}, new double[]{}, "Already fully charged");
        }
    }

    // Stop charging
    public void stopCharging() {
        if (isCharging) {
            isCharging = false;
        }
    }

    // Optimize charging based on the mode (low-energy or low-price)
    public String optimizeCharging(String mode) {
        double[] hourlyPrices = getEnergyPrices();
        double[] hourlyLoad = getBaseLoad();
        int[] optimizedHours;

        if ("low-energy".equals(mode)) {
            optimizedHours = optimizeForLowEnergy(hourlyLoad);  // Filter hours based on energy usage
        } else if ("low-price".equals(mode)) {
            optimizedHours = optimizeForLowPrice(hourlyPrices);  // Filter hours based on price
        } else {
            return "Invalid optimization mode.";
        }

        // Create a string to display the optimized hours (now hours are from 1 to 12
        StringBuilder optimizationResult = new StringBuilder("Optimal hours: ");
        for (int hour : optimizedHours) {
            optimizationResult.append(hour + 1).append(" ");  // +1 to 1-12
        }

        return optimizationResult.toString();
    }

    // Find hours with low energy consumption (below threshold)
    private int[] optimizeForLowEnergy(double[] hourlyLoad) {
        // Filter for hours where load is below the threshold (e.g., 3 kW)
        return IntStream.range(0, hourlyLoad.length)
                .filter(i -> hourlyLoad[i] <= 2)  // Only include hours where energy usage is below the threshold
                .toArray();  // Return the filtered hours as an integer array
    }



    // Find hours with low electricity price (below 1 SEK)
    private int[] optimizeForLowPrice(double[] hourlyPrices) {
        // Filter for hours where price is less than 1 SEK
        return IntStream.range(0, hourlyPrices.length)
                .filter(i -> hourlyPrices[i] <= 1.0)  // Only include hours where price is below 1 SEK
                .toArray();
    }

    // Mocked energy prices for each hour (in SEK per kWh)
    public double[] getEnergyPrices() {
        return new double[]{1.5, 1.2, 0.9, 0.8, 0.7, 1.0, 1.3, 1.8, 2.0, 1.9, 1.4, 1.33};
    }

    // Mocked energy usage per hour (household consumption in kW)
    public double[] getBaseLoad() {
        return new double[]{3.0, 2.5,7, 1.8, 1.5, 6, 1.7, 5, 2.4, 2.8, 3.2, 3.5};
    }
}
