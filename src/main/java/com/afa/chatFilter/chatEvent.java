package com.afa.chatFilter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;

public class chatEvent implements Listener {

    private ChatFilter chatFilter;
    public chatEvent(ChatFilter chatFilter) {
        this.chatFilter = chatFilter;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        String message = e.getMessage();
        List<String> blacklistedWords = chatFilter.getConfig().getStringList("blacklisted-words");

        for (String word : blacklistedWords) {
            if (message.toLowerCase().contains(word.toLowerCase())) {
                e.setCancelled(true);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', chatFilter.getConfig().getString("chat.blocked-message")));
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (onlinePlayer.hasPermission("chatFilter.staff")) {
                        String staffMessage = chatFilter.getConfig().getString("chat.staff-message")
                                .replace("%player%", player.getName()) // implement %player% placeholder
                                .replace("%message%", message); // implement %message% placeholder
                        onlinePlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', staffMessage));
                    }
                }
                return;
            }
        }
    }

}
