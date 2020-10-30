package com.omnipico.stardust;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.Set;

public class StardustLootTable implements LootTable {

    private Plugin plugin = Stardust.getPlugin(Stardust.class);
    private NamespacedKey key;
    int picks;
    WeightedRandomBag<StardustLootTableEntry> pool;

    public StardustLootTable(String name, int picks, WeightedRandomBag<StardustLootTableEntry> pool) {
        key = new NamespacedKey(plugin, name);
        this.picks = picks;
        this.pool = pool;
    }

    public StardustLootTable(String name, ConfigurationSection config) {
        key = new NamespacedKey(plugin, name);
        picks = config.getInt("picks");
        pool = new WeightedRandomBag<>();
        ConfigurationSection itemSection = config.getConfigurationSection("items");
        if (itemSection != null) {
            Set<String> materialNames = itemSection.getKeys(false);
            for (String materialName : materialNames) {
                Material material = Material.valueOf(materialName);
                ConfigurationSection entrySection = itemSection.getConfigurationSection(materialName);
                if (entrySection != null) {
                    StardustLootTableEntry entry = new StardustLootTableEntry(material, entrySection);
                    pool.addEntry(entry, entry.weight);
                }
            }
        }
    }

    @Override
    public Collection<ItemStack> populateLoot(Random random, LootContext lootContext) {
        ArrayList<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < picks; i++) {
            StardustLootTableEntry entry = pool.getRandom();
            items.add(new ItemStack(entry.material, random.nextInt(entry.max-entry.min+1)+entry.min));
        }
        return items;
    }

    @Override
    public void fillInventory(Inventory inventory, Random random, LootContext lootContext) {
        Collection<ItemStack> items = populateLoot(random, lootContext);
        int i = 0;
        for (ItemStack item : items) {
            if (i < inventory.getSize()) {
                inventory.setItem(i, item);
            }
        }
    }

    @Override
    public NamespacedKey getKey() {
        return key;
    }
}
