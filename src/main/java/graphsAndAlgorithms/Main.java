package graphsAndAlgorithms;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Main {
    public static long timeSearchAlgorithm(SearchAlgorithm<Location> searchAlgorithm, Location start, Location end) {
        long startTime = System.nanoTime();
        Path<WVertex<Location>> path = searchAlgorithm.findPath(start, end);
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    public static double round(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }

    public static String leftJustifyString(String string, int length) {
        return String.format("%-" + length + "s", string);
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        int size = 10000;
        int runs = 1000;

        System.out.println("Creating Grid Graph with " + size + " vertices...");
        long t1 = System.nanoTime();
        GridGraph graph = GridGraph.makeRandomGridGraph(size);
        long t2 = System.nanoTime();
        System.out.println("Created in " + round((t2 - t1) / 1000000000.0, 4) + "s\n");

        System.out.println("Finding " + runs + " random start and end pairs...");
        List<Location> vertices = new ArrayList<>(graph.vertices.keySet());
        Location[][] randomPoints = new Location[size][2];
        for (int i = 0; i < runs; i++) {
            for (int j = 0; j < size; j++) {
                randomPoints[j][0] = vertices.get((int)(Math.random() * size));
                randomPoints[j][1] = vertices.get((int)(Math.random() * size));
            }
        }
        System.out.println("Found in " + round((System.nanoTime() - t2) / 1000000000.0, 4) + "s\n");

        BreadthFirstSearch<Location> bfs = new BreadthFirstSearch<>(graph);
        DepthFirstSearch<Location> dfs = new DepthFirstSearch<>(graph);
        Dijkstras<Location> dijkstras = new Dijkstras<>(graph);
        AStar<Location> aStar = new AStar<>(graph);
        SearchAlgorithm<Location>[] searchAlgorithms = new SearchAlgorithm[] { bfs, dfs, dijkstras, aStar };

        for (SearchAlgorithm<Location> searchAlgorithm : searchAlgorithms) {
            System.out.println("Testing " + searchAlgorithm.getClass().getSimpleName() + " " + runs + " times...");
            double totalTime = 0;
            for (Location[] points : randomPoints) {
                Location start = points[0];
                Location end = points[1];
                totalTime += timeSearchAlgorithm(searchAlgorithm, start, end);
            }
            System.out.println("Average time: " + round(totalTime / runs / 1000000000.0, 4) + "s\n");
        }
    }
}
