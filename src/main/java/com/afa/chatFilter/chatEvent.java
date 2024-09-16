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
        if (!player.hasPermission("chatFilter.bypass")) {
            String message = e.getMessage();
            List<String> blacklistedWords = chatFilter.getConfig().getStringList("blacklisted-words");

            // Normalize the player's message by removing non-alphabetic characters
            String normalizedMessage = message.toLowerCase().replaceAll("[^a-zA-Z]", "");

            // Loop through blacklisted words
            for (String word : blacklistedWords) {
                // Normalize each blacklisted word
                String normalizedWord = word.toLowerCase().replaceAll("[^a-zA-Z]", "");

                // Check if the normalized message contains the normalized blacklisted word
                if (normalizedMessage.contains(normalizedWord)) {
                    // If found, replace all instances in the original message with ***
                    message = message.replaceAll("(?i)" + word, "***");
                }
            }

            // Set the modified message
            e.setMessage(message);

            // Send the filtered message to staff (if necessary)
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.hasPermission("chatFilter.staff")) {
                    String staffMessage = chatFilter.getConfig().getString("chat.staff-message")
                            .replace("%player%", player.getName())  // Replace %player% placeholder
                            .replace("%message%", message);  // Replace %message% placeholder with filtered message
                    onlinePlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', staffMessage));
                }
            }
        }
    }
}