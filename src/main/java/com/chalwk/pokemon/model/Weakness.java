// MIT License
// Copyright (c) 2026, Jericho Crosby (Chalwk)
// Project: PokemonTCGCollectionManager

package com.chalwk.pokemon.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;

import java.util.List;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public record Weakness(String symbol, int multiplier) {

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

    @JsonGetter("multiplier")
    public String getMultiplierAsString() {
        return String.valueOf(multiplier);
    }
}