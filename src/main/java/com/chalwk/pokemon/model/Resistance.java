// MIT License
// Copyright (c) 2026, Jericho Crosby (Chalwk)
// Project: PokemonTCGCollectionManager

package com.chalwk.pokemon.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public class Resistance {
    private String symbol;
    private int value;

    public Resistance(String symbol, int value) {
        this.symbol = symbol;
        this.value = value;
    }

    @JsonCreator
    public Resistance(
            @JsonProperty("symbol") String symbol,
            @JsonProperty("value") String valueStr) {
        this.symbol = symbol;
        this.value = Integer.parseInt(valueStr);
    }

    @JsonGetter("value")
    public String getValueAsString() {
        return String.valueOf(value);
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}