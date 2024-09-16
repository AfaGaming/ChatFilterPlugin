package com.afa.chatFilter;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChatFilter extends JavaPlugin {

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        Bukkit.getPluginManager().registerEvents(new chatEvent(this), this);
        Bukkit.getPluginManager().registerEvents(new signEvent(this), this);
        this.getCommand("chatfilter").setExecutor(new chatFilterCommand(this));
        this.getCommand("chatfilter").setTabCompleter(new chatFilterTab());
        this.getCommand("debug").setExecutor(new debugCommand(this));

        Bukkit.getLogger().info("Plugin has started!");
    }
}
