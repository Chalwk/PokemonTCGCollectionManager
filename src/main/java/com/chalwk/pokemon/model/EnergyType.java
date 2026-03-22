// MIT License
// Copyright (c) 2026, Jericho Crosby (Chalwk)
// Project: PokemonTCGCollectionManager

package com.chalwk.pokemon.model;

public enum EnergyType {
    FIRE("Fire", "🔴"),
    WATER("Water", "🔵"),
    GRASS("Grass", "🟢"),
    LIGHTNING("Lightning", "⚡"),
    PSYCHIC("Psychic", "🔮"),
    FIGHTING("Fighting", "🟤"),
    DARKNESS("Darkness", "⚫"),
    METAL("Metal", "⚙️"),
    FAIRY("Fairy", "💖"),
    DRAGON("Dragon", "🐉"),
    COLORLESS("Colorless", "⬜");

    private final String displayName;
    private final String emoji;

    EnergyType(String displayName, String emoji) {
        this.displayName = displayName;
        this.emoji = emoji;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmoji() {
        return emoji;
    }

    @Override
    public String toString() {
        return emoji + " " + displayName;
    }
}