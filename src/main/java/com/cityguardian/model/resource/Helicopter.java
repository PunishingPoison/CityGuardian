package com.cityguardian.model.resource;

import com.cityguardian.model.Citizen;
import java.util.List;
import com.cityguardian.model.Tile;

public class Helicopter extends EmergencyResource {
    private Citizen targetCitizen;
    private double currentX;
    private double currentY;
    private double speed = 5.0; // Fast, ignores roads

    public Helicopter(String id, int startX, int startY) {
        super(id, startX, startY);
        this.currentX = startX;
        this.currentY = startY;
    }

    public Citizen getTargetCitizen() { return targetCitizen; }
    public void setTargetCitizen(Citizen targetCitizen) { this.targetCitizen = targetCitizen; }

    public double getCurrentX() { return currentX; }
    public double getCurrentY() { return currentY; }
    public void setCurrentPos(double x, double y) {
        this.currentX = x;
        this.currentY = y;
    }
    public double getSpeed() { return speed; }

    @Override
    public void performAction() {
        if (targetCitizen != null) {
            targetCitizen.setEvacuated(true);
            this.targetCitizen = null;
            this.setStatus(Status.AVAILABLE);
        }
    }
}
