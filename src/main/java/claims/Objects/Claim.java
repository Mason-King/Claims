package claims.Objects;

import claims.Main;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Claim {

    Main main = Main.getInstance();

    private int id;
    private UUID owner;
    private String ownerName;
    private Chunk chunk;
    private World world;
    private Location chunkLocation;
    private int chunkX;
    private int chunkZ;

    //variable to know if it needs to be saved to help optimize!
    private boolean needsSaved = false;
    //Manage the players
    public List<String> banned = new ArrayList<>();
    public List<String> trusted = new ArrayList<>();

    //List of all current claims!
    public static Map<Integer, Claim> claims = new HashMap<>();
    public static Map<Chunk, Claim> chunkToClaims = new HashMap<>();
    public static Map<UUID, List<Claim>> playerClaims = new HashMap<>();

    public Claim(int id, UUID owner, World world, String ownerName, int chunkX, int chunkZ) {
        this.id = id;
        this.owner = owner;
        this.ownerName = ownerName;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;

        this.chunk = world.getChunkAt(chunkX, chunkZ);
        this.chunkLocation = new Location(world, chunkX, world.getHighestBlockYAt(chunkX, chunkZ), chunkZ);

        claims.put(id, this);
        chunkToClaims.put(chunk, this);
        List<Claim> temp = playerClaims.containsKey(id) ? playerClaims.get(id) : new ArrayList<>();
        temp.add(this);
        playerClaims.put(owner, temp);
    }

    public Claim(Player owner, Location loc) {
        this.owner = owner.getUniqueId();
        this.ownerName = owner.getName();
        this.chunk = loc.getChunk();
        this.world = loc.getWorld();
        this.chunkX = chunk.getX();
        this.chunkZ = chunk.getZ();
        this.id = claims.size() + 1;
        claims.put(claims.size() + 1, this);
        chunkToClaims.put(chunk, this);
        List<Claim> temp = playerClaims.containsKey(id) ? playerClaims.get(id) : new ArrayList<>();
        temp.add(this);
        playerClaims.put(owner.getUniqueId(), temp);
    }

    public static List<Claim> getClaims(Player p) {
        return playerClaims.get(p.getUniqueId());
    }

    public void banClaims(Player target) {
        for(Claim claim : playerClaims.get(owner)) {
            if(claim.isBanned(target)) continue;
            claim.banPlayer(target);
        }
    }

    public static Claim getClaimAt(World world, int x, int z) {
        Chunk c = world.getChunkAt(x, z);
        return chunkToClaims.get(c);
    }

    public void unbanClaims(Player target) {
        for(Claim claim : playerClaims.get(owner)) {
            if(!claim.isBanned(target)) continue;
            claim.unbanPlayer(target);
        }
    }

    public void banPlayer(Player p) {
        banned.add(p.getUniqueId().toString());
        banClaims(p);
    }

    public boolean isBanned(Player p) {
        if(banned.contains(p.getUniqueId().toString())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isTrusted(Player p) {
        if(trusted.contains(p.getUniqueId().toString())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean unbanPlayer(Player p) {
        if(banned.contains(p.getUniqueId().toString())) {
            banned.remove(p.getUniqueId().toString());
            return true;
        }
        return false;
    }

    public void trustPlayer(Player p) {
        trusted.add(p.getUniqueId().toString());
    }

    public boolean unTrust(Player p) {
        if(trusted.contains(p.getUniqueId().toString())) {
            trusted.remove(p.getUniqueId().toString());
            return true;
        }
        return false;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public UUID getOwner() {
        return owner;
    }

    public int getId() {
        return id;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public void setChunk(Chunk chunk) {
        this.chunk = chunk;
    }

    public Location getChunkLocation() {
        return chunkLocation;
    }

    public void setChunkLocation(Location chunkLocation) {
        this.chunkLocation = chunkLocation;
    }

    public int getChunkX() {
        return chunkX;
    }

    public void setChunkX(int chunkX) {
        this.chunkX = chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public void setChunkZ(int chunkZ) {
        this.chunkZ = chunkZ;
    }

    public boolean isNeedsSaved() {
        return needsSaved;
    }

    public void setNeedsSaved(boolean needsSaved) {
        this.needsSaved = needsSaved;
    }

}
