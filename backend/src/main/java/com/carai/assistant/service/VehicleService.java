package com.carai.assistant.service;

import com.carai.assistant.dto.VehicleRequest;
import com.carai.assistant.entity.VehicleAnalysis;
import com.carai.assistant.repository.VehicleRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.Year;
import java.util.List;
import java.util.Locale;

@Service
public class VehicleService {
    private final VehicleRepository repository;
    public VehicleService(VehicleRepository repository) { this.repository = repository; }

    public VehicleAnalysis analyze(VehicleRequest r) {
        VehicleAnalysis v = new VehicleAnalysis();
        v.setBrand(r.getBrand().trim()); v.setModel(r.getModel().trim()); v.setYear(r.getYear());
        v.setMileage(r.getMileage()); v.setFuelType(r.getFuelType()); v.setTransmission(r.getTransmission());
        v.setOwners(r.getOwners()); v.setAskingPrice(r.getAskingPrice()); v.setServiceHistory(r.getServiceHistory());
        v.setAccidentHistory(r.getAccidentHistory()); v.setEngineCondition(r.getEngineCondition());
        v.setAcCondition(r.getAcCondition()); v.setBrakeCondition(r.getBrakeCondition()); v.setTyreCondition(r.getTyreCondition());

        int age = Math.max(0, Year.now().getValue() - r.getYear());
        // Demo-only baseline; replace with local market data or a trained model for real valuation.
        double market = 800000.0 * Math.pow(0.91, age);
        market *= Math.max(0.55, 1.0 - Math.max(0, r.getMileage() - 40000) / 250000.0);
        market *= switch (r.getFuelType().toLowerCase(Locale.ROOT)) {
            case "diesel" -> 1.05; case "electric" -> 1.15; case "cng" -> 0.92; default -> 1.0;
        };
        market *= switch (r.getTransmission().toLowerCase(Locale.ROOT)) { case "automatic" -> 1.04; default -> 1.0; };
        market *= Math.max(0.72, 1.0 - Math.max(0, r.getOwners() - 1) * 0.045);
        market *= serviceFactor(r.getServiceHistory());
        market *= conditionFactor(r.getEngineCondition());
        if ("Minor".equalsIgnoreCase(r.getAccidentHistory())) market *= 0.90;
        if ("Major".equalsIgnoreCase(r.getAccidentHistory())) market *= 0.72;
        if ("Unknown".equalsIgnoreCase(r.getAccidentHistory())) market *= 0.94;
        market = roundToThousand(Math.max(50000, market));

        double repairs = repairCost(r.getEngineCondition(), 45000)
                + repairCost(r.getAcCondition(), 18000)
                + repairCost(r.getBrakeCondition(), 10000)
                + repairCost(r.getTyreCondition(), 20000);
        if ("Partial".equalsIgnoreCase(r.getServiceHistory())) repairs += 8000;
        if ("Unavailable".equalsIgnoreCase(r.getServiceHistory())) repairs += 15000;
        if ("Minor".equalsIgnoreCase(r.getAccidentHistory())) repairs += 15000;
        if ("Major".equalsIgnoreCase(r.getAccidentHistory())) repairs += 60000;
        if ("Unknown".equalsIgnoreCase(r.getAccidentHistory())) repairs += 10000;
        repairs = roundToThousand(repairs);

        int score = 100;
        score -= conditionPenalty(r.getEngineCondition(), 30);
        score -= conditionPenalty(r.getAcCondition(), 12);
        score -= conditionPenalty(r.getBrakeCondition(), 18);
        score -= conditionPenalty(r.getTyreCondition(), 10);
        score -= switch (r.getServiceHistory().toLowerCase(Locale.ROOT)) { case "partial" -> 8; case "unavailable" -> 15; default -> 0; };
        score -= switch (r.getAccidentHistory().toLowerCase(Locale.ROOT)) { case "minor" -> 8; case "major" -> 25; case "unknown" -> 10; default -> 0; };
        score -= Math.max(0, r.getOwners() - 1) * 3;
        score -= r.getMileage() > 150000 ? 10 : r.getMileage() > 100000 ? 5 : 0;
        score = Math.max(0, Math.min(100, score));

        String label = score >= 85 ? "Excellent" : score >= 70 ? "Good" : score >= 50 ? "Fair" : "Poor";
        double effective = roundToThousand(r.getAskingPrice() + repairs);
        String recommendation; String reason;
        if (score < 50 || "Major".equalsIgnoreCase(r.getAccidentHistory())) {
            recommendation = "Needs careful inspection";
            reason = "The reported condition or accident history presents higher risk. Get a detailed independent inspection before making an offer.";
        } else if (effective > market * 1.10) {
            recommendation = "Negotiate";
            reason = "The asking price plus estimated repairs is notably above this demo's estimated market value. Negotiate or compare similar local listings.";
        } else {
            recommendation = "Good candidate";
            reason = "The reported details look comparatively reasonable under this demo model. Confirm documents, service records and mechanical condition before buying.";
        }
        v.setEstimatedMarketPrice(market); v.setEstimatedRepairCost(repairs); v.setEffectiveBuyingCost(effective);
        v.setHealthScore(score); v.setHealthLabel(label); v.setRecommendation(recommendation); v.setRecommendationReason(reason);
        return repository.save(v);
    }

    public List<VehicleAnalysis> getAll() { return repository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")); }
    private double repairCost(String condition, double base) {
        return switch (condition.toLowerCase(Locale.ROOT)) {
            case "excellent" -> 0; case "good" -> base * 0.10; case "average" -> base * 0.45; case "poor" -> base; default -> base * 0.25;
        };
    }
    private int conditionPenalty(String condition, int max) {
        return switch (condition.toLowerCase(Locale.ROOT)) {
            case "excellent" -> 0; case "good" -> (int)Math.round(max * 0.15); case "average" -> (int)Math.round(max * 0.50); case "poor" -> max; default -> (int)Math.round(max * 0.25);
        };
    }
    private double serviceFactor(String history) {
        return switch (history.toLowerCase(Locale.ROOT)) { case "complete" -> 1.0; case "partial" -> 0.92; default -> 0.85; };
    }
    private double conditionFactor(String condition) {
        return switch (condition.toLowerCase(Locale.ROOT)) { case "excellent" -> 1.05; case "good" -> 1.0; case "average" -> 0.90; case "poor" -> 0.75; default -> 0.90; };
    }
    private double roundToThousand(double amount) { return Math.round(amount / 1000.0) * 1000.0; }
}
