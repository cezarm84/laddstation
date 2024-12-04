package com.example.laddstation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BatteryController {

    @Autowired
    private ChargingService chargingService;

    // Get battery status
    @GetMapping(value = "/battery/status", produces = "application/json")
    public BatteryStatus getBatteryStatus() {
        return chargingService.getBatteryStatus();
    }

    @PostMapping(value = "/battery/charge/start", produces = "application/json")
    public ChargingResponse startCharging() {
        return chargingService.startCharging();
    }

    @PostMapping("/battery/charge/stop")
    public BatteryStatus stopCharging() {
        chargingService.stopCharging();
        return chargingService.getBatteryStatus();
    }
    @GetMapping("/prices")
    public double[] getEnergyPrices() {
        return chargingService.getEnergyPrices();
    }

    @GetMapping("/baseload")
    public double[] getBaseLoad() {
        return chargingService.getBaseLoad();
    }

    @GetMapping("/optimization")
    public String optimizeCharging(@RequestParam String mode) {
        return chargingService.optimizeCharging(mode);
    }
}