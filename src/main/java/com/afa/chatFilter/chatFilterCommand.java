package com.afa.chatFilter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class chatFilterCommand implements CommandExecutor {
    private ChatFilter chatFilter;

    public chatFilterCommand(ChatFilter chatFilter) {
        this.chatFilter = chatFilter;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("chatFilter.admin")) {
                if (args.length == 2) {
                    switch (args[0].toLowerCase()) {
                        case "add":
                            // getting word
                            String word = args[1];
                            chatFilter.getConfig().getStringList("blacklisted-words").add(word);
                            break;
                        case "remove":
                            try {
                                for (String loopWord : chatFilter.getConfig().getStringList("blacklisted-words")) {
                                    if (loopWord.toLowerCase().equals(args[1].toLowerCase())) {
                                        chatFilter.getConfig().getStringList("blacklisted-words").remove(loopWord);
                                        player.sendMessage(ChatColor.RED + "Word removed.");
                                    } else {
                                        player.sendMessage("That word is not in the filter.");
                                    }
                                }
                            } catch (NullPointerException err) {
                                player.sendMessage(ChatColor.RED + "Something went wrong! Check console.");
                                Bukkit.getLogger().severe("An error occured running the chat filter remove command:");
                                err.printStackTrace();
                            }
                        default:
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /chatfilter add <word>"));
                            break;
                    }
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /chatfilter add <word>"));
                }
                //
            } else {
                player.sendMessage(ChatColor.RED + "Insufficient permissions!");
            }
        } else {
            Bukkit.getLogger().info("This command can only be run by players!");
        }
        return false;
    }
}
