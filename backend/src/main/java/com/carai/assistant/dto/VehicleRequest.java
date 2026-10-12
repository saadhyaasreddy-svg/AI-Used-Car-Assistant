package com.carai.assistant.dto;

import jakarta.validation.constraints.*;

public class VehicleRequest {
    @NotBlank private String brand;
    @NotBlank private String model;
    @Min(1990) @Max(2026) private int year;
    @Min(0) private long mileage;
    @NotBlank private String fuelType;
    @NotBlank private String transmission;
    @Min(1) @Max(10) private int owners;
    @Positive private double askingPrice;
    @NotBlank private String serviceHistory;
    @NotBlank private String accidentHistory;
    @NotBlank private String engineCondition;
    @NotBlank private String acCondition;
    @NotBlank private String brakeCondition;
    @NotBlank private String tyreCondition;

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public long getMileage() { return mileage; }
    public void setMileage(long mileage) { this.mileage = mileage; }
    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }
    public String getTransmission() { return transmission; }
    public void setTransmission(String transmission) { this.transmission = transmission; }
    public int getOwners() { return owners; }
    public void setOwners(int owners) { this.owners = owners; }
    public double getAskingPrice() { return askingPrice; }
    public void setAskingPrice(double askingPrice) { this.askingPrice = askingPrice; }
    public String getServiceHistory() { return serviceHistory; }
    public void setServiceHistory(String serviceHistory) { this.serviceHistory = serviceHistory; }
    public String getAccidentHistory() { return accidentHistory; }
    public void setAccidentHistory(String accidentHistory) { this.accidentHistory = accidentHistory; }
    public String getEngineCondition() { return engineCondition; }
    public void setEngineCondition(String engineCondition) { this.engineCondition = engineCondition; }
    public String getAcCondition() { return acCondition; }
    public void setAcCondition(String acCondition) { this.acCondition = acCondition; }
    public String getBrakeCondition() { return brakeCondition; }
    public void setBrakeCondition(String brakeCondition) { this.brakeCondition = brakeCondition; }
    public String getTyreCondition() { return tyreCondition; }
    public void setTyreCondition(String tyreCondition) { this.tyreCondition = tyreCondition; }
}
