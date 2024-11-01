package graphsAndAlgorithms;

public abstract class SearchAlgorithm<T extends Distancable<T>> {
    protected WAdjacencyGraph<T> graph;

    public SearchAlgorithm(WAdjacencyGraph<T> graph) { this.graph = graph; }

    @SuppressWarnings("unused")
    abstract Path<WVertex<T>> findPath(T start, T end);
}
