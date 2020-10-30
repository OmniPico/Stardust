package com.omnipico.stardust;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public class StardustLootTableEntry {
    Material material;
    int weight;
    int min;
    int max;
    public StardustLootTableEntry(Material material, ConfigurationSection entrySection) {
        this.material = material;
        weight = entrySection.getInt("weight");
        min = entrySection.getInt("min");
        max = entrySection.getInt(("max"));
    }
    public StardustLootTableEntry(Material material, int weight, int min, int max) {
        this.material = material;
        this.weight = weight;
        this.min = min;
        this.max = max;
    }
}
