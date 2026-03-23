// MIT License
// Copyright (c) 2026, Jericho Crosby (Chalwk)
// Project: PokemonTCGCollectionManager

package com.chalwk.pokemon.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;

import java.util.List;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public class Weakness {
    private String symbol;
    private int multiplier;

    public Weakness(String symbol, int multiplier) {
        this.symbol = symbol;
        this.multiplier = multiplier;
    }

    @JsonCreator
    public static Weakness fromArray(List<String> arr) {
        if (arr == null || arr.size() < 2) {
            return null;
        }
        try {
            String symbol = arr.get(0);
            int multiplier = Integer.parseInt(arr.get(1));
            return new Weakness(symbol, multiplier);
        } catch (NumberFormatException e) {
            return null;
        }
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

    @JsonGetter("multiplier")
    public String getMultiplierAsString() {
        return String.valueOf(multiplier);
    }
}