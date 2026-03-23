// MIT License
// Copyright (c) 2026, Jericho Crosby (Chalwk)
// Project: PokemonTCGCollectionManager

package com.chalwk.pokemon.model;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.List;

public class PokemonCollection {
    private final ObservableList<Card> energyCards = FXCollections.observableArrayList();
    private final ObservableList<Card> supporterCards = FXCollections.observableArrayList();
    private final ObservableList<Card> itemCards = FXCollections.observableArrayList();
    private final ObservableList<Card> toolCards = FXCollections.observableArrayList();
    private final ObservableList<Card> stadiumCards = FXCollections.observableArrayList();
    private final ObservableList<PokemonCard> pokemonCards = FXCollections.observableArrayList();

    private final ObservableList<Object> allCards = FXCollections.observableArrayList();

    public PokemonCollection() {
        rebuildCombinedList();
        energyCards.addListener((ListChangeListener.Change<? extends Card> c) -> rebuildCombinedList());
        supporterCards.addListener((ListChangeListener.Change<? extends Card> c) -> rebuildCombinedList());
        itemCards.addListener((ListChangeListener.Change<? extends Card> c) -> rebuildCombinedList());
        toolCards.addListener((ListChangeListener.Change<? extends Card> c) -> rebuildCombinedList());
        stadiumCards.addListener((ListChangeListener.Change<? extends Card> c) -> rebuildCombinedList());
        pokemonCards.addListener((ListChangeListener.Change<? extends PokemonCard> c) -> rebuildCombinedList());
    }

    private void rebuildCombinedList() {
        allCards.clear();
        allCards.addAll(energyCards);
        allCards.addAll(supporterCards);
        allCards.addAll(itemCards);
        allCards.addAll(toolCards);
        allCards.addAll(stadiumCards);
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

    public ObservableList<Card> getStadiumCards() {
        return stadiumCards;
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
            case STADIUM -> stadiumCards.add(card);
            default -> throw new IllegalArgumentException("Unsupported card type for addCard: " + type);
        }
    }

    public void addPokemonCard(PokemonCard card) {
        pokemonCards.add(card);
    }

    public void removeCard(Object card) {
        if (card instanceof PokemonCard pc) {
            pokemonCards.remove(pc);
            return;
        }

        if (card instanceof Card c) {
            List<ObservableList<Card>> lists = List.of(energyCards, supporterCards, itemCards, toolCards, stadiumCards);
            for (ObservableList<Card> list : lists) {
                if (list.remove(c)) {
                    return;
                }
            }
        }
    }

    public CardType getCardType(Object card) {
        if (card instanceof PokemonCard) return CardType.POKEMON;
        if (card instanceof Card c) {
            if (energyCards.contains(c)) return CardType.ENERGY;
            if (supporterCards.contains(c)) return CardType.SUPPORTER;
            if (itemCards.contains(c)) return CardType.ITEM;
            if (toolCards.contains(c)) return CardType.TOOL;
            if (stadiumCards.contains(c)) return CardType.STADIUM;
        }
        return null;
    }

    public enum CardType {
        ENERGY, SUPPORTER, ITEM, TOOL, STADIUM, POKEMON
    }
}