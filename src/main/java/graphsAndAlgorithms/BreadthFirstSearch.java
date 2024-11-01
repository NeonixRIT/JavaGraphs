package graphsAndAlgorithms;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class BreadthFirstSearch<T extends Distancable<T>> extends SearchAlgorithm<T> {  
    public BreadthFirstSearch(WAdjacencyGraph<T> graph) { super(graph); }

    @Override
    public Path<WVertex<T>> findPath(T start, T end) {
        if (!this.graph.contains(start) || !this.graph.contains(end)) {
            return null;
        }
        WVertex<T> startVertex = this.graph.vertices.get(start);
        WVertex<T> endVertex = this.graph.vertices.get(end);

        Queue<WVertex<T>> queue = new LinkedList<>();
        Map<WVertex<T>, WVertex<T>> predecessors = new HashMap<>();

        queue.add(startVertex);
        predecessors.put(startVertex, null);

        while(!queue.isEmpty()) {
            WVertex<T> currentVertex = queue.poll();
            if(currentVertex == endVertex) break;

            for(WVertex<T> neighbor : currentVertex.getNeighbors().keySet()) {
                if(!predecessors.containsKey(neighbor)) {
                    predecessors.put(neighbor, currentVertex);
                    queue.add(neighbor);
                }
            }
        }

        if (!predecessors.containsKey(endVertex)) return null;

        Path<WVertex<T>> path = new Path<>();
        WVertex<T> currentVertex = endVertex;
        while(currentVertex != null) {
            path.addFirst(currentVertex, 1);
            currentVertex = predecessors.get(currentVertex);
        }
        return path;
    }
}
