package graphsAndAlgorithms;

import java.util.HashSet;
import java.util.Set;

public class DepthFirstSearch<T extends Distancable<T>> extends SearchAlgorithm<T> {
    public DepthFirstSearch(WAdjacencyGraph<T> graph) {
        super(graph);
    }

    @Override
    public Path<WVertex<T>> findPath(T start, T end) {
        if (!this.graph.contains(start) || !this.graph.contains(end)) {
            return null;
        }
        this.running = true;
        
        WVertex<T> startVertex = graph.vertices.get(start);
        WVertex<T> endVertex = graph.vertices.get(end);

        Set<WVertex<T>> visited = new HashSet<>();
        visited.add(startVertex);

        Path<WVertex<T>> path = this.visitDFPath(startVertex, endVertex, visited);
        this.running = false;
        return path;
    }

    private Path<WVertex<T>> visitDFPath(WVertex<T> vertex, WVertex<T> end, Set<WVertex<T>> visited) {
        if (!this.running) return null;

        GridVertex gv = (GridVertex) vertex;
        gv.setVisited();
        this.notifyObservers(gv.getValue());
        if(vertex == end) {
            Path<WVertex<T>> path = new Path<>();
            path.addLast(end, 1);
            GridVertex gvVertex = (GridVertex) end;
            gvVertex.setPath();
            this.notifyObservers(gvVertex.getValue());
            try { Thread.sleep(1); } catch (InterruptedException e) {}
            return path;
        } else {
            for(WVertex<T> n : vertex.getNeighbors().keySet()) {
                if (!visited.contains(n)) {
                    GridVertex gvNeighbor = (GridVertex) n;
                    gvNeighbor.setQueued();
                    this.notifyObservers(gvNeighbor.getValue());
                }
             }
            for(WVertex<T> neighbor : vertex.getNeighbors().keySet()) {
                if(!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    Path<WVertex<T>> path = this.visitDFPath(neighbor, end, visited);
                    if(path != null) {
                        path.addFirst(vertex, 1);
                        GridVertex gvVertex = (GridVertex) vertex;
                        gvVertex.setPath();
                        this.notifyObservers(gvVertex.getValue());
                        try { Thread.sleep(10); } catch (InterruptedException e) {}
                        return path;
                    }
                }
                try { Thread.sleep(this.delay); } catch (InterruptedException e) {}
            }
            return null;
        }
    }
}
