package com.cityguardian.model.resource;

import com.cityguardian.model.Tile;

public abstract class EmergencyResource {
    public enum Status { AVAILABLE, DISPATCHED, BUSY, RETURNING }

    private String id;
    private int x;
    private int y;
    private Status status;
    private Tile targetTile;

    public EmergencyResource(String id, int startX, int startY) {
        this.id = id;
        this.x = startX;
        this.y = startY;
        this.status = Status.AVAILABLE;
    }

    public String getId() { return id; }
    public int getX() { return x; }
    public int getY() { return y; }
    public Status getStatus() { return status; }
    
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setStatus(Status status) { this.status = status; }
    public Tile getTargetTile() { return targetTile; }
    
    public void dispatch(Tile target) {
        this.targetTile = target;
        this.status = Status.DISPATCHED;
    }

    public abstract void performAction();
}
