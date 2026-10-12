package com.carai.assistant.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicle_analyses")
public class VehicleAnalysis {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String brand;
    private String model;
    private int year;
    private long mileage;
    private String fuelType;
    private String transmission;
    private int owners;
    private double askingPrice;
    private String serviceHistory;
    private String accidentHistory;
    private String engineCondition;
    private String acCondition;
    private String brakeCondition;
    private String tyreCondition;
    private double estimatedMarketPrice;
    private double estimatedRepairCost;
    private double effectiveBuyingCost;
    private int healthScore;
    private String healthLabel;
    private String recommendation;
    @Column(length = 1000) private String recommendationReason;
    private LocalDateTime createdAt;

    @PrePersist
    public void beforeSave() { if (createdAt == null) createdAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public double getEstimatedMarketPrice() { return estimatedMarketPrice; }
    public void setEstimatedMarketPrice(double estimatedMarketPrice) { this.estimatedMarketPrice = estimatedMarketPrice; }
    public double getEstimatedRepairCost() { return estimatedRepairCost; }
    public void setEstimatedRepairCost(double estimatedRepairCost) { this.estimatedRepairCost = estimatedRepairCost; }
    public double getEffectiveBuyingCost() { return effectiveBuyingCost; }
    public void setEffectiveBuyingCost(double effectiveBuyingCost) { this.effectiveBuyingCost = effectiveBuyingCost; }
    public int getHealthScore() { return healthScore; }
    public void setHealthScore(int healthScore) { this.healthScore = healthScore; }
    public String getHealthLabel() { return healthLabel; }
    public void setHealthLabel(String healthLabel) { this.healthLabel = healthLabel; }
    public String getRecommendation() { return recommendation; }
    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }
    public String getRecommendationReason() { return recommendationReason; }
    public void setRecommendationReason(String recommendationReason) { this.recommendationReason = recommendationReason; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
