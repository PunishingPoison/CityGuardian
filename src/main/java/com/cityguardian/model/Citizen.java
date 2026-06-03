package com.cityguardian.model;

import java.util.List;
import com.cityguardian.model.Tile;

public class Citizen {
    private String name;
    private int age;
    private double health = 100.0;
    private double x;
    private double y;
    
    // States
    private boolean isInjured = false;
    private boolean isEvacuated = false;
    private boolean isDead = false;
    
    // Pathfinding
    private List<Tile> evacuationPath;
    private int currentPathIndex = 0;
    private double movementSpeed = 2.0; // Tiles per second

    public Citizen(String name, int age, double startX, double startY) {
        this.name = name;
        this.age = age;
        this.x = startX;
        this.y = startY;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    
    public double getHealth() { return health; }
    public void takeDamage(double amount) {
        if (isDead || isEvacuated) return;
        this.health -= amount;
        if (this.health <= 0) {
            this.health = 0;
            this.isDead = true;
        } else if (this.health < 50) {
            this.isInjured = true;
        }
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public boolean isInjured() { return isInjured; }
    public boolean isDead() { return isDead; }

    public boolean isEvacuated() { return isEvacuated; }
    public void setEvacuated(boolean evacuated) { this.isEvacuated = evacuated; }
    
    public List<Tile> getEvacuationPath() { return evacuationPath; }
    public void setEvacuationPath(List<Tile> path) { 
        this.evacuationPath = path; 
        this.currentPathIndex = 0;
    }
    
    public int getCurrentPathIndex() { return currentPathIndex; }
    public void advancePath() { currentPathIndex++; }
    public double getMovementSpeed() { return movementSpeed; }
}
