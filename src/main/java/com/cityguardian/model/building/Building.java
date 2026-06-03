package com.cityguardian.model.building;

public abstract class Building {
    private String name;
    private int capacity;
    private int currentOccupancy;
    private double structuralIntegrity = 100.0;

    public Building(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        this.currentOccupancy = 0;
    }

    public String getName() { return name; }
    public int getCapacity() { return capacity; }
    public int getCurrentOccupancy() { return currentOccupancy; }
    
    public void addOccupant() {
        if (currentOccupancy < capacity) {
            currentOccupancy++;
        }
    }
    
    public void removeOccupant() {
        if (currentOccupancy > 0) {
            currentOccupancy--;
        }
    }

    public double getStructuralIntegrity() { return structuralIntegrity; }
    public void setStructuralIntegrity(double structuralIntegrity) { 
        this.structuralIntegrity = Math.max(0, structuralIntegrity); 
    }
    
    public boolean isDestroyed() {
        return structuralIntegrity <= 0;
    }
}
