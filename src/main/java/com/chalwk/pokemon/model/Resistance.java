// MIT License
// Copyright (c) 2026, Jericho Crosby (Chalwk)
// Project: PokemonTCGCollectionManager

package com.chalwk.pokemon.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public record Resistance(String symbol, int value) {

    @JsonCreator
    public Resistance(
            @JsonProperty("symbol") String symbol,
            @JsonProperty("value") String valueStr) {
        this(symbol, Integer.parseInt(valueStr));
    }

    @JsonGetter("value")
    public String getValueAsString() {
        return String.valueOf(value);
    }
}