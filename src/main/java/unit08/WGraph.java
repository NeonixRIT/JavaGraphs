package unit08;

public interface WGraph<E> {
    void add(E value);
    boolean contains(E value);
    int size();
    void connect_undirected(E a, E b, double weight);
    void connect_directed(E a, E b, double weight);
    boolean connected(E a, E b);
    double weight(E a, E b);
}
