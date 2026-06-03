package com.cityguardian.model;

import com.cityguardian.model.building.Building;

public class Tile {
    private final int x;
    private final int y;
    private TileType type;
    private Building building; // Can be null if it's a road or empty
    private double riskLevel = 0.0; // 0.0 (safe) to 1.0 (critical)
    private boolean hasDisaster = false;
    private double elevation = 0.0;

    public Tile(int x, int y, TileType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public TileType getType() { return type; }
    public void setType(TileType type) { this.type = type; }

    public Building getBuilding() { return building; }
    public void setBuilding(Building building) { this.building = building; }

    public double getRiskLevel() { return riskLevel; }
    public void setRiskLevel(double riskLevel) { this.riskLevel = riskLevel; }

    public boolean hasDisaster() { return hasDisaster; }
    public void setHasDisaster(boolean hasDisaster) { this.hasDisaster = hasDisaster; }

    public double getElevation() { return elevation; }
    public void setElevation(double elevation) { this.elevation = elevation; }

    public boolean isWalkable() {
        return type == TileType.ROAD || type == TileType.EMPTY || type == TileType.HOSPITAL || type == TileType.SHELTER;
    }
}
