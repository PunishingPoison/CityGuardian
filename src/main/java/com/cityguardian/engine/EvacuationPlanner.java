package com.cityguardian.engine;

import com.cityguardian.model.City;
import com.cityguardian.model.Tile;
import java.util.*;

public class EvacuationPlanner {

    private static class NodeRecord implements Comparable<NodeRecord> {
        Tile tile;
        NodeRecord parent;
        double costSoFar;
        double estimatedTotalCost;

        NodeRecord(Tile tile, NodeRecord parent, double costSoFar, double estimatedTotalCost) {
            this.tile = tile;
            this.parent = parent;
            this.costSoFar = costSoFar;
            this.estimatedTotalCost = estimatedTotalCost;
        }

        @Override
        public int compareTo(NodeRecord o) {
            return Double.compare(this.estimatedTotalCost, o.estimatedTotalCost);
        }
    }

    public static List<Tile> findPathAStar(City city, Tile start, Tile goal, boolean requiresRoad) {
        if (start == null || goal == null) return Collections.emptyList();

        PriorityQueue<NodeRecord> open = new PriorityQueue<>();
        Map<Tile, NodeRecord> openMap = new HashMap<>();
        Set<Tile> closed = new HashSet<>();

        NodeRecord startRecord = new NodeRecord(start, null, 0, heuristic(start, goal));
        open.add(startRecord);
        openMap.put(start, startRecord);

        while (!open.isEmpty()) {
            NodeRecord current = open.poll();
            openMap.remove(current.tile);

            if (current.tile == goal) {
                return reconstructPath(current);
            }

            closed.add(current.tile);

            for (Tile neighbor : getNeighbors(city, current.tile)) {
                if (requiresRoad) {
                    if (neighbor.getType() != com.cityguardian.model.TileType.ROAD && neighbor != goal && neighbor != start) continue;
                } else {
                    if (neighbor.getType() == com.cityguardian.model.TileType.WATER || neighbor.getType() == com.cityguardian.model.TileType.OBSTACLE) continue;
                }
                if (closed.contains(neighbor)) continue;

                // Penalty for tiles with high risk or disaster
                double riskPenalty = neighbor.hasDisaster() ? 1000 : (neighbor.getRiskLevel() * 10);
                double cost = current.costSoFar + 1 + riskPenalty;

                NodeRecord neighborRecord = openMap.get(neighbor);
                if (neighborRecord == null || cost < neighborRecord.costSoFar) {
                    double estimatedTotalCost = cost + heuristic(neighbor, goal);
                    if (neighborRecord == null) {
                        neighborRecord = new NodeRecord(neighbor, current, cost, estimatedTotalCost);
                        open.add(neighborRecord);
                        openMap.put(neighbor, neighborRecord);
                    } else {
                        // update existing
                        open.remove(neighborRecord);
                        neighborRecord.costSoFar = cost;
                        neighborRecord.estimatedTotalCost = estimatedTotalCost;
                        neighborRecord.parent = current;
                        open.add(neighborRecord);
                    }
                }
            }
        }
        return Collections.emptyList();
    }

    private static double heuristic(Tile a, Tile b) {
        // Manhattan distance
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
    }

    private static List<Tile> reconstructPath(NodeRecord record) {
        List<Tile> path = new ArrayList<>();
        while (record != null) {
            path.add(record.tile);
            record = record.parent;
        }
        Collections.reverse(path);
        return path;
    }

    private static List<Tile> getNeighbors(City city, Tile tile) {
        List<Tile> neighbors = new ArrayList<>();
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};
        for (int i = 0; i < 4; i++) {
            Tile t = city.getTile(tile.getX() + dx[i], tile.getY() + dy[i]);
            if (t != null) neighbors.add(t);
        }
        return neighbors;
    }
}
