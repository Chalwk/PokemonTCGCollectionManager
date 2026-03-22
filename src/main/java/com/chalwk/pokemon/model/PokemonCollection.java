package com.chalwk.pokemon.model;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class PokemonCollection {
    private final ObservableList<Card> energyCards = FXCollections.observableArrayList();
    private final ObservableList<Card> supporterCards = FXCollections.observableArrayList();
    private final ObservableList<Card> itemCards = FXCollections.observableArrayList();
    private final ObservableList<Card> toolCards = FXCollections.observableArrayList();
    private final ObservableList<PokemonCard> pokemonCards = FXCollections.observableArrayList();

    private final ObservableList<Object> allCards = FXCollections.observableArrayList();

    public PokemonCollection() {
        rebuildCombinedList();
        energyCards.addListener((ListChangeListener.Change<? extends Card> c) -> rebuildCombinedList());
        supporterCards.addListener((ListChangeListener.Change<? extends Card> c) -> rebuildCombinedList());
        itemCards.addListener((ListChangeListener.Change<? extends Card> c) -> rebuildCombinedList());
        toolCards.addListener((ListChangeListener.Change<? extends Card> c) -> rebuildCombinedList());
        pokemonCards.addListener((ListChangeListener.Change<? extends PokemonCard> c) -> rebuildCombinedList());
    }

    private void rebuildCombinedList() {
        allCards.clear();
        allCards.addAll(energyCards);
        allCards.addAll(supporterCards);
        allCards.addAll(itemCards);
        allCards.addAll(toolCards);
        allCards.addAll(pokemonCards);
    }

    public ObservableList<Object> getAllCards() {
        return allCards;
    }

    public ObservableList<Card> getEnergyCards() {
        return energyCards;
    }

    public ObservableList<Card> getSupporterCards() {
        return supporterCards;
    }

    public ObservableList<Card> getItemCards() {
        return itemCards;
    }

    public ObservableList<Card> getToolCards() {
        return toolCards;
    }

    public ObservableList<PokemonCard> getPokemonCards() {
        return pokemonCards;
    }

    public void addCard(Card card, CardType type) {
        switch (type) {
            case ENERGY -> energyCards.add(card);
            case SUPPORTER -> supporterCards.add(card);
            case ITEM -> itemCards.add(card);
            case TOOL -> toolCards.add(card);
        }
    }

    public void addPokemonCard(PokemonCard card) {
        pokemonCards.add(card);
    }

    public void removeCard(Object card) {
        if (card instanceof Card) {
            Card c = (Card) card;
            if (energyCards.remove(c)) return;
            if (supporterCards.remove(c)) return;
            if (itemCards.remove(c)) return;
            if (toolCards.remove(c)) return;
        } else if (card instanceof PokemonCard) {
            pokemonCards.remove(card);
        }
    }

    public CardType getCardType(Object card) {
        if (card instanceof PokemonCard) return CardType.POKEMON;
        if (energyCards.contains(card)) return CardType.ENERGY;
        if (supporterCards.contains(card)) return CardType.SUPPORTER;
        if (itemCards.contains(card)) return CardType.ITEM;
        if (toolCards.contains(card)) return CardType.TOOL;
        return null;
    }

    public enum CardType {
        ENERGY, SUPPORTER, ITEM, TOOL, POKEMON
    }
}