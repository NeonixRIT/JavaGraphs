module unit08 {
    requires transitive javafx.graphics;
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.media;
    requires java.desktop;

    opens unit08 to javafx.fxml;
    exports unit08;
}
