module com.chalwk.pokemon {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;

    opens com.chalwk.pokemon to javafx.fxml;
    opens com.chalwk.pokemon.util to com.fasterxml.jackson.databind;

    exports com.chalwk.pokemon;
    exports com.chalwk.pokemon.model;
    exports com.chalwk.pokemon.util;
}