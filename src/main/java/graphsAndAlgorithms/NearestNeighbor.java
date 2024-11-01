package graphsAndAlgorithms;

import java.util.HashSet;
import java.util.Set;

public class NearestNeighbor<T extends Distancable<T>> extends SearchAlgorithm<T>  {
    public NearestNeighbor(WAdjacencyGraph<T> graph) { super(graph); }

    @Override
    Path<WVertex<T>> findPath(T start, T end) {
        if (!this.graph.contains(start) || !this.graph.contains(end)) {
            return null;
        }

        WVertex<T> startVertex = this.graph.vertices.get(start);
        WVertex<T> endVertex = this.graph.vertices.get(end);

        Path<WVertex<T>> path = new Path<>();
        Set<WVertex<T>> visited = new HashSet<>();
        visited.add(startVertex);

        WVertex<T> currentVertex = startVertex;
        while (!currentVertex.equals(endVertex)) {
            WVertex<T> nextVertex = null;
            double minDistance = Double.POSITIVE_INFINITY;
            for (WVertex<T> neighbor : currentVertex.getNeighbors().keySet()) {
                if (!visited.contains(neighbor)) {
                    double distance = currentVertex.getNeighbors().get(neighbor);
                    if (distance < minDistance) {
                        minDistance = distance;
                        nextVertex = neighbor;
                    }
                }
            }
            if (nextVertex == null) {
                break;
            }
            visited.add(nextVertex);
            path.addLast(nextVertex, minDistance);
            currentVertex = nextVertex;
        }
        return path;
    }
    
}
