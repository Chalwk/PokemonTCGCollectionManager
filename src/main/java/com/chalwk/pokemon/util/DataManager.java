// MIT License
// Copyright (c) 2026, Jericho Crosby (Chalwk)
// Project: PokemonTCGCollectionManager

package com.chalwk.pokemon.util;

import com.chalwk.pokemon.model.Card;
import com.chalwk.pokemon.model.PokemonCard;
import com.chalwk.pokemon.model.PokemonCollection;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static final String DATA_FILE = getDataDirectory() + File.separator + "pokemon_collection.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static File lastUsedDirectory = null;

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    private static String getDataDirectory() {
        String userHome = System.getProperty("user.home");
        String appDataDir = userHome + File.separator + "PokemonCollection";
        File dir = new File(appDataDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return appDataDir;
    }

    public static File getLastUsedDirectory() {
        return lastUsedDirectory;
    }

    public static void setLastUsedDirectory(File directory) {
        if (directory != null && directory.isDirectory()) {
            lastUsedDirectory = directory;
        }
    }

    public static void saveData(PokemonCollection collection) {
        saveData(collection, new File(DATA_FILE));
    }

    public static void saveData(PokemonCollection collection, File file) {
        try {
            RootData root = new RootData();
            root.ENERGY = new ArrayList<>(collection.getEnergyCards());
            root.SUPPORTER = new ArrayList<>(collection.getSupporterCards());
            root.ITEM = new ArrayList<>(collection.getItemCards());
            root.TOOL = new ArrayList<>(collection.getToolCards());
            root.POKEMON = new ArrayList<>(collection.getPokemonCards());
            objectMapper.writeValue(file, root);
            System.out.println("Data saved to: " + file.getAbsolutePath());
            setLastUsedDirectory(file.getParentFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PokemonCollection loadData() {
        return loadData(new File(DATA_FILE));
    }

    public static PokemonCollection loadData(File file) {
        try {
            setLastUsedDirectory(file.getParentFile());

            if (file.exists()) {
                RootData root = objectMapper.readValue(file, RootData.class);
                PokemonCollection collection = new PokemonCollection();
                collection.getEnergyCards().addAll(root.ENERGY != null ? root.ENERGY : new ArrayList<>());
                collection.getSupporterCards().addAll(root.SUPPORTER != null ? root.SUPPORTER : new ArrayList<>());
                collection.getItemCards().addAll(root.ITEM != null ? root.ITEM : new ArrayList<>());
                collection.getToolCards().addAll(root.TOOL != null ? root.TOOL : new ArrayList<>());
                collection.getPokemonCards().addAll(root.POKEMON != null ? root.POKEMON : new ArrayList<>());
                return collection;
            } else {
                System.out.println("No existing data file found, starting with empty collection.");
                return new PokemonCollection();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new PokemonCollection();
        }
    }

    public static class RootData {
        public List<Card> ENERGY;
        public List<Card> SUPPORTER;
        public List<Card> ITEM;
        public List<Card> TOOL;
        public List<PokemonCard> POKEMON;
        public RootData() {}
    }
}