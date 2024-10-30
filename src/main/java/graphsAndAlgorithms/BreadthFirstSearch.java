package graphsAndAlgorithms;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class BreadthFirstSearch<T extends Distancable<T>> extends SearchAlgorithm<T> {  
    public BreadthFirstSearch(WAdjacencyGraph<T> graph) {
        super(graph);
    }

    @Override
    public Path<WVertex<T>> findPath(T start, T end) {
        if (!this.graph.contains(start) || !this.graph.contains(end)) {
            return null;
        }
        this.running = true;
        WVertex<T> startVertex = this.graph.vertices.get(start);
        WVertex<T> endVertex = this.graph.vertices.get(end);

        Queue<WVertex<T>> queue = new LinkedList<>();
        Map<WVertex<T>, WVertex<T>> predecessors = new HashMap<>();

        queue.add(startVertex);
        predecessors.put(startVertex, null);

        while(!queue.isEmpty() && this.running) {
            WVertex<T> currentVertex = queue.poll();
            GridVertex gv = (GridVertex) currentVertex;
            gv.setVisited();
            this.notifyObservers(gv.getValue());
            if(currentVertex == endVertex) {
                break;
            } else {
                for(WVertex<T> neighbor : currentVertex.getNeighbors().keySet()) {
                    if(!predecessors.containsKey(neighbor)) {
                        GridVertex gvNeighbor = (GridVertex) neighbor;
                        gvNeighbor.setQueued();
                        this.notifyObservers(gvNeighbor.getValue());
                        predecessors.put(neighbor, currentVertex);
                        queue.add(neighbor);
                    }
                }
            }
            try { Thread.sleep(this.delay); } catch (InterruptedException e) {}
        }
        if (!this.running) {
            return null;
        }
        Path<WVertex<T>> path = new Path<>();
        WVertex<T> currentVertex = endVertex;
        while(currentVertex != null) {
            path.addFirst(currentVertex, 1);
            GridVertex gv = (GridVertex) currentVertex;
            gv.setPath();
            this.notifyObservers(gv.getValue());
            currentVertex = predecessors.get(currentVertex);
            try { Thread.sleep(10); } catch (InterruptedException e) {}
        }
        this.running = false;
        return path;
    }
}
