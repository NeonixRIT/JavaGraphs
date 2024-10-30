module unit08 {
    requires transitive javafx.graphics;
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.media;
    requires java.desktop;

    opens graphsAndAlgorithms to javafx.fxml;
    exports graphsAndAlgorithms;
}
