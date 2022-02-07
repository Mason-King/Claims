package claims;


import claims.Commands.ClaimCommand;
import claims.Objects.Claim;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public final class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {
        // Plugin startup logic

        instance = this;

        this.getCommand("Claim").setExecutor(new ClaimCommand());

        saveConfig();
        saveResource("claims.yml", false);

    }

    @Override
    public void onDisable() {
        save();
        // Plugin shutdown logic
    }

    public static Main getInstance() {
        return instance;
    }

    public void save() {
        File file = new File(this.getDataFolder() + "claims.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        System.out.println(Claim.claims);
        for(Map.Entry e : Claim.claims.entrySet()) {
            Claim claim = (Claim) e.getValue();

            System.out.println(e.getKey());

            config.set("claims." + claim.getId() + ".owner", claim.getOwner().toString());
            config.set("ownerName", claim.getOwnerName());
            config.set("chunkX", claim.getChunkX());
            config.set("chunkZ", claim.getChunkZ());
        }

        try {
            config.save(file);
        } catch (IOException e) {

        }
    }

}
