package com.afa.chatFilter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import java.util.ArrayList;
import java.util.List;

public class signEvent implements Listener {

    private ChatFilter chatFilter;

    public signEvent(ChatFilter chatFilter) {
        this.chatFilter = chatFilter;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        Player player = e.getPlayer();
        if (!player.hasPermission("chatFilter.bypass")) {
            String[] lines = e.getLines();
            List<String> blacklistedWords = chatFilter.getConfig().getStringList("blacklisted-words");
            List<String> replacedWords = new ArrayList<>();

            // Loop through each line of the sign
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                // Normalize the line (lowercase, no non-alphabet characters)
                String normalizedLine = line.toLowerCase().replaceAll("[^a-zA-Z]", "");

                for (String word : blacklistedWords) {
                    // Normalize each blacklisted word (lowercase, no non-alphabet characters)
                    String normalizedWord = word.toLowerCase().replaceAll("[^a-zA-Z]", "");

                    // Check if the normalized line contains the normalized word
                    if (normalizedLine.contains(normalizedWord)) {
                        // Replace the actual word in the original line with asterisks of the same length
                        line = line.replaceAll("(?i)" + word, "*".repeat(word.length()));
                        replacedWords.add(word);
                        e.setLine(i, line);  // Update the sign's text with the filtered line

                        // Notify the player that the message was blocked
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', chatFilter.getConfig().getString("sign.blocked-message")));
                    }
                }
            }

            // Notify staff if any blacklisted words were replaced
            if (!replacedWords.isEmpty()) {
                for (Player loopPlayer : Bukkit.getOnlinePlayers()) {
                    if (loopPlayer.hasPermission("chatFilter.staff")) {
                        String staffMessage = ChatColor.translateAlternateColorCodes('&', chatFilter.getConfig().getString("sign.staff-message")
                                        .replace("%player%", player.getName()))
                                .replace("%words%", String.join(", ", replacedWords));
                        loopPlayer.sendMessage(staffMessage);
                    }
                }
            }
        }
    }
}