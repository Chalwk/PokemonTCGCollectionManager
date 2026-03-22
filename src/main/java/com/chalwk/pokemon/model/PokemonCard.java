// MIT License
// Copyright (c) 2026, Jericho Crosby (Chalwk)
// Project: PokemonTCGCollectionManager

package com.chalwk.pokemon.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonCard extends Card {
    private int hp;
    private String stage;
    private String type;
    private List<String> weakness;
    private Resistance resistance;
    private int retreatCost;

    @JsonCreator
    public PokemonCard(
            @JsonProperty("name") String name,
            @JsonProperty("count") int count,
            @JsonProperty("holo") boolean holo,
            @JsonProperty("setPart") String setPart,
            @JsonProperty("rarity") String rarity,
            @JsonProperty("attacks") List<Attack> attacks,
            @JsonProperty("hp") int hp,
            @JsonProperty("stage") String stage,
            @JsonProperty("type") String type,
            @JsonProperty("weakness") List<String> weakness,
            @JsonProperty("resistance") Resistance resistance,
            @JsonProperty("retreatCost") int retreatCost) {
        super(name, count, holo, setPart, rarity, attacks);
        this.hp = hp;
        this.stage = stage;
        this.type = type;
        this.weakness = weakness;
        this.resistance = resistance;
        this.retreatCost = retreatCost;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getWeakness() {
        return weakness;
    }

    public void setWeakness(List<String> weakness) {
        this.weakness = weakness;
    }

    public Resistance getResistance() {
        return resistance;
    }

    public void setResistance(Resistance resistance) {  // changed parameter type
        this.resistance = resistance;
    }

    public int getRetreatCost() {
        return retreatCost;
    }

    public void setRetreatCost(int retreatCost) {
        this.retreatCost = retreatCost;
    }
}