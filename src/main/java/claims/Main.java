package claims;


import claims.Commands.ClaimCommand;
import claims.Commands.UnclaimCommand;
import claims.Listener.BlockBreak;
import claims.Listener.PlayerMove;
import claims.Objects.Claim;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapAPI;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.DynmapCommonAPIListener;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class Main extends JavaPlugin {

    private static Main instance;
    private static DynmapAPI api = (DynmapAPI) Bukkit.getServer().getPluginManager().getPlugin("dynmap");

    @Override
    public void onEnable() {
        // Plugin startup logic

        instance = this;

        this.getCommand("Claim").setExecutor(new ClaimCommand());
        this.getCommand("UnClaim").setExecutor(new UnclaimCommand());

        saveConfig();
        saveResource("claims.yml", false);
        saveResource("Guis/banGui.yml", false);
        saveResource("Guis/flagsGui.yml", false);
        saveResource("Guis/trustedFlagsGui.yml", false);
        saveResource("Guis/visitorFlagsGui.yml", false);

        load();

        this.getServer().getPluginManager().registerEvents(new PlayerMove(), this);
        this.getServer().getPluginManager().registerEvents(new BlockBreak(), this);

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
        File file = new File(this.getDataFolder() + "/claims.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        if(Claim.claims == null) return;
        for(Map.Entry e : Claim.claims.entrySet()) {
            Claim claim = (Claim) e.getValue();
            if(claim.isUnclaimed()) {
                config.set("claims." + claim.getId(), null);
                continue;
            }

            config.set("claims." + claim.getId() + ".owner", claim.getOwner().toString());
            config.set("claims." + claim.getId() + ".ownerName", claim.getOwnerName());
            config.set("claims." + claim.getId() + ".world", claim.getWorld().getName());
            config.set("claims." + claim.getId() + ".chunkX", claim.getChunkX());
            config.set("claims." + claim.getId() + ".chunkZ", claim.getChunkZ());
            config.set("claims." + claim.getId() + ".bans", claim.banned);
            config.set("claims." + claim.getId() + ".trusted", claim.trusted);

            config.set("claims." + claim.getId() + ".trustedBlockBreak", claim.isTrustedBlockBreak());
            config.set("claims." + claim.getId() + ".trustedBlockPlace", claim.isTrustedBlockPlace());
            config.set("claims." + claim.getId() + ".trustedUse", claim.isTrustedUse());
            config.set("claims." + claim.getId() + ".trustedChestOpen", claim.isTrustedOpenChest());
            config.set("claims." + claim.getId() + ".trustedDoorOpen", claim.isTrustedOpenDoor());

            config.set("claims." + claim.getId() + ".visitorBlockBreak", claim.isVisitorBlockBreak());
            config.set("claims." + claim.getId() + ".visitorBlockPlace", claim.isVisitorBlockPlace());
            config.set("claims." + claim.getId() + ".visitorUse", claim.isVisitorUse());
            config.set("claims." + claim.getId() + ".visitorChestOpen", claim.isVisitorOpenChest());
            config.set("claims." + claim.getId() + ".visitorDoorOpen", claim.isVisitorOpenDoor());

        }

        try {
            config.save(file);
        } catch (IOException e) {

        }
    }

    public void load() {
        File file = new File(this.getDataFolder() + "/claims.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        if(config.getConfigurationSection("claims") == null) return;
        config.getConfigurationSection("claims").getKeys(false).forEach(key -> {
            int id = Integer.parseInt(key);
            UUID ownerId = UUID.fromString(config.getString("claims." + key + ".owner"));
            String ownerName = config.getString("claims." + key + ".ownerName");
            World world = Bukkit.getWorld(config.getString("claims." + key + ".world"));
            int chunkX = config.getInt("claims." + key + ".chunkX");
            int chunkZ = config.getInt("claims." + key + ".chunkZ");
            List<String> banned = config.getStringList("claims." + key + ".bans");
            List<String> trusted = config.getStringList("claims." + key + ".trusted");

            Boolean visitorBlockBreak = config.getBoolean("claims." + key + ".visitorBlockBreak");
            Boolean visitorBlockPlace = config.getBoolean("claims." + key + ".visitorBlockPlace");
            Boolean visitorUse = config.getBoolean("claims." + key + ".visitorUse");
            Boolean visitorChestOpen = config.getBoolean("claims." + key + ".visitorChestOpen");
            Boolean visitorDoorOpen = config.getBoolean("claims." + key + ".visitorDoorOpen");

            Boolean trustedBlockBreak = config.getBoolean("claims." + key + ".trustedBlockBreak");
            Boolean trustedBlockPlace = config.getBoolean("claims." + key + ".trustedBlockPlace");
            Boolean trustedUse = config.getBoolean("claims." + key + ".trustedUse");
            Boolean trustedChestOpen = config.getBoolean("claims." + key + ".trustedChestOpen");
            Boolean trustedDoorOpen = config.getBoolean("claims." + key + ".trustedDoorOpen");

            Claim claim = new Claim(id, ownerId, world, ownerName, chunkX, chunkZ, banned, trusted, visitorBlockBreak, visitorBlockPlace, visitorUse, visitorChestOpen, visitorDoorOpen, trustedBlockBreak, trustedBlockPlace, trustedChestOpen, trustedDoorOpen, trustedUse);

        });

        System.out.println(Claim.playerClaims);
    }

    public static DynmapAPI getDynmap() {
        return api;
    }

}
