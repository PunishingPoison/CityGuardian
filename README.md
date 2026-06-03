# CityGuardian

<img width="1919" height="1020" alt="image" src="https://github.com/user-attachments/assets/6d8ed514-eaac-4d68-bde3-02544e388f1d" />


CityGuardian is a complex, real-time Java application designed to simulate natural disasters and the intelligent deployment of emergency services within a procedurally generated city. The project demonstrates advanced algorithms, state machines, and real-time rendering using JavaFX.

## Concepts and Architecture

*   **Procedural City Generation**: Dynamically constructs city grids consisting of roads, residential zones, commercial zones, and hospital infrastructure based on randomized distance-weighted elevation maps.
*   **A* Pathfinding**: Implements the A* (A-Star) search algorithm for intelligent entity navigation. Citizens use it to find the nearest safe zones, and emergency resources (ambulances, firetrucks) use it to navigate the dynamic road network to reach disaster sites and hospitals.
*   **Entity State Machines**: Emergency resources operate on strict state machines (Available, Dispatched, Returning) to manage complex logistics, such as picking up injured citizens, reaching capacity limits, and pathfinding to medical facilities.
*   **Real-time Simulation Loop**: Utilizes an AnimationTimer-based game loop to process physics, movement, disaster spread (e.g., cellular automata for fire), and entity decision-making every frame.
*   **Custom JavaFX Rendering**: Maps, tiles, and entities are rendered directly onto a JavaFX Canvas, ensuring high-performance visualization of large-scale simulations.

## Tech Stack

*   **Language**: Java 17+
*   **UI Framework**: JavaFX
*   **Build Tool**: Maven

## Installation Guide

### Prerequisites
*   Java Development Kit (JDK) 17 or higher installed and configured in your system PATH.
*   Apache Maven installed.
*   (Optional) JavaFX SDK configured if not relying strictly on Maven dependencies.

### Build Instructions

1.  Clone the repository to your local machine:
    ```bash
    git clone https://github.com/PunishingPoison/CityGuardian.git
    cd CityGuardian
    ```

2.  Compile and build the project using Maven:
    ```bash
    mvn clean compile
    ```

3.  Run the application:
    ```bash
    mvn javafx:run
    ```

## Walkthrough Guide

1.  **Generate the City**: Upon launching the dashboard, click the "Generate City" button. This will procedurally generate a unique city layout complete with residential districts, commercial zones, roads, and hospitals.
2.  **Start Simulation**: Click the "Start Simulation" button to begin the real-time simulation loop. Citizens will begin to roam and ambient city logic will activate.
3.  **Trigger Disasters**: Use the control panel on the left to spawn disasters:
    *   **Fire**: Spawns a localized fire that dynamically spreads across adjacent tiles. Firetrucks will automatically dispatch to secure the perimeter and extinguish the flames.
    *   **Flood**: Triggers rising water levels based on terrain elevation. Helicopters will be deployed to rescue stranded citizens from submerged tiles.
    *   **Earthquake**: Applies instant structural damage and injures citizens within a massive radius. Ambulances will dispatch, load up to 10 injured citizens at a time, and transport them safely to the nearest hospital.
4.  **Monitor Analytics**: Observe the live charts on the right side of the dashboard, which track the status of the population (Safe, Injured, Casualties, Evacuated) in real-time as emergency services perform their duties.
