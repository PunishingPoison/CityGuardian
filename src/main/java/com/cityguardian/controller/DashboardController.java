package com.cityguardian.controller;

import com.cityguardian.model.City;
import com.cityguardian.model.Tile;
import com.cityguardian.model.TileType;
import com.cityguardian.engine.SimulationEngine;
import com.cityguardian.model.Citizen;
import com.cityguardian.model.disaster.Disaster.Severity;
import com.cityguardian.model.disaster.EarthquakeDisaster;
import com.cityguardian.model.disaster.FireDisaster;
import com.cityguardian.model.disaster.FloodDisaster;
import com.cityguardian.model.resource.EmergencyResource;
import com.cityguardian.model.resource.FireTruck;
import com.cityguardian.model.resource.Helicopter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.chart.PieChart;
import javafx.scene.paint.Color;

public class DashboardController {

    @FXML private Canvas mapCanvas;
    @FXML private ComboBox<String> speedCombo;
    @FXML private ListView<String> insightsList;
    @FXML private Label totalCitizensLabel;
    @FXML private Label savedLabel;
    @FXML private Label casualtiesLabel;
    @FXML private PieChart statusChart;
    @FXML private TextField truckInput;
    @FXML private TextField heliInput;
    @FXML private TextField ambulanceInput;
    private ObservableList<PieChart.Data> chartData;

    private City city;
    private SimulationEngine engine;
    
    private final int TILE_SIZE = 10;
    
    @FXML
    public void initialize() {
        speedCombo.getItems().addAll("1x", "2x", "5x", "10x");
        speedCombo.setValue("1x");
        speedCombo.setOnAction(e -> {
            String val = speedCombo.getValue();
            double s = Double.parseDouble(val.replace("x", ""));
            if (engine != null) engine.setSpeed(s);
        });
        
        city = new City(70, 60);
        engine = new SimulationEngine(city, this::onTick);
        
        chartData = FXCollections.observableArrayList(
            new PieChart.Data("Safe", 0),
            new PieChart.Data("Injured", 0),
            new PieChart.Data("Casualties", 0),
            new PieChart.Data("Evacuated", 0)
        );
        statusChart.setData(chartData);
        
        drawMap();
        insightsList.getItems().add("System Initialized. Awaiting simulation start.");
    }
    
    private void onTick() {
        Platform.runLater(() -> {
            drawMap();
            updateStats();
            
            // Sync vehicle limits from UI
            try {
                if (engine != null) {
                    engine.setMaxFiretrucks(Integer.parseInt(truckInput.getText()));
                    engine.setMaxHelicopters(Integer.parseInt(heliInput.getText()));
                    if (ambulanceInput != null && ambulanceInput.getText() != null) {
                        engine.setMaxAmbulances(Integer.parseInt(ambulanceInput.getText()));
                    }
                }
            } catch(NumberFormatException ex) {}
        });
    }

    private void updateStats() {
        int safe = 0, injured = 0, casualties = 0, evacuated = 0;
        for (Citizen c : city.getCitizens()) {
            if (c.isDead()) casualties++;
            else if (c.isEvacuated()) evacuated++;
            else if (c.isInjured()) injured++;
            else safe++;
        }
        
        totalCitizensLabel.setText("Total Citizens: " + city.getCitizens().size());
        savedLabel.setText("Saved: " + evacuated);
        casualtiesLabel.setText("Casualties: " + casualties);
        
        chartData.get(0).setPieValue(safe);
        chartData.get(1).setPieValue(injured);
        chartData.get(2).setPieValue(casualties);
        chartData.get(3).setPieValue(evacuated);
    }

    @FXML
    public void generateProceduralCity() {
        clearCity();
        city.getCitizens().clear();
        city.getResources().clear();
        city.getDisasters().clear();
        
        double cx = city.getWidth() / 2.0;
        double cy = city.getHeight() / 2.0;
        double maxDist = Math.sqrt(cx*cx + cy*cy);

        for (int x = 0; x < city.getWidth(); x++) {
            for (int y = 0; y < city.getHeight(); y++) {
                Tile t = city.getTile(x, y);
                
                double dist = Math.sqrt(Math.pow(x - cx, 2) + Math.pow(y - cy, 2));
                double baseElev = 1.0 - (dist / maxDist);
                t.setElevation(baseElev + (Math.random() * 0.2 - 0.1));

                boolean isRoad = (x % 6 == 0) || (y % 6 == 0);

                if (isRoad) {
                    t.setType(TileType.ROAD);
                } else {
                    if (Math.random() < 0.3) {
                        t.setType(TileType.RESIDENTIAL);
                        for (int i=0; i<3; i++) {
                            city.addCitizen(new Citizen("Cit_" + x + "_" + y + "_" + i, 30, x, y));
                        }
                    } else if (Math.random() < 0.1) {
                        t.setType(TileType.COMMERCIAL);
                    } else if (Math.random() < 0.02) {
                        t.setType(TileType.HOSPITAL);
                    } else {
                        t.setType(TileType.EMPTY);
                    }
                }
            }
        }
        drawMap();
        updateStats();
        insightsList.getItems().add("City procedurally generated.");
    }
    
    @FXML
    public void clearCity() {
        for (int x = 0; x < city.getWidth(); x++) {
            for (int y = 0; y < city.getHeight(); y++) {
                city.getTile(x, y).setType(TileType.EMPTY);
                city.getTile(x, y).setHasDisaster(false);
            }
        }
        city.getResources().clear();
        city.getDisasters().clear();
        drawMap();
        insightsList.getItems().add("City cleared.");
    }

    @FXML
    public void startSimulation() {
        engine.start();
        insightsList.getItems().add("Simulation started.");
    }

    @FXML
    public void pauseSimulation() {
        engine.pause();
        insightsList.getItems().add("Simulation paused.");
    }

    @FXML
    public void resetSimulation() {
        engine.pause();
        clearCity();
        insightsList.getItems().add("Simulation reset.");
    }
    
    @FXML
    public void triggerEarthquake() {
        insightsList.getItems().add("WARNING: Earthquake triggered!");
        city.addDisaster(new EarthquakeDisaster(35, 30, Severity.CRITICAL));
        if (!city.getDisasters().isEmpty()) engine.start();
    }
    
    @FXML
    public void triggerFire() {
        insightsList.getItems().add("WARNING: Major multi-block fire outbreak detected!");
        int startX = 35;
        int startY = 30;
        int initialRadius = 6; // Massive 13x13 initial fire covering ~4 city blocks
        
        for (int x = startX - initialRadius; x <= startX + initialRadius; x++) {
            for (int y = startY - initialRadius; y <= startY + initialRadius; y++) {
                Tile t = city.getTile(x, y);
                // Prevent roads from burning initially so firetrucks can navigate
                if (t != null && t.getType() != com.cityguardian.model.TileType.WATER && t.getType() != com.cityguardian.model.TileType.ROAD) {
                    t.setHasDisaster(true);
                    t.setRiskLevel(1.0);
                }
            }
        }
        
        city.addDisaster(new FireDisaster(startX, startY, Severity.HIGH));
        if (!city.getDisasters().isEmpty()) engine.start();
        drawMap();
    }
    
    @FXML
    public void triggerFlood() {
        insightsList.getItems().add("WARNING: Flood warnings issued. Water level rising rapidly.");
        if (city.getTile(10, 10) != null) {
            city.getTile(10, 10).setType(TileType.WATER);
            city.getTile(10, 10).setHasDisaster(true);
            city.addDisaster(new FloodDisaster(10, 10, Severity.HIGH));
            if (!city.getDisasters().isEmpty()) engine.start();
        }
        drawMap();
    }

    private void drawMap() {
        GraphicsContext gc = mapCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());
        
        for (int x = 0; x < city.getWidth(); x++) {
            for (int y = 0; y < city.getHeight(); y++) {
                Tile tile = city.getTile(x, y);
                
                if (tile.hasDisaster()) {
                    gc.setFill(Color.RED);
                } else {
                    switch (tile.getType()) {
                        case RESIDENTIAL: gc.setFill(Color.LIGHTBLUE); break;
                        case COMMERCIAL: gc.setFill(Color.ORANGE); break;
                        case ROAD: gc.setFill(Color.DARKGRAY); break;
                        case HOSPITAL: gc.setFill(Color.WHITE); break;
                        case SHELTER: gc.setFill(Color.LIGHTGREEN); break;
                        case WATER: gc.setFill(Color.DARKBLUE); break;
                        case BURNT: gc.setFill(Color.rgb(50, 50, 50)); break;
                        case OBSTACLE: gc.setFill(Color.rgb(139, 69, 19)); break; // SaddleBrown
                        default: 
                            double elev = Math.max(0, Math.min(1.0, tile.getElevation()));
                            int cVal = (int) (elev * 50) + 20;
                            gc.setFill(Color.rgb(cVal, cVal, cVal));
                            break;
                    }
                }
                
                gc.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE - 1, TILE_SIZE - 1);
            }
        }
        
        // Draw citizens
        for (Citizen c : city.getCitizens()) {
            if (c.isDead() || c.isEvacuated()) continue;
            
            if (c.isInjured()) {
                gc.setFill(Color.YELLOW);
            } else {
                gc.setFill(Color.rgb(100, 255, 100)); // Greenish
            }
            double px = c.getX() * TILE_SIZE + TILE_SIZE / 2.0;
            double py = c.getY() * TILE_SIZE + TILE_SIZE / 2.0;
            gc.fillOval(px - 2, py - 2, 4, 4);
        }
        
        // Draw resources
        for (EmergencyResource r : city.getResources()) {
            double px = r.getX() * TILE_SIZE;
            double py = r.getY() * TILE_SIZE;
            
            if (r instanceof Helicopter) {
                Helicopter h = (Helicopter)r;
                px = h.getCurrentX() * TILE_SIZE;
                py = h.getCurrentY() * TILE_SIZE;
                gc.setFill(Color.CYAN);
                gc.fillRect(px, py, TILE_SIZE, TILE_SIZE);
            } else if (r instanceof FireTruck) {
                FireTruck ft = (FireTruck)r;
                px = ft.getCurrentX() * TILE_SIZE;
                py = ft.getCurrentY() * TILE_SIZE;
                gc.setFill(Color.MAGENTA);
                gc.fillRect(px, py, TILE_SIZE, TILE_SIZE);
            } else if (r instanceof com.cityguardian.model.resource.Ambulance) {
                com.cityguardian.model.resource.Ambulance a = (com.cityguardian.model.resource.Ambulance)r;
                px = a.getCurrentX() * TILE_SIZE;
                py = a.getCurrentY() * TILE_SIZE;
                gc.setFill(Color.WHITE);
                gc.fillRect(px, py, TILE_SIZE, TILE_SIZE);
            }
        }
    }
}
