package com.cityguardian.model;

import java.util.ArrayList;
import java.util.List;

public class City {
    private final int width;
    private final int height;
    private final Tile[][] grid;
    private final List<Citizen> citizens;
    private final List<com.cityguardian.model.disaster.Disaster> disasters;
    private final List<com.cityguardian.model.resource.EmergencyResource> resources;
    
    public City(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Tile[width][height];
        this.citizens = new ArrayList<>();
        this.disasters = new ArrayList<>();
        this.resources = new ArrayList<>();
        initializeGrid();
    }

    private void initializeGrid() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = new Tile(x, y, TileType.EMPTY);
            }
        }
    }

    public Tile getTile(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return grid[x][y];
        }
        return null;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public List<Citizen> getCitizens() { return citizens; }
    
    public void addCitizen(Citizen c) {
        citizens.add(c);
    }
    
    public List<com.cityguardian.model.disaster.Disaster> getDisasters() {
        return disasters;
    }
    
    public void addDisaster(com.cityguardian.model.disaster.Disaster d) {
        disasters.add(d);
    }
    
    public List<com.cityguardian.model.resource.EmergencyResource> getResources() {
        return resources;
    }
    
    public void addResource(com.cityguardian.model.resource.EmergencyResource r) {
        resources.add(r);
    }
}
