// MIT License
// Copyright (c) 2026, Jericho Crosby (Chalwk)
// Project: PokemonTCGCollectionManager

package com.chalwk.pokemon.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Attack(String name, int damage, List<String> cost, String effect) {
    @JsonCreator
    public Attack(
            @JsonProperty("name") String name,
            @JsonProperty("damage") int damage,
            @JsonProperty("cost") List<String> cost,
            @JsonProperty("effect") String effect) {
        this.name = name;
        this.damage = damage;
        this.cost = cost;
        this.effect = effect;
    }
}