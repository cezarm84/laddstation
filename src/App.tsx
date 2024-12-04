import React, { useState, useEffect } from "react";
import axios from "axios";
import {
  Button,
  Typography,
  Container,
  Box,
  MenuItem,
  FormControl,
  Select,
  SelectChangeEvent,  // Import SelectChangeEvent for correct typing
} from "@mui/material";
import "./App.css";

const App: React.FC = () => {
  const [prices, setPrices] = useState<number[]>([]);
  const [usage, setUsage] = useState<number[]>([]);
  const [battery, setBattery] = useState<number>(20);
  const [optimizationMessage, setOptimizationMessage] = useState<string>("");
  const [chargingData, setChargingData] = useState<any>(null); // To store charging data
  const [selectedPriceHour, setSelectedPriceHour] = useState<number>(0);
  const [selectedUsageHour, setSelectedUsageHour] = useState<number>(0);

  useEffect(() => {
    // Fetch energy prices and usage when the component mounts
    axios.get("http://localhost:8080/api/prices").then((res) => setPrices(res.data));
    axios.get("http://localhost:8080/api/baseload").then((res) => setUsage(res.data));
  }, []);

  const handleCharging = (action: string) => {
    axios.post("http://localhost:8080/api/battery/charge/" + action).then((res) => {
      setBattery(res.data.currentCharge);
      setChargingData(res.data); // Store the charging data in state
    });
  };

  const handleOptimization = (mode: string) => {
    axios.get(`http://localhost:8080/api/optimization?mode=${mode}`).then((res) => {
      setOptimizationMessage(res.data); // Display the optimization result
    });
  };

  // Update the function signature to accept SelectChangeEvent<number>
  const handlePriceChange = (event: SelectChangeEvent<number>) => {
    setSelectedPriceHour(event.target.value as number);
  };

  // Update the function signature to accept SelectChangeEvent<number>
  const handleUsageChange = (event: SelectChangeEvent<number>) => {
    setSelectedUsageHour(event.target.value as number);
  };

  return (
    <Container className="container">
      <Typography variant="h4" color="text.primary" gutterBottom>
        Battery Management - Charging Optimization
      </Typography>

      <Box className="table-section">
        <Typography variant="h6">Hourly Energy Prices (SEK):</Typography>
        <FormControl fullWidth>
          <Select
            value={selectedPriceHour}
            onChange={handlePriceChange}  // This will work now with SelectChangeEvent
            displayEmpty
            inputProps={{ "aria-label": "Select Hour" }}
          >
            {prices.map((price, index) => (
              <MenuItem key={index} value={index}>
                Hour {index + 1}: {price.toFixed(2)} SEK
              </MenuItem>
            ))}
          </Select>
        </FormControl>
      </Box>

      <Box className="table-section">
        <Typography variant="h6">Hourly Energy Usage (kW):</Typography>
        <FormControl fullWidth>
          <Select
            value={selectedUsageHour}
            onChange={handleUsageChange}  // This will work now with SelectChangeEvent
            displayEmpty
            inputProps={{ "aria-label": "Select Hour" }}
          >
            {usage.map((value, index) => (
              <MenuItem key={index} value={index}>
                Hour {index + 1}: {value.toFixed(2)} kW
              </MenuItem>
            ))}
          </Select>
        </FormControl>
      </Box>

      <Typography variant="h6">Battery Status:</Typography>
      <Typography variant="body1">{`Battery: ${battery}%`}</Typography>

      {/* Show charging data if available */}
      {chargingData && (
        <Box className="charging-details">
          <Typography variant="h6">Charging Details:</Typography>
          <Typography variant="body1">{`Current Charge: ${chargingData.currentCharge}%`}</Typography>
          <Typography variant="body1">{`Max Capacity: ${chargingData.maxCapacity}%`}</Typography>
          <Typography variant="body1">{`Is Charging: ${chargingData.isCharging ? "Yes" : "No"}`}</Typography>
          <Typography variant="body1">{`Remaining Time: ${chargingData.remainingTime} hours`}</Typography>
          <Typography variant="body1">
            {`Current Price: ${chargingData.energyPrices && chargingData.energyPrices[0] ? chargingData.energyPrices[0] : "N/A"} SEK`}
          </Typography>
          <Typography variant="body1">
            {`Baseload: ${chargingData.baseload && chargingData.baseload[0] ? chargingData.baseload[0] : "N/A"} kW`}
          </Typography>
        </Box>
      )}

      <Box className="charging-buttons">
        <Button
          variant="contained"
          color="primary"
          onClick={() => handleCharging("start")}
        >
          Start Charging
        </Button>
        <Button variant="contained" color="secondary" onClick={() => handleCharging("stop")}>
          Stop Charging
        </Button>
      </Box>

      <Box className="optimization-buttons">
        <Typography variant="h6">Optimization:</Typography>
        <Button variant="contained" onClick={() => handleOptimization("low-energy")}>
          Optimize for Low Energy Usage
        </Button>
        <Button variant="contained" onClick={() => handleOptimization("low-price")}>
          Optimize for Low Price
        </Button>
        <Typography variant="body1" className="optimization-message">
          {optimizationMessage}
        </Typography>
      </Box>
    </Container>
  );
};

export default App;
