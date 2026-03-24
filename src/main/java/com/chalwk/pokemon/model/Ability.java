// MIT License
// Copyright (c) 2026, Jericho Crosby (Chalwk)
// Project: PokemonTCGCollectionManager

package com.chalwk.pokemon.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Ability(String name, String description) {

 @JsonCreator
 public Ability(
         @JsonProperty("name") String name,
         @JsonProperty("description") String description) {
  this.name = name;
  this.description = description;
 }
}