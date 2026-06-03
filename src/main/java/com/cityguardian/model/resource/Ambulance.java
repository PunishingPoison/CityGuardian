package com.cityguardian.model.resource;

import com.cityguardian.model.Citizen;
import com.cityguardian.model.Tile;
import java.util.ArrayList;
import java.util.List;

public class Ambulance extends EmergencyResource {
    private Citizen targetCitizen;
    private double currentX;
    private double currentY;
    private double speed = 10.0; // Ambulances are very fast
    
    private int capacity;
    private List<Citizen> loadedCitizens;
    private boolean returningToHospital = false;
    
    private List<Tile> path;
    private int pathIndex = 0;

    public Ambulance(String id, int startX, int startY, int capacity) {
        super(id, startX, startY);
        this.currentX = startX;
        this.currentY = startY;
        this.capacity = capacity;
        this.loadedCitizens = new ArrayList<>();
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

    public List<Tile> getPath() { return path; }
    public void setPath(List<Tile> path) {
        this.path = path;
        this.pathIndex = 0;
    }
    
    public int getPathIndex() { return pathIndex; }
    public void advancePath() { pathIndex++; }
    
    public int getCapacity() { return capacity; }
    public List<Citizen> getLoadedCitizens() { return loadedCitizens; }
    
    public boolean isFull() { return loadedCitizens.size() >= capacity; }
    
    public void loadCitizen(Citizen c) {
        if (!isFull() && !loadedCitizens.contains(c)) {
            loadedCitizens.add(c);
        }
    }
    
    public boolean isReturningToHospital() { return returningToHospital; }
    public void setReturningToHospital(boolean ret) { this.returningToHospital = ret; }

    @Override
    public void performAction() {
        if (returningToHospital) {
            // Arrived at hospital, unload everyone
            for (Citizen c : loadedCitizens) {
                c.setEvacuated(true);
            }
            loadedCitizens.clear();
            returningToHospital = false;
            this.setStatus(Status.AVAILABLE);
        } else {
            // In the field. Loading logic is handled by SimulationEngine.
            // If full, start returning to hospital
            if (isFull()) {
                returningToHospital = true;
                this.setStatus(Status.RETURNING);
            } else {
                this.setStatus(Status.AVAILABLE);
            }
        }
        this.targetCitizen = null;
    }
}
