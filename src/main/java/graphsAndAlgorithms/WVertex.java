package graphsAndAlgorithms;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class WVertex<E> implements Comparator<WVertex<E>>{
    private final E value;
    private final Map<WVertex<E>, Double> neighbors;

    public WVertex(E value) {
        this.value = value;
        this.neighbors = new HashMap<>();
    }

    public E getValue() {
        return this.value;
    }

    public void connect(WVertex<E> neighbor, double weight) {
        this.neighbors.put(neighbor, weight);
    }

    public boolean connected(WVertex<E> vertex) {
        return this.neighbors.containsKey(vertex);
    }

    public double weight(WVertex<E> neighbor) {
        return this.neighbors.get(neighbor);
    }

    public Map<WVertex<E>, Double> getNeighbors() {
        return this.neighbors;
    }

    @Override
    public String toString(){
        return this.value.toString();
    }

    @Override
    public int compare(WVertex<E> a, WVertex<E> b) {
        double weightA = this.neighbors.get(a);
        double weightB = this.neighbors.get(b);
        return weightA < weightB ? -1 : 1;
    }     
}
