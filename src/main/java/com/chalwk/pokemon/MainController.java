// MIT License
// Copyright (c) 2026, Jericho Crosby (Chalwk)
// Project: PokemonTCGCollectionManager

package com.chalwk.pokemon;

import com.chalwk.pokemon.model.*;
import com.chalwk.pokemon.util.DataManager;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {

    private static final List<String> RARITIES = Arrays.asList(
            "Common", "Uncommon", "Rare", "Double Rare", "Ultra Rare", "Secret Rare",
            "Illustration Rare", "Special Illustration Rare", "Hyper Rare", "Amazing Rare",
            "Prism Rare", "Shiny Rare", "Radiant Rare", "Trainer Gallery Rare", "Galarian Gallery",
            "Pokémon V", "Pokémon VMAX", "Pokémon VSTAR", "Pokémon EX", "Pokémon GX",
            "Pokémon Tag Team GX", "Pokémon LV.X", "Pokémon Prime", "Pokémon LEGEND",
            "Pokémon BREAK", "Pokémon SP", "Pokémon Star", "Pokémon Crystal", "Pokémon Shining",
            "Pokémon ex", "Pokémon Lv.X", "Pokémon M", "Pokémon Mega", "Pokémon Prism",
            "Pokémon ACE SPEC", "Pokémon RADIANT", "Pokémon SHINY"
    );

    @FXML
    private TableView<Object> cardsTable;
    @FXML
    private TableColumn<Object, String> nameColumn;
    @FXML
    private TableColumn<Object, String> typeColumn;
    @FXML
    private TableColumn<Object, Integer> countColumn;
    @FXML
    private TableColumn<Object, Boolean> holoColumn;
    @FXML
    private TableColumn<Object, String> setColumn;
    @FXML
    private TableColumn<Object, String> rarityColumn;
    @FXML
    private TableColumn<Object, String> extraColumn;

    @FXML
    private ComboBox<String> typeFilter;
    @FXML
    private TextField searchField;
    @FXML
    private Label statusLabel;

    private PokemonCollection collection;
    private FilteredList<Object> filteredCards;
    private Stage primaryStage;

    private static ListView<Attack> getAttackListView(ObservableList<Attack> attacks) {
        ListView<Attack> attacksListView = new ListView<>(attacks);
        attacksListView.setCellFactory(lv -> new ListCell<Attack>() {
            @Override
            protected void updateItem(Attack attack, boolean empty) {
                super.updateItem(attack, empty);
                if (empty || attack == null) {
                    setText(null);
                } else {
                    setText(attack.name() + " (" + attack.damage() + " dmg)");
                }
            }
        });
        attacksListView.setPrefHeight(150);
        return attacksListView;
    }

    private static Weakness getWeakness(ComboBox<String> weaknessTypeCombo, TextField weaknessMultiplierField) {
        Weakness weakness = null;
        String selectedWeakness = weaknessTypeCombo.getSelectionModel().getSelectedItem();
        if (selectedWeakness != null && !"None".equals(selectedWeakness)) {
            int multiplier = Integer.parseInt(weaknessMultiplierField.getText().trim());
            weakness = new Weakness(selectedWeakness, multiplier);
        }
        return weakness;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        collection = DataManager.loadData();
        setupTable();
        setupFilters();
        updateStatus();
    }

    private void setupTable() {
        nameColumn.setCellValueFactory(cellData -> {
            Object cardObj = cellData.getValue();
            if (cardObj instanceof Card card) {
                return new SimpleStringProperty(card.getName());
            }
            return new SimpleStringProperty("");
        });

        typeColumn.setCellValueFactory(cellData -> {
            PokemonCollection.CardType type = collection.getCardType(cellData.getValue());
            return new SimpleStringProperty(type != null ? type.name() : "");
        });

        countColumn.setCellValueFactory(cellData -> {
            Object cardObj = cellData.getValue();
            int count = (cardObj instanceof Card card) ? card.getCount() : 0;
            return new SimpleIntegerProperty(count).asObject();
        });

        holoColumn.setCellValueFactory(cellData -> {
            Object cardObj = cellData.getValue();
            boolean holo = (cardObj instanceof Card card) && card.isHolo();
            return new SimpleBooleanProperty(holo);
        });

        setColumn.setCellValueFactory(cellData -> {
            Object cardObj = cellData.getValue();
            String setPart = (cardObj instanceof Card card) ? card.getSetPart() : "";
            return new SimpleStringProperty(setPart);
        });

        rarityColumn.setCellValueFactory(cellData -> {
            Object cardObj = cellData.getValue();
            String rarity = (cardObj instanceof Card card) ? card.getRarity() : "";
            return new SimpleStringProperty(rarity);
        });

        extraColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue() instanceof PokemonCard pc) {
                String weaknessStr = pc.getWeakness() == null ? "" :
                        pc.getWeakness().symbol() + "×" + pc.getWeakness().multiplier();
                return new SimpleStringProperty(pc.getHp() + " HP, " + pc.getStage() +
                        (weaknessStr.isEmpty() ? "" : ", Weak: " + weaknessStr));
            } else {
                return new SimpleStringProperty("");
            }
        });

        filteredCards = new FilteredList<>(collection.getAllCards());
        SortedList<Object> sortedCards = new SortedList<>(filteredCards);
        sortedCards.comparatorProperty().bind(cardsTable.comparatorProperty());
        cardsTable.setItems(sortedCards);

        cardsTable.setRowFactory(tv -> {
            TableRow<Object> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    editCard();
                }
            });
            return row;
        });
    }

    private void setupFilters() {
        typeFilter.setItems(FXCollections.observableArrayList(
                "All",
                PokemonCollection.CardType.ENERGY.name(),
                PokemonCollection.CardType.SUPPORTER.name(),
                PokemonCollection.CardType.ITEM.name(),
                PokemonCollection.CardType.TOOL.name(),
                PokemonCollection.CardType.STADIUM.name(),
                PokemonCollection.CardType.POKEMON.name()
        ));
        typeFilter.getSelectionModel().select("All");

        typeFilter.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
    }

    private void applyFilters() {
        String selectedType = typeFilter.getSelectionModel().getSelectedItem();
        String searchText = searchField.getText().toLowerCase();

        filteredCards.setPredicate(cardObj -> {
            if (!"All".equals(selectedType)) {
                PokemonCollection.CardType type = collection.getCardType(cardObj);
                if (type == null || !type.name().equals(selectedType)) {
                    return false;
                }
            }
            if (!searchText.isEmpty() && cardObj instanceof Card card) {
                String name = card.getName() != null ? card.getName().toLowerCase() : "";
                String setPart = card.getSetPart() != null ? card.getSetPart().toLowerCase() : "";
                String rarity = card.getRarity() != null ? card.getRarity().toLowerCase() : "";
                return name.contains(searchText) || setPart.contains(searchText) || rarity.contains(searchText);
            }
            return true;
        });
    }

    private void updateStatus() {
        int total = collection.getAllCards().size();
        statusLabel.setText("Total cards: " + total);
    }

    @FXML
    private void addCard() {
        CardResult result = showRegularCardDialog(null, null);
        if (result != null) {
            collection.addCard(result.card, result.type);
            cardsTable.refresh();
            updateStatus();
            saveData();
        }
    }

    @FXML
    private void editCard() {
        Object selected = cardsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a card to edit.");
            return;
        }
        PokemonCollection.CardType type = collection.getCardType(selected);
        if (type == PokemonCollection.CardType.POKEMON) {
            showPokemonCardDialog((PokemonCard) selected);
        } else {
            CardResult result = showRegularCardDialog((Card) selected, type);
            if (result != null) {
                if (type != result.type) {
                    collection.removeCard(selected);
                    collection.addCard(result.card, result.type);
                } else {
                    Card existing = (Card) selected;
                    existing.setName(result.card.getName());
                    existing.setCount(result.card.getCount());
                    existing.setHolo(result.card.isHolo());
                    existing.setSetPart(result.card.getSetPart());
                    existing.setRarity(result.card.getRarity());
                    existing.setAttacks(result.card.getAttacks());
                }
                cardsTable.refresh();
                updateStatus();
                saveData();
            }
        }
    }

    @FXML
    private void deleteCard() {
        Object selected = cardsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a card to delete.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Card");
        confirm.setHeaderText("Delete card?");
        confirm.setContentText("Are you sure you want to delete this card?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                collection.removeCard(selected);
                cardsTable.refresh();
                updateStatus();
                saveData();
            }
        });
    }

    private Attack showAttackEditorDialog(Attack attack, String title) {
        Dialog<Attack> dialog = new Dialog<>();
        dialog.setTitle(title);

        ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        TextField nameField = new TextField(attack != null ? attack.name() : "");
        TextField damageField = new TextField(attack != null ? String.valueOf(attack.damage()) : "0");

        Map<EnergyType, Spinner<Integer>> costSpinners = new HashMap<>();
        GridPane costGrid = createCostGrid(attack != null ? attack.cost() : null, costSpinners);

        TextArea effectArea = new TextArea(attack != null ? attack.effect() : "");
        effectArea.setPrefRowCount(3);
        effectArea.setPrefWidth(300);
        effectArea.setWrapText(true);

        GridPane mainGrid = new GridPane();
        mainGrid.setHgap(10);
        mainGrid.setVgap(10);
        mainGrid.setPadding(new Insets(20, 150, 10, 10));

        int row = 0;
        mainGrid.add(new Label("Name:"), 0, row);
        mainGrid.add(nameField, 1, row);
        row++;
        mainGrid.add(new Label("Damage:"), 0, row);
        mainGrid.add(damageField, 1, row);
        row++;
        mainGrid.add(new Label("Cost:"), 0, row);
        mainGrid.add(costGrid, 1, row);
        row++;
        mainGrid.add(new Label("Effect:"), 0, row);
        mainGrid.add(effectArea, 1, row);

        ScrollPane scrollPane = new ScrollPane(mainGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        dialog.getDialogPane().setContent(scrollPane);

        dialog.setResultConverter(btn -> {
            if (btn == saveButton) {
                try {
                    int damage = Integer.parseInt(damageField.getText().trim());
                    String name = nameField.getText().trim();
                    List<String> cost = getStrings(costSpinners);
                    String effect = effectArea.getText().trim();
                    return new Attack(name, damage, cost, effect);
                } catch (NumberFormatException e) {
                    showAlert("Invalid Input", "Damage must be a number.");
                    return null;
                }
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }

    private static List<String> getStrings(Map<EnergyType, Spinner<Integer>> costSpinners) {
        List<String> cost = null;
        List<String> costList = new ArrayList<>();
        for (Map.Entry<EnergyType, Spinner<Integer>> entry : costSpinners.entrySet()) {
            int count = entry.getValue().getValue();
            String symbol = entry.getKey().getEmoji();
            for (int i = 0; i < count; i++) {
                costList.add(symbol);
            }
        }
        if (!costList.isEmpty()) {
            cost = costList;
        }
        return cost;
    }

    private GridPane createCostGrid(List<String> existingCost, Map<EnergyType, Spinner<Integer>> spinnersOut) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(5);
        Map<String, Integer> counts = new HashMap<>();
        if (existingCost != null) {
            for (String symbol : existingCost) {
                counts.put(symbol, counts.getOrDefault(symbol, 0) + 1);
            }
        }
        int row = 0;
        for (EnergyType et : EnergyType.values()) {
            String symbol = et.getEmoji();
            int initialValue = counts.getOrDefault(symbol, 0);
            Spinner<Integer> spinner = new Spinner<>(0, 10, initialValue);
            spinner.setEditable(true);
            spinnersOut.put(et, spinner);
            grid.add(new Label(et.name()), 0, row);
            grid.add(spinner, 1, row);
            row++;
        }
        return grid;
    }

    private VBox createAttackSection(ObservableList<Attack> attacks) {
        ListView<Attack> attacksListView = getAttackListView(attacks);

        attacksListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Attack selected = attacksListView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    Attack edited = showAttackEditorDialog(selected, "Edit Attack");
                    if (edited != null) {
                        int index = attacks.indexOf(selected);
                        attacks.set(index, edited);
                    }
                }
            }
        });

        Button addBtn = new Button("Add");
        Button editBtn = new Button("Edit");
        Button removeBtn = new Button("Remove");

        addBtn.setOnAction(e -> {
            Attack newAttack = showAttackEditorDialog(null, "New Attack");
            if (newAttack != null) attacks.add(newAttack);
        });
        editBtn.setOnAction(e -> {
            Attack selected = attacksListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Attack edited = showAttackEditorDialog(selected, "Edit Attack");
                if (edited != null) {
                    int index = attacks.indexOf(selected);
                    attacks.set(index, edited);
                }
            } else {
                showAlert("No Selection", "Select an attack to edit.");
            }
        });
        removeBtn.setOnAction(e -> {
            Attack selected = attacksListView.getSelectionModel().getSelectedItem();
            if (selected != null) attacks.remove(selected);
        });

        HBox btnBox = new HBox(10, addBtn, editBtn, removeBtn);
        return new VBox(5, new Label("Attacks:"), attacksListView, btnBox);
    }

    private CardResult showRegularCardDialog(Card existingCard, PokemonCollection.CardType initialType) {
        Dialog<CardResult> dialog = new Dialog<>();
        dialog.setTitle(existingCard == null ? "Add Card" : "Edit Card");

        ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        ObservableList<PokemonCollection.CardType> types = FXCollections.observableArrayList(
                PokemonCollection.CardType.values());
        types.remove(PokemonCollection.CardType.POKEMON);
        ComboBox<PokemonCollection.CardType> typeCombo = new ComboBox<>(types);
        if (initialType != null && types.contains(initialType)) {
            typeCombo.getSelectionModel().select(initialType);
        } else {
            typeCombo.getSelectionModel().selectFirst();
        }

        TextField nameField = new TextField(existingCard != null ? existingCard.getName() : "");
        TextField countField = new TextField(existingCard != null ? String.valueOf(existingCard.getCount()) : "1");
        CheckBox holoCheck = new CheckBox("Holo");
        if (existingCard != null) holoCheck.setSelected(existingCard.isHolo());
        TextField setField = new TextField(existingCard != null ? existingCard.getSetPart() : "");

        ComboBox<String> rarityCombo = new ComboBox<>(FXCollections.observableArrayList(RARITIES));
        if (existingCard != null && existingCard.getRarity() != null) {
            rarityCombo.getSelectionModel().select(existingCard.getRarity());
        } else {
            rarityCombo.getSelectionModel().selectFirst();
        }

        ObservableList<Attack> attacks = FXCollections.observableArrayList();
        if (existingCard != null && existingCard.getAttacks() != null) {
            attacks.addAll(existingCard.getAttacks());
        }
        VBox attackSection = createAttackSection(attacks);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));
        int row = 0;
        grid.add(new Label("Type:"), 0, row);
        grid.add(typeCombo, 1, row++);
        grid.add(new Label("Name:"), 0, row);
        grid.add(nameField, 1, row++);
        grid.add(new Label("Count:"), 0, row);
        grid.add(countField, 1, row++);
        grid.add(new Label("Holo:"), 0, row);
        grid.add(holoCheck, 1, row++);
        grid.add(new Label("Set Part:"), 0, row);
        grid.add(setField, 1, row++);
        grid.add(new Label("Rarity:"), 0, row);
        grid.add(rarityCombo, 1, row++);
        grid.add(attackSection, 0, row, 2, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(btn -> {
            if (btn == saveButton) {
                try {
                    int count = Integer.parseInt(countField.getText());
                    String name = nameField.getText();
                    String setPart = setField.getText();
                    String rarity = rarityCombo.getSelectionModel().getSelectedItem();
                    boolean holo = holoCheck.isSelected();
                    Card card = new Card(name, count, holo, setPart, rarity,
                            attacks.isEmpty() ? null : List.copyOf(attacks));
                    return new CardResult(card, typeCombo.getSelectionModel().getSelectedItem());
                } catch (NumberFormatException e) {
                    showAlert("Invalid Input", "Count must be a number.");
                    return null;
                }
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }

    private void showPokemonCardDialog(PokemonCard existingCard) {
        Dialog<PokemonCard> dialog = new Dialog<>();
        dialog.setTitle(existingCard == null ? "Add Pokémon Card" : "Edit Pokémon Card");

        ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        TextField nameField = new TextField(existingCard != null ? existingCard.getName() : "");
        TextField countField = new TextField(existingCard != null ? String.valueOf(existingCard.getCount()) : "1");
        CheckBox holoCheck = new CheckBox("Holo");
        if (existingCard != null) holoCheck.setSelected(existingCard.isHolo());
        TextField setField = new TextField(existingCard != null ? existingCard.getSetPart() : "");

        ComboBox<String> rarityCombo = new ComboBox<>(FXCollections.observableArrayList(RARITIES));
        if (existingCard != null && existingCard.getRarity() != null) {
            rarityCombo.getSelectionModel().select(existingCard.getRarity());
        } else {
            rarityCombo.getSelectionModel().selectFirst();
        }

        TextField hpField = new TextField(existingCard != null ? String.valueOf(existingCard.getHp()) : "");

        ComboBox<String> stageCombo = new ComboBox<>(FXCollections.observableArrayList("Basic", "Stage 1", "Stage 2"));
        if (existingCard != null && existingCard.getStage() != null) {
            stageCombo.getSelectionModel().select(existingCard.getStage());
        } else {
            stageCombo.getSelectionModel().selectFirst();
        }

        // ----- Pokémon type as ComboBox (change) -----
        ComboBox<String> typeCombo = new ComboBox<>();
        for (EnergyType et : EnergyType.values()) {
            typeCombo.getItems().add(et.getEmoji());
        }
        if (existingCard != null && existingCard.getType() != null) {
            if (typeCombo.getItems().contains(existingCard.getType())) {
                typeCombo.getSelectionModel().select(existingCard.getType());
            } else {
                typeCombo.getSelectionModel().selectFirst(); // fallback
            }
        } else {
            typeCombo.getSelectionModel().selectFirst();
        }
        // -----------------------------

        Weakness existingWeakness = existingCard != null ? existingCard.getWeakness() : null;

        ComboBox<String> weaknessTypeCombo = new ComboBox<>();
        weaknessTypeCombo.getItems().add("None");
        for (EnergyType et : EnergyType.values()) {
            weaknessTypeCombo.getItems().add(et.getEmoji());
        }
        TextField weaknessMultiplierField = new TextField();
        weaknessMultiplierField.setPromptText("Multiplier (e.g., 2)");
        weaknessMultiplierField.setDisable(true);

        weaknessTypeCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean enabled = newVal != null && !"None".equals(newVal);
            weaknessMultiplierField.setDisable(!enabled);
            if (!enabled) weaknessMultiplierField.clear();
        });

        if (existingWeakness != null) {
            String symbol = existingWeakness.symbol();
            if (weaknessTypeCombo.getItems().contains(symbol)) {
                weaknessTypeCombo.getSelectionModel().select(symbol);
            } else {
                weaknessTypeCombo.getSelectionModel().select("None");
            }
            weaknessMultiplierField.setText(String.valueOf(existingWeakness.multiplier()));
        } else {
            weaknessTypeCombo.getSelectionModel().select("None");
        }

        ComboBox<String> resistanceSymbolCombo = new ComboBox<>();
        resistanceSymbolCombo.getItems().add("None");
        for (EnergyType et : EnergyType.values()) {
            resistanceSymbolCombo.getItems().add(et.getEmoji());
        }
        TextField resistanceValueField = new TextField();
        resistanceValueField.setPromptText("Reduction (e.g., 30)");
        resistanceValueField.setDisable(true);

        resistanceSymbolCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean enabled = newVal != null && !"None".equals(newVal);
            resistanceValueField.setDisable(!enabled);
            if (!enabled) resistanceValueField.clear();
        });

        if (existingCard != null && existingCard.getResistance() != null) {
            Resistance res = existingCard.getResistance();
            String symbol = res.symbol();
            if (resistanceSymbolCombo.getItems().contains(symbol)) {
                resistanceSymbolCombo.getSelectionModel().select(symbol);
            } else {
                resistanceSymbolCombo.getSelectionModel().select("None");
            }
            int reduction = -res.value();
            resistanceValueField.setText(String.valueOf(reduction));
        } else {
            resistanceSymbolCombo.getSelectionModel().select("None");
        }

        TextField retreatCostField = new TextField(existingCard != null ? String.valueOf(existingCard.getRetreatCost()) : "1");

        ObservableList<Attack> attacks = FXCollections.observableArrayList();
        if (existingCard != null && existingCard.getAttacks() != null) {
            attacks.addAll(existingCard.getAttacks());
        }
        VBox attackSection = createAttackSection(attacks);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));
        int row = 0;
        grid.add(new Label("Name:"), 0, row);
        grid.add(nameField, 1, row++);
        grid.add(new Label("Count:"), 0, row);
        grid.add(countField, 1, row++);
        grid.add(new Label("Holo:"), 0, row);
        grid.add(holoCheck, 1, row++);
        grid.add(new Label("Set Part:"), 0, row);
        grid.add(setField, 1, row++);
        grid.add(new Label("Rarity:"), 0, row);
        grid.add(rarityCombo, 1, row++);
        grid.add(new Label("HP:"), 0, row);
        grid.add(hpField, 1, row++);
        grid.add(new Label("Stage:"), 0, row);
        grid.add(stageCombo, 1, row++);
        grid.add(new Label("Type:"), 0, row);
        grid.add(typeCombo, 1, row++);
        grid.add(new Label("Weakness:"), 0, row);
        HBox weaknessBox = new HBox(10, weaknessTypeCombo, weaknessMultiplierField);
        grid.add(weaknessBox, 1, row++);
        grid.add(new Label("Resistance:"), 0, row);
        HBox resistanceBox = new HBox(10, resistanceSymbolCombo, resistanceValueField);
        grid.add(resistanceBox, 1, row++);
        grid.add(new Label("Retreat Cost:"), 0, row);
        grid.add(retreatCostField, 1, row++);
        grid.add(attackSection, 0, row, 2, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(btn -> {
            if (btn == saveButton) {
                try {
                    int count = Integer.parseInt(countField.getText());
                    int hp = Integer.parseInt(hpField.getText());
                    int retreatCost = Integer.parseInt(retreatCostField.getText());
                    String name = nameField.getText();
                    String setPart = setField.getText();
                    String rarity = rarityCombo.getSelectionModel().getSelectedItem();
                    boolean holo = holoCheck.isSelected();
                    String stage = stageCombo.getSelectionModel().getSelectedItem();
                    String type = typeCombo.getSelectionModel().getSelectedItem(); // now from ComboBox

                    if (type == null || type.isBlank()) {
                        showAlert("Invalid Input", "Pokémon type must be selected.");
                        return null;
                    }

                    Weakness weakness = getWeakness(weaknessTypeCombo, weaknessMultiplierField);

                    Resistance resistance = null;
                    String selectedRes = resistanceSymbolCombo.getSelectionModel().getSelectedItem();
                    if (selectedRes != null && !"None".equals(selectedRes)) {
                        int reduction = Integer.parseInt(resistanceValueField.getText().trim());
                        if (reduction > 0) {
                            resistance = new Resistance(selectedRes, -reduction);
                        } else {
                            showAlert("Invalid Input", "Resistance reduction must be a positive number.");
                            return null;
                        }
                    }

                    return new PokemonCard(name, count, holo, setPart, rarity,
                            attacks.isEmpty() ? null : List.copyOf(attacks),
                            hp, stage, type, weakness, resistance, retreatCost);
                } catch (NumberFormatException e) {
                    showAlert("Invalid Input", "Count, HP, and Retreat Cost must be numbers.");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(card -> {
            if (existingCard == null) {
                collection.addPokemonCard(card);
            } else {
                existingCard.setName(card.getName());
                existingCard.setCount(card.getCount());
                existingCard.setHolo(card.isHolo());
                existingCard.setSetPart(card.getSetPart());
                existingCard.setRarity(card.getRarity());
                existingCard.setAttacks(card.getAttacks());
                existingCard.setHp(card.getHp());
                existingCard.setStage(card.getStage());
                existingCard.setType(card.getType());
                existingCard.setWeakness(card.getWeakness());
                existingCard.setResistance(card.getResistance());
                existingCard.setRetreatCost(card.getRetreatCost());
            }
            cardsTable.refresh();
            updateStatus();
            saveData();
        });
    }

    @FXML
    private void importData() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Pokémon Collection JSON");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File lastDir = DataManager.getLastUsedDirectory();
        if (lastDir != null && lastDir.isDirectory()) {
            fileChooser.setInitialDirectory(lastDir);
        }
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            try {
                collection = DataManager.loadData(file);
                filteredCards = new FilteredList<>(collection.getAllCards());
                SortedList<Object> sortedCards = new SortedList<>(filteredCards);
                sortedCards.comparatorProperty().bind(cardsTable.comparatorProperty());
                cardsTable.setItems(sortedCards);
                applyFilters();
                cardsTable.refresh();
                updateStatus();
                showAlert("Success", "Data imported from: " + file.getAbsolutePath());
            } catch (Exception e) {
                showAlert("Error", "Failed to import: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void exportData() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Pokémon Collection JSON");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File lastDir = DataManager.getLastUsedDirectory();
        if (lastDir != null && lastDir.isDirectory()) {
            fileChooser.setInitialDirectory(lastDir);
        }
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            DataManager.saveData(collection, file);
            showAlert("Success", "Data exported to: " + file.getAbsolutePath());
        }
    }

    private void saveData() {
        if (javafx.application.Platform.isFxApplicationThread()) {
            DataManager.saveData(collection);
        } else {
            javafx.application.Platform.runLater(() -> DataManager.saveData(collection));
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private static class CardResult {
        Card card;
        PokemonCollection.CardType type;

        CardResult(Card card, PokemonCollection.CardType type) {
            this.card = card;
            this.type = type;
        }
    }
}