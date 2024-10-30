package graphsAndAlgorithms;

import java.util.HashSet;
import java.util.Set;

public abstract class SearchAlgorithm<T extends Distancable<T>> {
    protected WAdjacencyGraph<T> graph;
    protected Set<SearchAlgorithmObserver> observers;
    protected long delay;
    protected boolean running;

    public SearchAlgorithm(WAdjacencyGraph<T> graph) {
        this.graph = graph;
        this.observers = new HashSet<>();
        this.delay = 0l;
        this.running = false;
    }

    @SuppressWarnings("unused")
    abstract Path<WVertex<T>> findPath(T start, T end);
    public void notifyObservers(Location location) {
        for (SearchAlgorithmObserver observer : this.observers) {
            observer.squareUpdated(location);
        }
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public void registerObserver(SearchAlgorithmObserver observer) {
        this.observers.add(observer);
    }

    public void stop() {
        this.running = false;
    }
}
