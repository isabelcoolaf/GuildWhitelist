package dev.sabel.i.GuildWhitelist;

import dev.sabel.i.GuildWhitelist.commands.GWLCommand;
import dev.sabel.i.GuildWhitelist.events.PlayerJoin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import net.hypixel.api.HypixelAPI;

import java.util.UUID;

public final class GuildWhitelist extends JavaPlugin {

    public HypixelAPI api;
    public String gid;
    public String kickmsg;
    public CustomAllowedHandler wlhandler;
    public boolean allowJoinOnFail;
    public boolean configured = false;

    @Override
    public final void onEnable() {

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        FileConfiguration config = getConfig();

        wlhandler = new CustomAllowedHandler(this);
        wlhandler.setup();

        Bukkit.getPluginManager().registerEvents(new PlayerJoin(this), this);
        getCommand("guildwhitelist").setExecutor(new GWLCommand(this));

        String apikey = config.getString("HypixelApiKey");
        String guildid = config.getString("GuildID");
        kickmsg = config.getString("DeniedMsg");
        allowJoinOnFail = config.getBoolean("AllowLoginOnGuildFetchFail");
        if (apikey == null || guildid == null || kickmsg == null) {
            getLogger().warning("Plugin not configured yet...");
            return;
        }
        configured = true;
        api = new HypixelAPI(UUID.fromString(apikey));
        gid = guildid;

        getLogger().info("Setup and enabled!");
    }
}
