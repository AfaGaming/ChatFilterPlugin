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
        String[] lines = e.getLines();
        List<String> blacklistedWords = chatFilter.getConfig().getStringList("blacklisted-words");
        List<String> replacedWords = new ArrayList<>();

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            for (String word : blacklistedWords) {
                if (line.toLowerCase().contains(word.toLowerCase())) {
                    line = line.replaceAll("(?i)" + word, "*".repeat(word.length())); // replace the (case-insensitive) word with * for all the word's charecters.
                    replacedWords.add(word);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', chatFilter.getConfig().getString("sign.blocked-message")));
                }
            }
        }

        if (!replacedWords.isEmpty()) {
            for (Player loopplayer : Bukkit.getOnlinePlayers()) {
                if (loopplayer.hasPermission("chatFilter.staff")) {
                    String staffMessage = ChatColor.translateAlternateColorCodes('&', chatFilter.getConfig().getString("sign.staff-message")
                                    .replace("%player%", player.getName()))
                            .replace("%words%", String.join(", ", replacedWords));
                    loopplayer.sendMessage(staffMessage);
                }
            }
        }
    }
}