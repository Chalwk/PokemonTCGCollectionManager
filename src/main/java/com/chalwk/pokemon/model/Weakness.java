// MIT License
// Copyright (c) 2026, Jericho Crosby (Chalwk)
// Project: PokemonTCGCollectionManager

package com.chalwk.pokemon.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public class Weakness {
    private String symbol;
    private int multiplier;

    public Weakness(String symbol, int multiplier) {
        this.symbol = symbol;
        this.multiplier = multiplier;
    }

    @JsonCreator
    public Weakness(
            @JsonProperty("symbol") String symbol,
            @JsonProperty("multiplier") String multiplierStr) {
        this.symbol = symbol;
        this.multiplier = Integer.parseInt(multiplierStr);
    }

    @JsonGetter("multiplier")
    public String getMultiplierAsString() {
        return String.valueOf(multiplier);
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }
}