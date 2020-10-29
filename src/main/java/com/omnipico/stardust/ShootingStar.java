package com.omnipico.stardust;

import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.security.SecureRandom;
import java.util.Collection;

public class ShootingStar extends BukkitRunnable {
    StarType starType;
    Location position;
    Location target;
    int age = 0;
    double delta;
    boolean hit = false;
    int hitAge = 0;
    Vector angle;
    SecureRandom random = new SecureRandom();
    Item droppedItem;

    public ShootingStar(StarType starType, Location position, Location target) {
        this.starType = starType;
        this.position = position;
        this.target = target;
        delta = starType.travelSpeed / 20;
        position = position.setDirection(target.subtract(position).toVector().normalize());
        angle = position.getDirection();
        angle.multiply(delta);
    }

    public void run() {
        age++;
        World world = position.getWorld();
        if (world != null && !hit) {
            RayTraceResult result = position.getWorld().rayTrace(position, angle, delta, FluidCollisionMode.ALWAYS, true, 1, null);
            if (result == null) {
                position = position.add(angle);
                world.spawnParticle(starType.trailParticle, position, 5, 0.25, 0.25, 0.25, 0, null, true);
            } else {
                hit = true;
                LootContext context = new LootContext.Builder(position).killer(null).lootedEntity(null).lootingModifier(0).luck(0).build();
                Collection<ItemStack> items = starType.lootTable.populateLoot(random, context);
                for (ItemStack item : items) {
                    droppedItem = position.getWorld().dropItemNaturally(position, item);
                }
                if (world.getHighestBlockAt(position).getY() < position.getY()) {
                    position = world.getHighestBlockAt(position).getLocation();
                }
                //DROP ITEMS
            }
        } else if (world != null && hit) {
            hitAge++;
            if (hitAge > starType.beaconDuration || !droppedItem.isValid()) {
                this.cancel();
            }
            Location particlePosition = new Location(position.getWorld(), position.getX(), position.getY(), position.getZ());
            for (int i = 0; i < 30; i++) {
                world.spawnParticle(starType.beaconParticle, particlePosition, 5, 0.1, 0.5, 0.0, 0, null, true);
                particlePosition.add(0, 1, 0);
            }
        }
    }
}
