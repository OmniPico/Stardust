package com.omnipico.stardust;

import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.loot.LootTable;

public class StarType {
    /* weight: 100
    trail_particle: END_ROD
    player_weight: 100
    player_proximity_radius: 100
    player_proximity_bonus: 50
    loot_table: "minecraft:gameplay/piglin_bartering"
     */
    int weight;
    double travelSpeed;
    Particle trailParticle;
    Particle beaconParticle;
    int beaconDuration;
    int playerWeight;
    int playerProximityRadius;
    int playerProximityBonus;
    LootTable lootTable;

    public StarType(ConfigurationSection starDescription, Stardust stardust) {
        weight = starDescription.getInt("weight");
        travelSpeed = starDescription.getDouble("travel_speed");
        trailParticle = Particle.valueOf(starDescription.getString("trail_particle"));
        beaconParticle = Particle.valueOf(starDescription.getString("beacon_particle"));
        beaconDuration = starDescription.getInt("beacon_duration");
        playerWeight = starDescription.getInt("player_weight");
        playerProximityRadius = starDescription.getInt("player_proximity_radius");
        playerProximityBonus = starDescription.getInt("player_proximity_bonus");
        String lootTableString = starDescription.getString("loot_table");
        if (lootTableString != null) {
            if (lootTableString.equals("stardust:common")) {
                lootTable = new CommonLootTable();
            } else if (lootTableString.equals("stardust:rare")) {
                lootTable = new RareLootTable();
            } else {
                String[] lootTableParts = lootTableString.split(":", 2);
                NamespacedKey namespacedKey = new NamespacedKey(lootTableParts[0], lootTableParts[1]);
                lootTable = stardust.getServer().getLootTable(namespacedKey);
            }
        }
    }
}
