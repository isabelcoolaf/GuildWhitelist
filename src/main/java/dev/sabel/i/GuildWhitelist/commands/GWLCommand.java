package dev.sabel.i.GuildWhitelist.commands;

import dev.sabel.i.GuildWhitelist.CustomAllowedHandler;
import dev.sabel.i.GuildWhitelist.GuildWhitelist;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.shanerx.mojang.Mojang;
import org.shanerx.mojang.PlayerProfile;

import java.util.UUID;

public final class GWLCommand implements CommandExecutor {

    private final GuildWhitelist plugin;
    private final CustomAllowedHandler cah;
    private final Mojang api = new Mojang().connect();
    public GWLCommand(GuildWhitelist p) {
        plugin = p;
        cah = p.wlhandler;
    }

    @Override
    public final boolean onCommand(CommandSender s, Command command, String label, String[] args) {
        boolean isConsole = !(s instanceof Player);

        if (!isConsole && !s.hasPermission("guildwhitelist.manage")) {
            s.sendMessage(ChatColor.RED + "You don't have permission to access this.");
            return true;
        }

        if (args.length == 0) {
            sendHelp(s);
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                if (cah.whitelist.isEmpty()) {
                    if (isConsole) {
                        plugin.getLogger().info("No exceptions! Add one using /gwl add or /gwl remove.");
                        return true;
                    }
                    s.sendMessage(ChatColor.GREEN + "No exceptions! Add some using" + ChatColor.YELLOW + " /gwl add" + ChatColor.GREEN + " or " + ChatColor.YELLOW + "/gwl remove" + ChatColor.GREEN + ".");
                    return true;
                }

                String listmsg = (isConsole) ? "List of Exceptions:\n" : ChatColor.AQUA + "List of Exceptions:\n";
                if (isConsole) {
                    plugin.getLogger().info("Generating list... this may take a minute depending on your list size.");
                } else {
                    s.sendMessage(ChatColor.GRAY + "Generating list... this may take a minute depending on your list size.");
                }

                for (UUID uid : cah.whitelist) {
                    OfflinePlayer pl = Bukkit.getOfflinePlayer(uid);
                    String pname = (pl.getName() == null) ? api.getPlayerProfile(uid.toString()).getUsername() : pl.getName();
                    listmsg = (isConsole) ? listmsg + pname + " - " + pl.getUniqueId() + "\n" : listmsg + ChatColor.YELLOW + pname + " - " + pl.getUniqueId() + "\n";
                }

                if (isConsole) {
                    plugin.getLogger().info(listmsg);
                } else {
                    s.sendMessage(listmsg);
                }

                return true;
            }

            if (args[0].equalsIgnoreCase("reloadwl")) {
                cah.setup();
                if (isConsole) {
                    plugin.getLogger().info("Exception list reloaded!");
                    return true;
                }
                s.sendMessage(ChatColor.GREEN + "Exception list reloaded!");
                return true;
            }

            sendHelp(s);
            return true;

        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("add")) {

                if (isConsole) {
                    plugin.getLogger().info("Processing...");
                } else {
                    s.sendMessage(ChatColor.GRAY + "Processing...");
                }

                try {
                    UUID uid = UUID.fromString(api.getUUIDOfUsername(args[1]).replaceAll("(.{8})(.{4})(.{4})(.{4})(.{12})", "$1-$2-$3-$4-$5"));
                    if (cah.whitelist.contains(uid)) {
                        if (isConsole) {
                            plugin.getLogger().info("That user is already in the exception list!");
                            return true;
                        }
                        s.sendMessage(ChatColor.RED + "That user is already in the exception list!");
                        return true;
                    }
                    cah.whitelist.add(uid);
                    cah.save();
                    String msg = args[1] + " has been added to the exception list!";
                    if (isConsole) {
                        plugin.getLogger().info(msg);
                        return true;
                    }
                    s.sendMessage(ChatColor.GREEN + msg);
                    return true;
                } catch (RuntimeException ignored) {}

                try {
                    UUID uid = UUID.fromString(args[1]);
                    if (cah.whitelist.contains(uid)) {
                        if (isConsole) {
                            plugin.getLogger().info("That user is already in the exception list!");
                            return true;
                        }
                        s.sendMessage(ChatColor.RED + "That user is already in the exception list!");
                        return true;
                    }
                    PlayerProfile p = api.getPlayerProfile(uid.toString());
                    cah.whitelist.add(uid);
                    cah.save();
                    String msg = p.getUsername() + " has been added to the exception list!";
                    if (isConsole) {
                        plugin.getLogger().info(msg);
                        return true;
                    }
                    s.sendMessage(ChatColor.GREEN + msg);
                    return true;
                } catch (RuntimeException ignored) {}

                String msg = "That doesn't seem to be a valid name or UUID!";
                if (isConsole) {
                    plugin.getLogger().info(msg);
                    return true;
                }
                s.sendMessage(ChatColor.RED + msg);
                return true;
            }

            if (args[0].equalsIgnoreCase("remove")) {
                if (isConsole) {
                    plugin.getLogger().info("Processing...");
                } else {
                    s.sendMessage(ChatColor.GRAY + "Processing...");
                }

                try {
                    UUID uid = UUID.fromString(api.getUUIDOfUsername(args[1]).replaceAll("(.{8})(.{4})(.{4})(.{4})(.{12})", "$1-$2-$3-$4-$5"));
                    if (!cah.whitelist.contains(uid)) {
                        if (isConsole) {
                            plugin.getLogger().info("That user is not in the exception list!");
                            return true;
                        }
                        s.sendMessage(ChatColor.RED + "That user is not in the exception list!");
                        return true;
                    }
                    cah.whitelist.remove(uid);
                    cah.save();
                    String msg = args[1] + " has been removed from the exception list!";
                    if (isConsole) {
                        plugin.getLogger().info(msg);
                        return true;
                    }
                    s.sendMessage(ChatColor.GREEN + msg);
                    return true;
                } catch (RuntimeException ignored) {}

                try {
                    UUID uid = UUID.fromString(args[1]);
                    if (!cah.whitelist.contains(uid)) {
                        if (isConsole) {
                            plugin.getLogger().info("That user is not in the exception list!");
                            return true;
                        }
                        s.sendMessage(ChatColor.RED + "That user is not in the exception list!");
                        return true;
                    }
                    PlayerProfile p = api.getPlayerProfile(uid.toString());
                    cah.whitelist.remove(uid);
                    cah.save();
                    String msg = p.getUsername() + " has been removed from the exception list!";
                    if (isConsole) {
                        plugin.getLogger().info(msg);
                        return true;
                    }
                    s.sendMessage(ChatColor.GREEN + msg);
                    return true;
                } catch (RuntimeException ignored) {}

                String msg = "That doesn't seem to be a valid name or UUID!";
                if (isConsole) {
                    plugin.getLogger().info(msg);
                    return true;
                }
                s.sendMessage(ChatColor.RED + msg);
                return true;
            }

            sendHelp(s);
            return true;
        }
        sendHelp(s);
        return true;
    }

    public final void sendHelp(CommandSender s) {
        String[] lines = {"Usage:", "/gwl add <username or UUID>", "/gwl remove <username or UUID>", "/gwl list", "/gwl reloadwl"};
        if (!(s instanceof Player)) {
            for (String l : lines) { plugin.getLogger().info(l); }
        } else {
            for (String l : lines) { s.sendMessage(ChatColor.RED + l); }
        }
    }
}
