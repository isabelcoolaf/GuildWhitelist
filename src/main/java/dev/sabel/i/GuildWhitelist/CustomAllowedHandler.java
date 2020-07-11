package dev.sabel.i.GuildWhitelist;


import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public final class CustomAllowedHandler {

    private final GuildWhitelist plugin;
    private File file;
    public FileConfiguration fconf;
    public ArrayList<UUID> whitelist = new ArrayList<>();
    public CustomAllowedHandler(GuildWhitelist p) {
        plugin = p;
    }

    public final void setup() {
        whitelist = new ArrayList<>();
        file = new File(plugin.getDataFolder(), "whitelisted.yml");
        boolean fe = true;
        if (!file.exists()) {
            fe = false;
            try {
                boolean s = file.createNewFile();
                if (!s) {
                    plugin.getLogger().warning("Could not create whitelist.yml file?");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        fconf = YamlConfiguration.loadConfiguration(file);
        fconf.options().copyDefaults(true);
        if (fe) {
            for (String uid : fconf.getStringList("whitelistedPlayers")) {
                whitelist.add(UUID.fromString(uid));
            }
        }
        save();
    }
    public void save() {
        try {
            ArrayList<String> sl = new ArrayList<>(whitelist.size());
            whitelist.forEach((id) -> sl.add(id.toString()));
            fconf.set("whitelistedPlayers", sl);
            fconf.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
