package com.omnipico.stardust;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class Stardust extends JavaPlugin {
    Chat chat;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        chat = getServer().getServicesManager().load(Chat.class);
        FileConfiguration config = this.getConfig();

        ArrayList<StarType> starTypes = new ArrayList<>();
        ConfigurationSection starTypesSection = config.getConfigurationSection("star_types");
        if (starTypesSection != null) {
            Set<String> starTypesKeys = starTypesSection.getKeys(false);
            for (String starTypeKey : starTypesKeys) {
                if (starTypesSection.getConfigurationSection(starTypeKey) != null) {
                    starTypes.add(new StarType(starTypesSection.getConfigurationSection(starTypeKey), this));
                }
            }
        }

        StarFinder starFinder = new StarFinder(this, config, starTypes);
        starFinder.runTaskTimer(this, 0, 20);

        CommandStardust commandStardust = new CommandStardust(starFinder);
        this.getCommand("stardust").setExecutor(commandStardust);
        this.getCommand("stardust").setTabCompleter(commandStardust);

        //this.getCommand("pk").setExecutor(commandPK);
        //this.getCommand("pk").setTabCompleter(commandPK);
        //getServer().getPluginManager().registerEvents(new PicoItemListener(), this);
    }

    @Override
    public void onDisable() {
        //Fired when the server stops and disables all plugins
    }
}
