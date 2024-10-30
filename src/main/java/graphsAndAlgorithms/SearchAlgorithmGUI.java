/*
 * Creates a GUI to demonstrate the search algorithms.
 * 
 * The GUI will display a grid of cells, each cell representing a vertex in the graph.
 *     The user can input rows and columns for the grid.
 *     Square Colors:
 *         The start square is Green
 *         The end square is Red
 *         The frontier/queued squares are Blue
 *         The visited squares are Yellow
 *         The final path is Purple
 *         The walls are Black
 *         The unvisited squares are White
 * 
 * The GUI will allow the user to move the start and end points on the grid.
 *     They can do this by selecting the start or end square and then clicking on a new square.
 *     Clicking on the start or end and then clicking on the same square will deselect the start or end.
 *     Clicking on the start or end and then clicking on the start or end will swap the start and end.
 * 
 * The GUI will also allow the user to select the search algorithm to use via a drop down.
 *     The options are Depth First Search, Breadth First Search, Dijkstra's, and A*.
 * The GUI will allow users to select cells to be walls, disconnecting their neighbors in the graph.
 *     Clicking on a wall square will remove the wall, reconnecting the neighbors.
 */
package graphsAndAlgorithms;

import java.util.HashSet;
import java.util.Set;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SearchAlgorithmGUI extends Application {
    private static final StackPane GREEN;
    private static final StackPane RED;
    private static final StackPane BLUE;
    private static final StackPane CORNFLOWERBLUE;
    private static final StackPane YELLOW;
    private static final StackPane PURPLE;
    private static final StackPane BLACK;
    private static final StackPane WHITE;

    private static final int DEFAULT_SQUARE_SIZE = 15;
    private static final double BORDER_SIZE = 0.1;
    private static final int SQAURE_GAP = 5;

    private static final int DEFAULT_ROWS = 25;
    private static final int DEFAULT_COLS = 25;

    private static final long DEFAULT_DELAY = 5; // ms

    static {
        GREEN = new StackPane();
        GREEN.setBackground(new Background(
            new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, new Insets(BORDER_SIZE)),
            new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)
        ));
        
        RED = new StackPane();
        RED.setBackground(new Background(
            new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, new Insets(BORDER_SIZE)),
            new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)
        ));


        BLUE = new StackPane();
        BLUE.setBackground(new Background(
            new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, new Insets(BORDER_SIZE)),
            new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)
        ));


        CORNFLOWERBLUE = new StackPane();
        CORNFLOWERBLUE.setBackground(new Background(
            new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, new Insets(BORDER_SIZE)),
            new BackgroundFill(Color.CORNFLOWERBLUE, CornerRadii.EMPTY, Insets.EMPTY)
        ));

        YELLOW = new StackPane();
        YELLOW.setBackground(new Background(
            new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, new Insets(BORDER_SIZE)),
            new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)
        ));
        
        PURPLE = new StackPane();
        PURPLE.setBackground(new Background(
            new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, new Insets(BORDER_SIZE)),
            new BackgroundFill(Color.PURPLE, CornerRadii.EMPTY, Insets.EMPTY)
        ));
        
        WHITE = new StackPane();
        WHITE.setBackground(new Background(
            new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, new Insets(BORDER_SIZE)),
            new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)
        ));

        BLACK = new StackPane();
        BLACK.setBackground(new Background(
            new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, new Insets(BORDER_SIZE)),
            new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)
        ));
    }

    private class StackPaneGrid {
        private StackPane[][] grid;

        public StackPaneGrid(int rows, int cols) {
            this.grid = new StackPane[rows][cols];
        }

        public StackPane get(int row, int col) {
            return this.grid[row][col];
        }

        public StackPane get(Location location) {
            return this.grid[location.getX()][location.getY()];
        }

        public void set(int row, int col, StackPane stackPane) {
            this.grid[row][col] = stackPane;
        }

        public void set(Location location, StackPane stackPane) {
            this.grid[location.getX()][location.getY()] = stackPane;
        }
    }

    private enum GUIStatus {
        NONE, SEARCHING, FINISHED,
    }

    private StackPaneGrid squares;
    private Set<Location> walls;

    private GridGraph graph;
    private int squareSize;

    private long delay;

    private String selectedAlgorithmString;
    private SearchAlgorithm<Location> searchAlgorithm;

    private Location start;
    private Location end;

    private GUIStatus status;
    private boolean movingStart;
    private boolean movingEnd;
    private Thread searchingThread;

    @Override
    public void start(Stage stage) {
        this.status = GUIStatus.NONE;
        this.graph = GridGraph.makeFullGridGraph(DEFAULT_ROWS, DEFAULT_COLS);
        this.searchAlgorithm = new BreadthFirstSearch<>(this.graph);
        this.selectedAlgorithmString = "Breadth First Search";

        this.squareSize = DEFAULT_SQUARE_SIZE;
        this.delay = DEFAULT_DELAY;
        this.searchAlgorithm.setDelay(this.delay);
        this.start = new Location(this.graph.getRows() / 2, 1);
        this.end = new Location(this.graph.getRows() / 2, this.graph.getCols() - 2);
        this.walls = new HashSet<>();

        stage.setTitle("[" + this.status.name() + "] Search Algorithms: " + this.selectedAlgorithmString);

        BorderPane borderPane = new BorderPane();

        VBox optionsAndControls = new VBox();
        optionsAndControls.setPadding(new Insets(5));
        optionsAndControls.setSpacing(5);
        
        GridPane gridOptions = new GridPane();
        gridOptions.setPadding(new Insets(5));
        gridOptions.setHgap(SQAURE_GAP);
        gridOptions.setVgap(SQAURE_GAP);
        optionsAndControls.getChildren().add(gridOptions);

        Label rowsLabel = new Label("Rows:");
        gridOptions.add(rowsLabel, 0, 0);

        TextField rowsInput = new TextField();
        rowsInput.setText(Integer.toString(this.graph.getRows()));
        gridOptions.add(rowsInput, 1, 0);

        rowsInput.setOnAction(eh -> {
            if (this.status == GUIStatus.SEARCHING) return;
            Integer rows = tryParseInt(rowsInput.getText());
            if (rows == null || rows < 3) return;
            this.graph = GridGraph.makeFullGridGraph(rows, this.graph.getCols());
            GridPane newGridMaze = this.createMazeGridPane(stage);
            this.updateSearchAlgorithm(stage);
            this.resetStartAndEndLocations();

            borderPane.setCenter(newGridMaze);
            this.updateWindowDimensions(stage);
        });

        Label colsLabel = new Label("Columns:");
        gridOptions.add(colsLabel, 0, 1);

        TextField colsInput = new TextField();
        colsInput.setText(Integer.toString(this.graph.getCols()));
        gridOptions.add(colsInput, 1, 1);

        colsInput.setOnAction(eh -> {
            if (this.status == GUIStatus.SEARCHING) return;
            Integer cols = tryParseInt(colsInput.getText());
            if (cols == null || cols < 3) return;
            this.graph = GridGraph.makeFullGridGraph(this.graph.getRows(), cols);
            GridPane newGridMaze = this.createMazeGridPane(stage);
            this.updateSearchAlgorithm(stage);
            this.resetStartAndEndLocations();

            borderPane.setCenter(newGridMaze);
            this.updateWindowDimensions(stage);
        });
        
        Label squareSizeLabel = new Label("Square Size:");
        gridOptions.add(squareSizeLabel, 0, 2);

        TextField squareSizeInput = new TextField();
        squareSizeInput.setText(Integer.toString(this.squareSize));
        gridOptions.add(squareSizeInput, 1, 2);

        squareSizeInput.setOnAction(eh -> {
            if (this.status == GUIStatus.SEARCHING) return;
            Integer newSquareSize = tryParseInt(squareSizeInput.getText());
            if (newSquareSize == null || newSquareSize < 1) return;
            this.squareSize = newSquareSize;
            GridPane newGridMaze = this.createMazeGridPane(stage);
            this.resetStartAndEndLocations();

            borderPane.setCenter(newGridMaze);
            this.updateWindowDimensions(stage);
        });

        Label searchAlgorithmLabel = new Label("Search Algorithm:");
        gridOptions.add(searchAlgorithmLabel, 0, 3);

        ComboBox<String> searchAlgorithmSelection = new ComboBox<>();
        searchAlgorithmSelection.getItems().addAll("Breadth First Search", "Depth First Search", "Dijkstra's", "A*");
        searchAlgorithmSelection.setValue("Breadth First Search");
        gridOptions.add(searchAlgorithmSelection, 1, 3);

        searchAlgorithmSelection.setOnAction(eh -> {
            this.selectedAlgorithmString = searchAlgorithmSelection.getValue();
            this.updateSearchAlgorithm(stage);
        });

        Label delayLabel = new Label("Iteration Delay (ms):");
        gridOptions.add(delayLabel, 0, 4);

        TextField delayBox = new TextField();
        delayBox.setText(Long.toString(this.delay));
        gridOptions.add(delayBox, 1, 4);

        delayBox.setOnAction(eh -> {
            Long newDelay = tryParseLong(delayBox.getText());
            if (newDelay == null || newDelay < 0) return;
            this.delay = newDelay;
            this.searchAlgorithm.setDelay(this.delay);
        });

        GridPane gridControls = new GridPane();
        gridControls.setPadding(new Insets(5));
        gridControls.setHgap(5);
        gridControls.setVgap(5);
        optionsAndControls.getChildren().add(gridControls);

        Button startButton = new Button("Start");
        gridControls.add(startButton, 0, 0);

        startButton.setOnAction(eh -> {
            if (this.status == GUIStatus.SEARCHING) return;
            if (this.status == GUIStatus.FINISHED) this.setVerticesToDefault();
            this.status = GUIStatus.SEARCHING;
            stage.setTitle("[" + this.status.name() + "] Search Algorithms: " + this.selectedAlgorithmString);
            this.searchingThread = new Thread(() -> {
                this.searchAlgorithm.findPath(this.start, this.end);
                this.status = GUIStatus.FINISHED;
                Platform.runLater(() -> {
                    stage.setTitle("[" + this.status.name() + "] Search Algorithms: " + this.selectedAlgorithmString);
                });
            });
            this.searchingThread.setDaemon(true);
            this.searchingThread.start();
        });

        Button stopButton = new Button("Stop");
        gridControls.add(stopButton, 1, 0);

        stopButton.setOnAction(eh -> {
            if (this.status == GUIStatus.SEARCHING) {
                this.searchAlgorithm.stop();
                stage.setTitle("[" + this.status.name() + "] Search Algorithms: " + this.selectedAlgorithmString);
                this.searchingThread = null;
            }
        });

        Button clearButton = new Button("Clear");
        gridControls.add(clearButton, 2, 0);

        clearButton.setOnAction(eh -> {
            if (this.status == GUIStatus.SEARCHING) this.searchAlgorithm.stop();
            this.status = GUIStatus.NONE;
            stage.setTitle("[" + this.status.name() + "] Search Algorithms: " + this.selectedAlgorithmString);
            this.setVerticesToDefault();
        });

        Button resetButton = new Button("Reset");
        gridControls.add(resetButton, 3, 0);

        resetButton.setOnAction(eh -> {
            if (this.status == GUIStatus.SEARCHING) this.searchAlgorithm.stop();
            this.graph = GridGraph.makeFullGridGraph(this.graph.getRows(), this.graph.getCols());
            GridPane newGridMaze = this.createMazeGridPane(stage);
            this.updateSearchAlgorithm(stage);
            this.resetStartAndEndLocations();
            borderPane.setCenter(newGridMaze);
            this.updateWindowDimensions(stage);
            this.searchAlgorithm.stop();
            this.searchingThread = null;
            this.status = GUIStatus.NONE;
            stage.setTitle("[" + this.status.name() + "] Search Algorithms: " + this.selectedAlgorithmString);
        });

        borderPane.setLeft(optionsAndControls);

        GridPane gridMaze = createMazeGridPane(stage);
        borderPane.setCenter(gridMaze);
        this.resetStartAndEndLocations();
        this.updateSearchAlgorithm(stage);

        stage.setScene(new Scene(borderPane));
        this.updateWindowDimensions(stage);

        stage.show();
    }

    private GridPane createMazeGridPane(Stage stage) {
        GridPane newGridMaze = new GridPane();
        newGridMaze.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
        newGridMaze.setPadding(new Insets(1));
        newGridMaze.setHgap(1);
        newGridMaze.setVgap(1);

        this.squares = new StackPaneGrid(this.graph.getRows(), this.graph.getCols());
        this.walls.clear();
        for (int row = 0; row < this.graph.getRows(); row++) {
            for (int col = 0; col < this.graph.getCols(); col++) {
                final int rowFinal = row;
                final int colFinal = col;
                StackPane cell = new StackPane();
                cell.setBackground(new Background(
                    new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, new Insets(BORDER_SIZE)),
                    new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)
                ));
                cell.setPrefSize(this.squareSize, this.squareSize);
                cell.setOnMouseClicked(eh -> {
                    if (this.status == GUIStatus.SEARCHING) return;
                    final Location location = new Location(rowFinal, colFinal);
                    if (this.movingStart) {
                        if (location.equals(this.start)) {
                            this.movingStart = false;
                            this.movingEnd = false;
                            stage.setTitle("[" + this.status.name() + "] Search Algorithms: " + this.selectedAlgorithmString);
                            return;
                        } else if (location.equals(this.end)) {
                            Location tmp = this.start;
                            this.start = this.end;
                            this.end = tmp;
                            this.squares.get(this.start).setBackground(GREEN.getBackground());
                            this.squares.get(this.end).setBackground(RED.getBackground());
                        } else {
                            this.squares.get(this.start).setBackground(WHITE.getBackground());
                            this.start = location;
                            this.squares.get(this.start).setBackground(GREEN.getBackground());
                        }
                        this.movingStart = false;
                        this.movingEnd = false;
                        stage.setTitle("[" + this.status.name() + "] Search Algorithms: " + this.selectedAlgorithmString);
                    } else if (this.movingEnd) {
                        if (location.equals(this.end)) {
                            this.movingStart = false;
                            this.movingEnd = false;
                            stage.setTitle("[" + this.status.name() + "] Search Algorithms: " + this.selectedAlgorithmString);
                            return;
                        } else if (location.equals(this.start)) {
                            Location tmp = this.end;
                            this.end = this.start;
                            this.start = tmp;
                            this.squares.get(this.start).setBackground(GREEN.getBackground());
                            this.squares.get(this.end).setBackground(RED.getBackground());
                        } else {
                            this.squares.get(this.end).setBackground(WHITE.getBackground());
                            this.end = location;
                            this.squares.get(this.end).setBackground(RED.getBackground());
                        }
                        this.movingEnd = false;
                        this.movingStart = false;
                        stage.setTitle("[" + this.status.name() + "] Search Algorithms: " + this.selectedAlgorithmString);
                    } else if (location.equals(this.start) && !this.movingStart) {
                        stage.setTitle("[" + this.status.name() + "] Search Algorithms: Moving Start");
                        this.movingStart = true;
                        this.movingEnd = false;
                    } else if (location.equals(this.end) && !this.movingEnd) {
                        stage.setTitle("[" + this.status.name() + "] Search Algorithms: Moving End");
                        this.movingEnd = true;
                        this.movingStart = false;
                    } else {
                        if (this.walls.contains(location)) {
                            cell.setBackground(WHITE.getBackground());
                            this.walls.remove(location);
                            int[] rowOffsets = {0, 0, 1, -1};
                            int[] colOffsets = {1, -1, 0, 0};
                            for (int i = 0; i < 4; i++) {
                                Location neighborLocation = new Location(location.getX() + colOffsets[i], location.getY() + rowOffsets[i]);
                                if (this.graph.contains(neighborLocation) && !this.walls.contains(neighborLocation)) {
                                    this.graph.connect_undirected(location, neighborLocation, 1);
                                }
                            }
                        } else if (!(location.equals(this.start) || location.equals(this.end))) {
                            cell.setBackground(BLACK.getBackground());
                            this.walls.add(location);
                            WVertex<Location> vertex = this.graph.vertices.get(location);
                            vertex.getNeighbors().clear();
                        }
                    }
                });
                newGridMaze.add(cell, colFinal, rowFinal);
                this.squares.set(rowFinal, colFinal, cell);
            }
        }
        return newGridMaze;
    }

    private void resetStartAndEndLocations() {
        this.start = new Location(this.graph.getRows() / 2, 1);
        this.end = new Location(this.graph.getRows() / 2, this.graph.getCols() - 2);

        if (this.start.equals(this.end)) {
            this.start = new Location(0, 0);
            this.end = new Location(this.graph.getRows() - 1, this.graph.getCols() - 1);
        }

        this.squares.get(this.start).setBackground(GREEN.getBackground());
        this.squares.get(this.end).setBackground(RED.getBackground());
    }

    private void updateWindowDimensions(Stage stage) {
        int rows = this.graph.getRows();
        int cols = this.graph.getCols();
        double newHeight = 5 + (rows - 1) + ((rows + 2) * this.squareSize);
        if (newHeight < 238.5) newHeight = 238.5;
        stage.setHeight(newHeight);

        double newWidth = 272 + (cols - 1) + ((cols + 2) * this.squareSize);
        stage.setWidth(newWidth);
    }

    private void updateSearchAlgorithm(Stage stage) {
        switch (this.selectedAlgorithmString) {
            case "Breadth First Search":
                this.searchAlgorithm = new BreadthFirstSearch<>(this.graph);
                break;
            case "Depth First Search":
                this.searchAlgorithm = new DepthFirstSearch<>(this.graph);
                break;
            case "Dijkstra's":
                this.searchAlgorithm = new Dijkstras<>(this.graph);
                break;
            case "A*":
                this.searchAlgorithm = new AStar<>(this.graph);
                break;
            default:
                break;
        }
        this.searchAlgorithm.setDelay(this.delay);
        stage.setTitle("[" + this.status.name() + "] Search Algorithms: " + this.selectedAlgorithmString);
        this.searchAlgorithm.registerObserver(location -> {
            if (location.equals(this.start) || location.equals(this.end)) return;
            if (this.walls.contains(location)) return;
            StackPane square = this.squares.get(location);
            GridVertex gv = (GridVertex) this.graph.vertices.get(location);
            if (gv.getStatus() != null) switch (gv.getStatus()) {
                case QUEUED:
                    square.setBackground(CORNFLOWERBLUE.getBackground());
                    break;
                case VISITED:
                    square.setBackground(BLUE.getBackground());
                    break;
                case PATH:
                    square.setBackground(YELLOW.getBackground());
                    break;
                default:
                    break;
            }
        });
    }

    private Integer tryParseInt(String text) {
        try {
            return Integer.valueOf(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Long tryParseLong(String text) {
        try {
            return Long.valueOf(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    private void setVerticesToDefault() {
        for (Location location : this.graph.vertices.keySet()) {
            GridVertex gv = (GridVertex) this.graph.vertices.get(location);
            gv.setDefault();
            if (this.walls.contains(location)) {
                this.squares.get(location).setBackground(BLACK.getBackground());
            } else if (!location.equals(this.start) && !location.equals(this.end)) {
                this.squares.get(location).setBackground(WHITE.getBackground());
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
