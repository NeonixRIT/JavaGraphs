package graphsAndAlgorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GridGraph extends WAdjacencyGraph<Location>{
    private final int rows;
    private final int cols;

    public GridGraph(int rows, int cols) {
        super();
        this.rows = rows;
        this.cols = cols;
    }

    @Override
    public void add(Location value) {
        if (this.contains(value)) return;
        this.vertices.put(value, new GridVertex(value));
    }

    public void add(GridVertex vertex) {
        if (this.contains(vertex.getValue())) return;
        this.vertices.put(vertex.getValue(), vertex);
    }

    public int getRows() {
        return this.rows;
    }

    public int getCols() {
        return this.cols;
    }

    public static GridGraph makeFullGridGraph(int rows, int cols) {
        GridGraph graph = new GridGraph(rows, cols);
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                Location currentLocation = new Location(x, y);
                graph.add(currentLocation);
                if (x > 0) graph.connect_undirected(currentLocation, new Location(x - 1, y), 1);
                if (y > 0) graph.connect_undirected(currentLocation, new Location(x, y - 1), 1);
            }
        }
        return graph;
    }

    /*
     * Create size random points within a size x size grid
     * Connect each point to closest 3 neighbors with a weight that equals the distance between the points
     */
    public static GridGraph makeRandomGridGraph(int size) {
        GridGraph graph = new GridGraph(size, size);
        Set<Location> points = new HashSet<>();
        while (points.size() < size) {
            int x = (int)(Math.random() * size);
            int y = (int)(Math.random() * size);
            Location point = new Location(x, y);
            if (points.contains(point)) continue;
            points.add(point);
        }

        for (Location point : points) {
            graph.add(point);
        }

        for (Location point : points) {
            List<Path<Location>> distances = new ArrayList<>();
            for (Location other : points) {
                if (point.equals(other)) continue;
                Path<Location> path = new Path<>();
                path.addLast(other, point.distance(other));
                distances.add(path);
            }
            distances.sort((a, b) -> Double.compare(a.distance, b.distance));
            for (int i = 0; i < 3; i++) {
                graph.connect_undirected(point, distances.get(i).path.get(0), distances.get(i).distance);
            }
        }
        return graph;
    }
}
