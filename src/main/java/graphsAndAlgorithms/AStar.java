package graphsAndAlgorithms;

public class AStar<T extends Distancable<T>> extends Dijkstras<T> {
    public AStar(WAdjacencyGraph<T> graph) {
        super(graph);
    }

    @Override
    public double heuristic(T start, T end) {
        return start.distance(end);
    }
}
