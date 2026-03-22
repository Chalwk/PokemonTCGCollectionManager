package com.chalwk.pokemon.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Card {
    private String name;
    private int count;
    private boolean holo;
    private String setPart;
    private String rarity;
    private List<Attack> attacks;

    @JsonCreator
    public Card(
            @JsonProperty("name") String name,
            @JsonProperty("count") int count,
            @JsonProperty("holo") boolean holo,
            @JsonProperty("setPart") String setPart,
            @JsonProperty("rarity") String rarity,
            @JsonProperty("attacks") List<Attack> attacks) {
        this.name = name;
        this.count = count;
        this.holo = holo;
        this.setPart = setPart;
        this.rarity = rarity;
        this.attacks = attacks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isHolo() {
        return holo;
    }

    public void setHolo(boolean holo) {
        this.holo = holo;
    }

    public String getSetPart() {
        return setPart;
    }

    public void setSetPart(String setPart) {
        this.setPart = setPart;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public List<Attack> getAttacks() {
        return attacks;
    }

    public void setAttacks(List<Attack> attacks) {
        this.attacks = attacks;
    }
}