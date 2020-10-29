package com.omnipico.stardust;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class RareLootTable implements LootTable {

    private Plugin plugin = Stardust.getPlugin(Stardust.class);
    private NamespacedKey key = new NamespacedKey(plugin, "rare");

    @Override
    public Collection<ItemStack> populateLoot(Random random, LootContext lootContext) {
        ArrayList<ItemStack> items = new ArrayList<>();
        double rng = random.nextDouble();
        if (rng < 0.1) {
            items.add(new ItemStack(Material.DIAMOND, random.nextInt(3)+1));
        } else if (rng < 0.5) {
            items.add(new ItemStack(Material.EXPERIENCE_BOTTLE, random.nextInt(5)+1));
        } else {
            items.add(new ItemStack(Material.IRON_INGOT, random.nextInt(8)+1));
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
