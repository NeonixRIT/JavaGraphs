package graphsAndAlgorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class Dijkstras<T extends Distancable<T>> extends SearchAlgorithm<T> {
    public class PathItem implements Comparable<PathItem> {
        public WVertex<T> vertex;
        public double distance;

        public PathItem(double distance, WVertex<T> vertex) {
            this.distance = distance;
            this.vertex = vertex;
        }

        @Override
        public int compareTo(PathItem o) {
            return Double.compare(this.distance, o.distance);
        }
    }

    public Dijkstras(WAdjacencyGraph<T> graph) {
        super(graph);
    }

    public double heuristic(T start, T end) {
        return 0;
    }

    private HashMap<WVertex<T>, Double> makeDistanceMap(WVertex<T> start) {
        HashMap<WVertex<T>, Double> distances = new HashMap<>();
        for (WVertex<T> vertex : this.graph.vertices.values()) {
            distances.put(vertex, Double.POSITIVE_INFINITY);
        }
        distances.put(start, 0d);
        return distances;
    }

    private HashMap<WVertex<T>, WVertex<T>> makePredecessorMap() {
        HashMap<WVertex<T>, WVertex<T>> predecessors = new HashMap<>();
        for (WVertex<T> vertex : this.graph.vertices.values()) {
            predecessors.put(vertex, null);
        }
        return predecessors;
    }

    @Override
    public Path<WVertex<T>> findPath(T start, T end) {
        if (!this.graph.contains(start) || !this.graph.contains(end)) {
            return null;
        }

        WVertex<T> startVertex = this.graph.vertices.get(start);
        WVertex<T> endVertex = this.graph.vertices.get(end);

        HashMap<WVertex<T>, Double> distances = this.makeDistanceMap(startVertex);
        HashMap<WVertex<T>, WVertex<T>> predecessors = makePredecessorMap();
        Set<WVertex<T>> visited = new HashSet<>();

        PriorityQueue<PathItem> heap = new PriorityQueue<>();

        distances.put(startVertex, 0d);
        heap.add(new PathItem(0, startVertex));

        while (!heap.isEmpty()){
            PathItem current = heap.poll();
            WVertex<T> currentVertex = current.vertex;

            double currentDistance = current.distance;
            if (visited.contains(currentVertex)) {
                continue;
            }
            visited.add(currentVertex);

            if (currentVertex.equals(endVertex)) {
                break;
            }

            for (WVertex<T> neighbor : currentVertex.getNeighbors().keySet()) {
                if (visited.contains(neighbor)) {
                    continue;
                }
                double newDistance = currentDistance + currentVertex.weight(neighbor) + this.heuristic(neighbor.getValue(), end);
                if (newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, newDistance);
                    predecessors.put(neighbor, currentVertex);
                    heap.add(new PathItem(newDistance, neighbor));
                }
            }
        }

        if (distances.get(endVertex) == Double.POSITIVE_INFINITY) {
            return null;
        }

        List<WVertex<T>> pathList = new ArrayList<>();
        WVertex<T> currentVertex = endVertex;
        double distance = 0;
        while (currentVertex != null) {
            pathList.add(0, currentVertex);
            currentVertex = predecessors.get(currentVertex);
            if (currentVertex != null) {
                distance += currentVertex.getNeighbors().get(pathList.get(0));
            }
        }
        return new Path<>(pathList, distance);
    }
}
