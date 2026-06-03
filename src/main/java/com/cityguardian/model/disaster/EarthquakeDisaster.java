package com.cityguardian.model.disaster;

import com.cityguardian.model.City;
import com.cityguardian.model.Tile;

public class EarthquakeDisaster extends Disaster {

    public EarthquakeDisaster(int startX, int startY, Severity severity) {
        super(startX, startY, severity);
        this.damageRadius = 10;
    }

    @Override
    public void evolve(City city, double deltaTime) {
        if (!active) return;
        
        // Earthquakes apply instant damage in a radius, blocking roads and destroying buildings
        for (int x = startX - damageRadius; x <= startX + damageRadius; x++) {
            for (int y = startY - damageRadius; y <= startY + damageRadius; y++) {
                Tile t = city.getTile(x, y);
                if (t != null) {
                    double dist = Math.sqrt(Math.pow(x - startX, 2) + Math.pow(y - startY, 2));
                    if (dist <= damageRadius) {
                        t.setRiskLevel(0.8);
                        
                        // Destroy some buildings
                        if (t.getType() != com.cityguardian.model.TileType.EMPTY && Math.random() < 0.2) {
                            t.setType(com.cityguardian.model.TileType.OBSTACLE);
                        }
                    }
                }
            }
        }
        
        // Instantly injure citizens in the earthquake zone
        for (com.cityguardian.model.Citizen c : city.getCitizens()) {
            if (!c.isDead() && !c.isEvacuated()) {
                double dist = Math.sqrt(Math.pow(c.getX() - startX, 2) + Math.pow(c.getY() - startY, 2));
                if (dist <= damageRadius) {
                    c.takeDamage(60.0); // Instantly drops 100 health to 40, triggering isInjured=true
                }
            }
        }
        
        active = false; // Only trigger once
    }
}
