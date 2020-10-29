package com.omnipico.stardust;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class StarFinder extends BukkitRunnable {
    Stardust stardust;
    FileConfiguration config;

    List<StarType> starTypes;
    WeightedRandomBag<StarType> starTypeBag;
    final double angleDelta = (1.0/9) * Math.PI;
    final double spawnHeight = 260;

    public StarFinder(Stardust stardust, FileConfiguration config, List<StarType> starTypes) {
        this.config = config;
        this.starTypes = starTypes;
        this.stardust = stardust;
        starTypeBag = new WeightedRandomBag<>();
        for (StarType starType : starTypes) {
            starTypeBag.addEntry(starType, starType.weight);
        }
    }

    public void spawnStar(World world, StarType starType) {
        SecureRandom random = new SecureRandom();
        List<Player> players = world.getPlayers();
        WeightedRandomBag<Player> playerBag = new WeightedRandomBag<>();
        for (Player player : players) {
            int weight = starType.playerWeight;
            for (Player player2 : players) {
                Location playerLocation = player.getLocation();
                Location player2Location = player2.getLocation();
                double distance = Math.hypot(playerLocation.getX() - player2Location.getX(), playerLocation.getY() - player2Location.getY());
                if (player2 != player && distance < starType.playerProximityRadius) {
                    weight += starType.playerProximityBonus;
                }
            }
            playerBag.addEntry(player, weight);
        }
        Player spawnOnPlayer = playerBag.getRandom();
        double r = config.getInt("shooting_star_radius") * Math.sqrt(random.nextDouble());
        double theta = random.nextDouble() * 2 * Math.PI;
        //Bukkit.getLogger().info("Spawning on player at " + spawnOnPlayer.getLocation().toString());
        int targetX = (int) (spawnOnPlayer.getLocation().getX() + (r * Math.cos(theta)));
        int targetZ = (int) (spawnOnPlayer.getLocation().getZ() + (r * Math.sin(theta)));
        Location target = world.getHighestBlockAt(new Location(world, targetX, 0, targetZ)).getLocation();
        //Bukkit.getLogger().info("Target = " + target.toString());
        theta = theta + Math.PI;
        //Bukkit.getLogger().info("Spawn between " + (theta-angleDelta) + " and " + (theta+angleDelta));
        theta += -angleDelta + (angleDelta * 2) * random.nextDouble();
        r += random.nextInt(config.getInt("shooting_star_radius"));
        double spawnX = target.getX() + (r * Math.cos(theta));
        double spawnZ = target.getZ() + (r * Math.sin(theta));
        Location spawnLocation = new Location(world, spawnX, spawnHeight, spawnZ);
        //Bukkit.getLogger().info("Spawn = " + spawnLocation.toString());
        ShootingStar shootingStar = new ShootingStar(starType, spawnLocation, target);
        shootingStar.runTaskTimer(stardust, 0, 1);
    }

    public void spawnRandomStar(World world) {
        StarType starType = starTypeBag.getRandom();
        spawnStar(world, starType);
    }

    public void run() {
        SecureRandom random = new SecureRandom();
        for (World world : stardust.getServer().getWorlds()) {
            if (starTypes.size() > 0 &&
                    world.getEnvironment().equals(World.Environment.NORMAL) &&
                    world.getTime() >= config.getInt("begin_starfall") &&
                    world.getTime() <= config.getInt("end_starfall") &&
                    world.getPlayers().size() > 0) {
                if (random.nextDouble() < config.getDouble("shooting_star_chance", 0.0001)) {
                    spawnRandomStar(world);
                }
            }
        }
    }
}
