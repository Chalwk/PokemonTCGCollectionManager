// MIT License
// Copyright (c) 2026, Jericho Crosby (Chalwk)
// Project: PokemonTCGCollectionManager

package com.chalwk.pokemon.model;

public enum EnergyType {
    FIRE("Fire", "FIRE"),
    WATER("Water", "WATER"),
    GRASS("Grass", "GRASS"),
    LIGHTNING("Lightning", "LIGHTNING"),
    PSYCHIC("Psychic", "PSYCHIC"),
    FIGHTING("Fighting", "FIGHTING"),
    DARKNESS("Darkness", "DARKNESS"),
    METAL("Metal", "METAL"),
    FAIRY("Fairy", "FAIRY"),
    DRAGON("Dragon", "DRAGON"),
    COLORLESS("Colorless", "COLORLESS");

    private final String displayName;
    private final String emoji;

    EnergyType(String displayName, String emoji) {
        this.displayName = displayName;
        this.emoji = emoji;
    }

    public String getEmoji() {
        return emoji;
    }

    @Override
    public String toString() {
        return emoji + " " + displayName;
    }
}