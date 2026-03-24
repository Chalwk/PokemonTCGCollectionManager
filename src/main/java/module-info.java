module com.chalwk.pokemon {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;

    exports com.chalwk.pokemon;
    opens com.chalwk.pokemon.model to com.fasterxml.jackson.databind;
    opens com.chalwk.pokemon.util to com.fasterxml.jackson.databind;
    opens com.chalwk.pokemon to javafx.fxml;
}