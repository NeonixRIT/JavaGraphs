package graphsAndAlgorithms;

import java.util.ArrayList;
import java.util.List;

public class Path<T> {
    public final List<T> path;
    public double distance;

    public Path() {
        this.path = new ArrayList<>();
        this.distance = 0;
    }

    public Path(List<T> path, double distance) {
        this.path = path;
        this.distance = distance;
    }

    public void addLast(T vertex, double distance) {
        this.path.add(vertex);
        this.distance += distance;
    }

    public void addFirst(T vertex, double distance) {
        this.path.add(0, vertex);
        this.distance += distance;
    }
}
