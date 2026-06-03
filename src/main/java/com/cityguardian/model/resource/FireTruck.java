package com.cityguardian.model.resource;

import com.cityguardian.model.Citizen;
import com.cityguardian.model.Tile;
import java.util.List;

public class FireTruck extends EmergencyResource {
    private Citizen targetCitizen;
    private Tile targetTile;
    private double currentX;
    private double currentY;
    private double speed = 3.0; // Needs roads
    
    private List<Tile> path;
    private int pathIndex = 0;

    public FireTruck(String id, int startX, int startY) {
        super(id, startX, startY);
        this.currentX = startX;
        this.currentY = startY;
    }

    public Citizen getTargetCitizen() { return targetCitizen; }
    public void setTargetCitizen(Citizen targetCitizen) { this.targetCitizen = targetCitizen; }
    
    public Tile getTargetTile() { return targetTile; }
    public void setTargetTile(Tile targetTile) { this.targetTile = targetTile; }

    public double getCurrentX() { return currentX; }
    public double getCurrentY() { return currentY; }
    public void setCurrentPos(double x, double y) {
        this.currentX = x;
        this.currentY = y;
    }
    public double getSpeed() { return speed; }

    public List<Tile> getPath() { return path; }
    public void setPath(List<Tile> path) {
        this.path = path;
        this.pathIndex = 0;
    }
    
    public int getPathIndex() { return pathIndex; }
    public void advancePath() { pathIndex++; }

    @Override
    public void performAction() {
        if (targetCitizen != null) {
            targetCitizen.setEvacuated(true);
            this.targetCitizen = null;
        }
        this.targetTile = null;
        this.setStatus(Status.AVAILABLE);
    }
}
