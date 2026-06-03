package com.cityguardian.model.disaster;

import com.cityguardian.model.City;
import com.cityguardian.model.Tile;

public abstract class Disaster {
    public enum Severity { LOW, MEDIUM, HIGH, CRITICAL }

    protected int startX;
    protected int startY;
    protected Severity severity;
    protected double spreadSpeed; // Tiles per second or tick
    protected int damageRadius;
    protected boolean active = true;

    public Disaster(int startX, int startY, Severity severity) {
        this.startX = startX;
        this.startY = startY;
        this.severity = severity;
    }

    public int getStartX() { return startX; }
    public int getStartY() { return startY; }
    public Severity getSeverity() { return severity; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    // Each disaster has its own spread logic
    public abstract void evolve(City city, double deltaTime);
}
