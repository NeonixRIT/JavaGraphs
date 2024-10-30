package unit08;
import java.util.HashMap;
import java.util.Map;

public class WAdjacencyGraph<E> implements WGraph<E> {
    protected final Map<E, WVertex<E>> vertices;

    public WAdjacencyGraph() {
        this.vertices = new HashMap<>();
    }

    @Override
    public void add(E value) {
        WVertex<E> vertex = new WVertex<>(value);
        vertices.put(value, vertex);
    }

    @Override
    public boolean contains(E value) {
        return vertices.containsKey(value);
    }

    @Override
    public int size() {
        return vertices.size();
    }

    @Override
    public void connect_undirected(E a, E b, double weight) {
        WVertex<E> vertexA = vertices.get(a);
        WVertex<E> vertexB = vertices.get(b);

        vertexA.connect(vertexB, weight);
        vertexB.connect(vertexA, weight);
    }

    @Override
    public void connect_directed(E a, E b, double weight) {
        WVertex<E> vertexA = vertices.get(a);
        WVertex<E> vertexB = vertices.get(b);

        vertexA.connect(vertexB, weight);
    }

    @Override
    public boolean connected(E a, E b) {
        WVertex<E> vertexA = vertices.get(a);
        WVertex<E> vertexB = vertices.get(b);

        return vertexA.connected(vertexB);
    }

    @Override
    public double weight(E a, E b) {
        WVertex<E> vertexA = vertices.get(a);
        WVertex<E> vertexB = vertices.get(b);

        return vertexA.weight(vertexB);
    }
}
