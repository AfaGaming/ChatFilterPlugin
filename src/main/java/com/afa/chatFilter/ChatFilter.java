package com.afa.chatFilter;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChatFilter extends JavaPlugin {

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        Bukkit.getPluginManager().registerEvents(new chatEvent(this), this);
        this.getCommand("chatfilter").setExecutor(new chatFilterCommand(this));

        Bukkit.getLogger().info("Plugin has started!");
    }
}
