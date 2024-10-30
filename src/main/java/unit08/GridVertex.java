package unit08;

public class GridVertex extends WVertex<Location> {
    public enum VertexStatus {
        DEFAULT, QUEUED, VISITED, PATH
    }

    private VertexStatus status;

    public GridVertex(Location value) {
        super(value);
        this.status = VertexStatus.DEFAULT;
    }

    public double distance(GridVertex other) {
        return this.getValue().distance(other.getValue());
    }

    public VertexStatus getStatus() {
        return this.status;
    }

    public void setDefault() {
        this.status = VertexStatus.DEFAULT;
    }

    public void setVisited() {
        this.status = VertexStatus.VISITED;
    }

    public void setQueued() {
        this.status = VertexStatus.QUEUED;
    }

    public void setPath() {
        this.status = VertexStatus.PATH;
    }
    
    @Override
    public String toString() {
        return this.getValue().toString();
    }

    @Override
    public int hashCode() {
        return this.getValue().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GridVertex) {
            GridVertex other = (GridVertex) obj;
            return this.getValue().equals(other.getValue());
        }
        return false;
    }

}
