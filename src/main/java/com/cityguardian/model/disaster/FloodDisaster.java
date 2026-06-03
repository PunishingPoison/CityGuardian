package com.cityguardian.model.disaster;

import com.cityguardian.model.City;
import com.cityguardian.model.Tile;
import java.util.ArrayList;
import java.util.List;

public class FloodDisaster extends Disaster {
    private double timeAccumulator = 0;
    private final double spreadInterval = 3.0; // Seconds before spreading

    public FloodDisaster(int startX, int startY, Severity severity) {
        super(startX, startY, severity);
        this.spreadSpeed = 0.5;
        this.damageRadius = 1;
    }

    @Override
    public void evolve(City city, double deltaTime) {
        if (!active) return;
        
        timeAccumulator += deltaTime;
        if (timeAccumulator >= spreadInterval) {
            timeAccumulator = 0;
            spread(city);
        }
    }
    
    private void spread(City city) {
        List<Tile> newlyFlooded = new ArrayList<>();
        
        for (int x = 0; x < city.getWidth(); x++) {
            for (int y = 0; y < city.getHeight(); y++) {
                Tile t = city.getTile(x, y);
                if (t != null && t.hasDisaster() && t.getType() == com.cityguardian.model.TileType.WATER) {
                    
                    int[] dx = {-1, 1, 0, 0};
                    int[] dy = {0, 0, -1, 1};
                    
                    Tile lowestNeighbor = null;
                    double lowestElev = Double.MAX_VALUE;
                    
                    for (int i = 0; i < 4; i++) {
                        Tile neighbor = city.getTile(x + dx[i], y + dy[i]);
                        if (neighbor != null && neighbor.getType() != com.cityguardian.model.TileType.WATER) {
                            if (neighbor.getElevation() < lowestElev) {
                                lowestElev = neighbor.getElevation();
                                lowestNeighbor = neighbor;
                            }
                        }
                    }
                    
                    if (lowestNeighbor != null && Math.random() < 0.8) {
                        newlyFlooded.add(lowestNeighbor);
                    }
                }
            }
        }
        
        for (Tile t : newlyFlooded) {
            t.setHasDisaster(true);
            t.setRiskLevel(0.9);
            t.setType(com.cityguardian.model.TileType.WATER); 
        }
    }
}
