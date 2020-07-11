package dev.sabel.i.GuildWhitelist.events;

import dev.sabel.i.GuildWhitelist.CustomAllowedHandler;
import dev.sabel.i.GuildWhitelist.GuildWhitelist;
import net.hypixel.api.reply.GuildReply;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public final class PlayerJoin implements Listener {

    private final GuildWhitelist plugin;
    private final CustomAllowedHandler cah;
    public PlayerJoin(GuildWhitelist p) {
        plugin = p;
        cah = p.wlhandler;
    }

    @EventHandler
    private void onJoin(AsyncPlayerPreLoginEvent e) {
        String pname = e.getName();
        UUID puuid = e.getUniqueId();
        if (e.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) return;
        if (!plugin.configured) {
            plugin.getLogger().warning("User " + pname + " joined but I wasn't configured yet!");
            return;
        }

        // allow user if whitelisted before checking guild to avoid unnecessary requests
        if (cah.whitelist.contains(puuid)) return;

        GuildReply.Guild guild;
        try {
            guild = plugin.api.getGuildByPlayer(puuid).get().getGuild();
        } catch (InterruptedException | ExecutionException err) {
            err.printStackTrace();
            plugin.getLogger().info("Could not get guild for player " + pname +"!  Disallowing login as precaution.");
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.RED + "GuildWhitelist plugin error. Please message a server administrator.");
            return;
        }

        if (guild == null) {
            disallowJoin(e, pname);
            return;
        }
        if (!guild.get_id().equals(plugin.gid)) disallowJoin(e, pname);

    }

    private void disallowJoin(AsyncPlayerPreLoginEvent e, String p) {
        String msg = ChatColor.translateAlternateColorCodes('&', plugin.kickmsg);
        msg = ChatColor.RESET + msg + "\n\n\n" + ChatColor.RED + "If you believe this is an error, contact a server admin.";
        e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, msg);
        plugin.getLogger().info(p + " was denied entry to the server.");
    }

}
