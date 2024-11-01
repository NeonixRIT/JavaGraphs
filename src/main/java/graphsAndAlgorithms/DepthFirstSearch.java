package graphsAndAlgorithms;

import java.util.HashSet;
import java.util.Set;

public class DepthFirstSearch<T extends Distancable<T>> extends SearchAlgorithm<T> {
    public DepthFirstSearch(WAdjacencyGraph<T> graph) { super(graph); }

    @Override
    public Path<WVertex<T>> findPath(T start, T end) {
        if (!this.graph.contains(start) || !this.graph.contains(end)) {
            return null;
        }
        
        WVertex<T> startVertex = graph.vertices.get(start);
        WVertex<T> endVertex = graph.vertices.get(end);

        Set<WVertex<T>> visited = new HashSet<>();
        visited.add(startVertex);

        return this.visitDFPath(startVertex, endVertex, visited);
    }

    private Path<WVertex<T>> visitDFPath(WVertex<T> vertex, WVertex<T> end, Set<WVertex<T>> visited) {
        if(vertex == end) {
            Path<WVertex<T>> path = new Path<>();
            path.addLast(end, 1);
            return path;
        } else {
            for(WVertex<T> neighbor : vertex.getNeighbors().keySet()) {
                if(!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    Path<WVertex<T>> path = this.visitDFPath(neighbor, end, visited);
                    if(path != null) {
                        path.addFirst(vertex, 1);
                        return path;
                    }
                }
            }
            return null;
        }
    }
}
